package com.mallcloud.init.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class Admin implements Serializable {

    /**
     * 管理员id
     */
    private Integer id;

    /**
     * 管理员名称
     */
    private String name;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 管理员邮箱
     */
    private String email;

    /**
     * 管理员手机号
     */
    private String phone;

    /**
     * 管理员头像
     */
    private String avatar;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
