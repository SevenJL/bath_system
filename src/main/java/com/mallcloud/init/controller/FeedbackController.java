package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.PageRequest;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.model.dto.user.FeedbackRequest;
import com.mallcloud.init.model.entity.Feedback;
import com.mallcloud.init.model.vo.PageVO;
import com.mallcloud.init.service.impl.FeedbackServiceImpl;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse submitFeedback(@RequestBody FeedbackRequest feedbackRequest) {
        if (feedbackRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        boolean isSuccess = feedbackService.submitFeedback(feedbackRequest);
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
    public BaseResponse getAllFeedback() {
        // 获取所有反馈
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        return ResultUtils.success(feedbackList);
    }

    /**
     * 分页查询商品
     * @param pageRequest
     * @return 分页结果
     */
    @PostMapping("/page")
    public BaseResponse<PageVO<Feedback>> getStudentsByPage(@RequestBody PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        List<Feedback> feedbackList =  feedbackService.getAllFeedbackByPage(page,size);
        Integer total = feedbackService.getCounts();
        PageVO<Feedback> pageVO = new PageVO<>();
        pageVO.setData(feedbackList);
        pageVO.setPageTotal(total);

        return ResultUtils.success(pageVO);
    }

    /**
     * 删除反馈
     */
    @DeleteMapping("/{id}")
    public BaseResponse deleteFeedback(@PathVariable Long id) {
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        boolean b = feedbackService.deleteFeedbackById(id);
        if (!b) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }
}
