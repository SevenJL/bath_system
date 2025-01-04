package com.mallcloud.init.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.mapper.FeedbackMapper;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;
import com.mallcloud.init.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Admin
 */
@Service
@Slf4j
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }


    @Override
    public Boolean submitFeedback(FeedbackRequest feedbackRequest) {
        // 判断是否登录
        String feedbackText = feedbackRequest.getFeedbackText();
        if (feedbackText == null || feedbackText.isEmpty()) {
            return false;
        }
        String feedbacktype = feedbackRequest.getFeedbackType();
        if (feedbacktype == null) {
            return false;
        }
        Feedback feedback = new Feedback();
        BeanUtil.copyProperties(feedbackRequest, feedback);

        int result = feedbackMapper.insert(feedback);
        return result > 0;
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return this.list();
    }

    @Override
    public List<Feedback> getAllFeedbackByPage(int page, int size) {
        int offset = (page - 1) * size;
        // 2. 查询用户列表
        return feedbackMapper.getReservationByPage(offset, size);
    }

    @Override
    public Integer getCounts() {
        return feedbackMapper.getCounts();
    }
    @Override
    public boolean deleteFeedbackById(Long id) {
        return feedbackMapper.deleteFeedbackById(id) > 0;
    }
}
