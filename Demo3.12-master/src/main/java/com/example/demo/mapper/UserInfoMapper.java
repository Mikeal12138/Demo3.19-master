package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper // 标识该接口为 MyBatis 的 Mapper，交由 Spring 容器管理代理对象
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    // 继承 BaseMapper

    @Select("""
        SELECT
            u.id AS userId,
            u.username,
            i.real_name AS realName,
            i.phone,
            i.address
        FROM sys_user u
        LEFT JOIN user_info i ON u.id = i.user_id
        WHERE u.id = #{userId}
        """)
    UserDetailVO getUserDetail(@Param("userId") Long userId);
}