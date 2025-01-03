package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.constant.UserConstant;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;
import com.mallcloud.init.model.entity.User;
import com.mallcloud.init.service.impl.FeedbackServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Admin
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

    public FeedbackController(FeedbackServiceImpl feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * 提交用户反馈
     *
     * @param feedbackRequest 反馈信息
     * @return 提交结果
     */
    @PostMapping("/submit")
    public BaseResponse submitFeedback(@RequestBody FeedbackRequest feedbackRequest, HttpServletRequest request) {
        if (feedbackRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        User user = (User)request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        boolean isSuccess = feedbackService.submitFeedback(feedbackRequest,request);
        if (!isSuccess) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "提交失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取所有用户反馈
     * @return 反馈列表
     */
    @GetMapping("/all")
    public BaseResponse getAllFeedback(HttpServletRequest request) {
        // 校验是否有权限
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String userRole = user.getUserRole();
        if (!UserConstant.ADMIN_ROLE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        return ResultUtils.success(feedbackList);
    }
}
