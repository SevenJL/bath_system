package com.mallcloud.init.model.dto.user;

import lombok.Getter;


/**
 * @author Admin
 */
@Getter
public class FeedbackRequest {
    // 反馈人ID
    private String userId;

    // 反馈类型
    private String feedbackType;

    // 反馈内容
    private String feedbackText;
}
