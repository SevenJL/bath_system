package com.mallcloud.init.model.dto.user;

import com.mallcloud.init.model.enums.FEEDBACKTYPE;
import lombok.Getter;


/**
 * @author Admin
 */
@Getter
public class FeedbackRequest {
    // 反馈类型
    private FEEDBACKTYPE feedbackType;

    // 反馈内容
    private String feedbackText;
}
