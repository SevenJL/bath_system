package com.mallcloud.init.model.entity;

import lombok.Data;

/**
 * @author Admin
 */
@Data
public class Feedback {
    // 反馈id
    private Integer feedbackId;

    // 用户id
    private Integer userId;

    // 反馈内容
    private String feedbackText;

    // 反馈时间
    private String createdTime;

    // 反馈类型
    private String feedbackType;
}
