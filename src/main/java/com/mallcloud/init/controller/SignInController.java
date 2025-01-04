package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.model.entity.SignIn;
import com.mallcloud.init.service.impl.SignInServiceImpl;
import com.mallcloud.init.utils.TimeClassifierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Admin
 */
@Slf4j
@RestController
@RequestMapping("/sign-in")
public class SignInController {

    private final SignInServiceImpl signInService;

    public SignInController(SignInServiceImpl signInService) {
        this.signInService = signInService;
    }

    // 用户签到接口
    @GetMapping("/sign")
    public BaseResponse signIn(@RequestParam("userId") int userId) {
        try {
            signInService.signIn(userId);
            return ResultUtils.success("ok");
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    // 更新签到状态为已完成
    @PostMapping("/complete")
    public BaseResponse completeSignIn(@RequestParam("userId") int userId) {
        try {
            signInService.completeSignIn(userId);
            return ResultUtils.success("ok");
        } catch (Exception e) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
    }

    /**
     * 对签到进行分类
     */
    @GetMapping("/classify")
    public BaseResponse classifyByTime() {
        List<SignIn> signInList = signInService.getAllData();
        if (signInList == null || signInList.isEmpty()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "没有签到数据");
        }
        Map<String, Integer> stringIntegerMap = TimeClassifierUtil.classifySignInsByTime(signInList);
        if (stringIntegerMap.isEmpty()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        log.info("分类成功！:{}", stringIntegerMap);

        return ResultUtils.success(stringIntegerMap);
    }
}
