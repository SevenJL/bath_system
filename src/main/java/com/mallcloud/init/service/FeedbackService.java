package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Admin
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 提交反馈
     */
    Boolean submitFeedback(FeedbackRequest feedbackRequest, HttpServletRequest request);

    /**
     * 获取所有反馈
     */
    List<Feedback> getAllFeedback();

}
