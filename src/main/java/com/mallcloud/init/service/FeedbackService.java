package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;

import java.util.List;

/**
 * @author Admin
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 提交反馈
     */
    Boolean submitFeedback(FeedbackRequest feedbackRequest);

    /**
     * 获取所有反馈
     */
    List<Feedback> getAllFeedback();

    /**
     * 获取所有反馈
     */
    List<Feedback> getAllFeedbackByPage(int page, int size);

    /**
     * 获取反馈总数
     */
    Integer getCounts();

    /**
     * 删除反馈
     */
    boolean deleteFeedbackById(Long id);
}
