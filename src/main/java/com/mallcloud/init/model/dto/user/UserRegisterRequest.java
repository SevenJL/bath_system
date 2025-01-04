package com.mallcloud.init.model.dto.user;

import lombok.Data;

/**
 * 用户注册请求体
 * @author Admin
 */
@Data
public class UserRegisterRequest {

    private String userAccount;

    private String email;

    private String name;

    private String studentNumber;

    private String studentClass;

    private String grade;

    private String userPassword;

    private String checkPassword;

    private String captcha;

}
