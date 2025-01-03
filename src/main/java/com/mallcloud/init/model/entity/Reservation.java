package com.mallcloud.init.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Admin
 */
@Data
public class Reservation {

    // 预约ID
    private Integer reservationId;

    // 用户ID
    private Integer userId;

    // 预约时间
    private LocalDateTime startTime;

    // 结束时间
    private LocalDateTime endTime;

    // 预约状态
    private String status ;

    // 确认预约时间
    private LocalDateTime confirmationSentAt;

    // 创建时间
    private LocalDateTime createdAt = LocalDateTime.now();

}
