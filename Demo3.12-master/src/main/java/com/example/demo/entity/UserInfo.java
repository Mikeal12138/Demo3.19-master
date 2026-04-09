package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 用户个人信息实体类
 * 对应数据库表：user_info
 */
@TableName("user_info") // 绑定数据库表名 user_info
public class UserInfo {

    @TableId(type = IdType.AUTO) // 主键自增（对应 SERIAL 自增）
    private Integer id;

    private String realName;  // 真实姓名（数据库字段：real_name）

    private String phone;     // 手机号码

    private String address;   // 联系地址

    private Integer userId;   // 关联 sys_user 表的 id（数据库字段：user_id）

    // Getter 和 Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}