package com.mallcloud.init.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.constant.UserConstant;
import com.mallcloud.init.mapper.FeedbackMapper;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;
import com.mallcloud.init.model.entity.User;
import com.mallcloud.init.model.enums.FEEDBACKTYPE;
import com.mallcloud.init.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Admin
 */
@Service
@Slf4j
public class FeedbackServiceImpl  extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }


    @Override
    public Boolean submitFeedback(FeedbackRequest feedbackRequest, HttpServletRequest request) {
        // 判断是否登录
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        String feedbackText = feedbackRequest.getFeedbackText();
        if (feedbackText == null || feedbackText.isEmpty()) {
            return false;
        }
        FEEDBACKTYPE feedbacktype = feedbackRequest.getFeedbackType();
        if (feedbacktype == null) {
            return false;
        }
        Feedback feedback = new Feedback();
        BeanUtil.copyProperties(feedbackRequest, feedback);
        feedback.setUserId(user.getUserId());
        int result = feedbackMapper.insert(feedback);
        return result > 0;
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return this.list();
    }
}
