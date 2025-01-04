package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.DeleteRequest;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.mapper.CaptchaMapper;
import com.mallcloud.init.model.dto.user.UserLoginRequest;
import com.mallcloud.init.model.dto.user.UserRegisterRequest;
import com.mallcloud.init.model.entity.User;
import com.mallcloud.init.service.UserService;
import com.mallcloud.init.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Stream;

import static com.mallcloud.init.common.ErrorCode.PARAMS_ERROR;
import static com.mallcloud.init.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 * @author Admin
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private CaptchaMapper captchaMapper;

    /**
     * 分页查询用户
     * @param page 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/page")
    public BaseResponse<List<User>> getUsersByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        // 1. 调用getUsersByPage查询方法
        List<User> usersByPage = userService.getUsersByPage(page, size);
        
        // 2. 判断分页数据是否为空
        if (usersByPage.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有查询到用户数据！");
        }
        
        // 3. 返回分页结果
        return ResultUtils.success(usersByPage);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete")
    public BaseResponse<String> deleteUser(String userIds) {
        // 设置基础变量
        List<Integer> userIdList;
        // 判断是否为空
        if (userIds == null || userIds.isEmpty()) {
            throw new BusinessException(PARAMS_ERROR);
        }
        // 如果为单个删除
        if (!userIds.contains(",")) {
            userIdList = Stream.of(userIds).map(Integer::parseInt).toList();
        } else {
            // 否则为批量删除
            String[] split = userIds.split(",");
            userIdList = Stream.of(split).map(Integer::parseInt).toList();
        }
        // 改为集合
        userService.deleteUserByIds(userIdList);
        return ResultUtils.success("删除成功");
    }



    /**
     * 删除用户
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        // 1. 校验传入的参数是否为NULL
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(PARAMS_ERROR);
        }
        
        // 2. 进行删除
        boolean b = userService.deleteUserById(deleteRequest.getId());
        
        // 3. 返回数据
        return ResultUtils.success(b);
    }


    /**
     * 根据 id 获取用户
     */
    @GetMapping("/get")
    public BaseResponse<User> getUserById(long id) {
        // 1. 校验参数是否合法
        if (id <= 0) {
            throw new BusinessException(PARAMS_ERROR);
        }
        
        // 2. 根据 id 获取用户
        User user = userService.getUserInfo(id);
        
        // 3. 判断是否为空
        if(user == null || user.getUserId() == null) {
            // 抛出异常
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        
        // 4. 返回用户
        return ResultUtils.success(user);
    }


    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request  HttpServletRequest
     * @return 登录结果
     */

    @PostMapping("/login")
    public BaseResponse login(@RequestBody UserLoginRequest userLoginRequest,HttpServletRequest request) {
        // 从Session中获取存储的验证码
        String username = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        String captcha = userLoginRequest.getCaptcha();
        if (captcha == null) {
            // 验证码为空，返回错误消息
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"验证码为空");
        }

        // 验证用户名和密码
        User loginUser = userService.userLogin(username,password);

        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, loginUser);

        // 从数据库中取出验证码
        int i = captchaMapper.deleteCaptchaCode(captcha);
        if (i == 0) {
            // 验证码错误，返回错误消息
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"验证码错误");
        }
        // 登录成功，重定向到主页或其他页面
        return ResultUtils.success(loginUser);
    }

    /**
     * 注册用户
     */
    @PostMapping("/register")
    public BaseResponse userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验参数是否为空
        if (userRegisterRequest == null) {
            throw new BusinessException(PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String captcha = userRegisterRequest.getCaptcha();
        String name = userRegisterRequest.getName();
        String studentNumber = userRegisterRequest.getStudentNumber();
        String grade = userRegisterRequest.getGrade();
        String studentClass = userRegisterRequest.getStudentClass();

        // 判断参数是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, captcha,email
                ,name,studentNumber,grade,studentClass)) {
            throw new BusinessException(PARAMS_ERROR);
        }
        // 判断两次密码是否一致
        if (!userPassword.equals(checkPassword)){
            return ResultUtils.success("两次密码不一致");
        }
        // 判断邮箱格式是否正确
        if (!EmailUtils.isValidEmail(email)){
            return ResultUtils.success("邮箱格式不正确");
        }
        // 删除验证码
        int i = captchaMapper.deleteCaptchaCode(captcha);
        if (i == 0) {
            // 验证码错误，返回错误消息
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"验证码错误");
        }
        // 注册用户
        Boolean result = userService.userRegister(userRegisterRequest);

        return ResultUtils.success(result);
    }
}
