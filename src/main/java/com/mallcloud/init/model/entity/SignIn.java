package com.mallcloud.init.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Admin
 */
@Data
public class SignIn {

    private int signInId;
    private int userId;
    private LocalDateTime signInTime;
    private String status;
}
