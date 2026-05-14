package com.example.demo.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.demo.model.dto.ChatRequestDTO;
import com.example.demo.model.vo.ChatResponseVO;
import com.example.demo.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatClient chatClient;
    private final StringRedisTemplate stringRedisTemplate;

    // Redis key前缀
    private static final String CHAT_SESSION_PREFIX = "chat:session:";
    // 会话过期时间（2小时）
    private static final long SESSION_EXPIRE_HOURS = 2;
    // 最大历史轮数
    private static final int MAX_HISTORY_ROUNDS = 3;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder,
                          StringRedisTemplate stringRedisTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业、友好、简洁的中文智能助手，请结合历史对话上下文进行回答。")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        // 校验sessionId
        String sessionId = validateAndGetSessionId(requestDTO.getSessionId());
        String message = requestDTO.getMessage();

        String redisKey = CHAT_SESSION_PREFIX + sessionId;

        try {
            // 1. 读取历史消息
            List<String> records = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
            String historyText = "";
            if (records != null && !records.isEmpty()) {
                historyText = String.join("\n", records);
            }

            // 2. 拼接上下文
            String finalPrompt = buildFinalPrompt(historyText, message);

            // 3. 调用模型
            String answer = chatClient.prompt(finalPrompt)
                    .call()
                    .content();

            // 4. 保存本轮记录
            String recordText = "用户: " + message + "\n助手: " + answer;
            stringRedisTemplate.opsForList().rightPush(redisKey, recordText);

            // 5. 只保留最近3轮
            Long size = stringRedisTemplate.opsForList().size(redisKey);
            if (size != null && size > MAX_HISTORY_ROUNDS) {
                stringRedisTemplate.opsForList().trim(redisKey, size - MAX_HISTORY_ROUNDS, size - 1);
            }

            // 设置过期时间
            stringRedisTemplate.expire(redisKey, SESSION_EXPIRE_HOURS, TimeUnit.HOURS);

            return new ChatResponseVO(message, answer);

        } catch (Exception e) {
            logger.error("聊天服务处理异常，sessionId: {}, message: {}", sessionId, message, e);
            // 返回友好的错误信息给用户
            return new ChatResponseVO(message, "抱歉，当前服务暂时不可用，请稍后重试。");
        }
    }

    /**
     * 校验并获取sessionId，为空时生成新的sessionId
     */
    private String validateAndGetSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            // 生成新的sessionId
            String newSessionId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            logger.info("sessionId为空，生成新的sessionId: {}", newSessionId);
            return newSessionId;
        }
        // 校验sessionId格式（简单校验，避免非法字符）
        if (!sessionId.matches("^[a-zA-Z0-9_-]{1,64}$")) {
            logger.warn("sessionId格式非法，生成新的sessionId, original: {}", sessionId);
            return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }
        return sessionId;
    }

    /**
     * 构建最终的prompt
     */
    private String buildFinalPrompt(String historyText, String message) {
        StringBuilder finalPrompt = new StringBuilder();
        
        if (historyText != null && !historyText.isEmpty()) {
            finalPrompt.append("以下是历史对话：\n");
            finalPrompt.append(historyText);
            finalPrompt.append("\n\n");
        }
        
        finalPrompt.append("当前用户问题：\n");
        finalPrompt.append(message);
        
        return finalPrompt.toString();
    }
}