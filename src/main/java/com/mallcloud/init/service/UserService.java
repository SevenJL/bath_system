package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.dto.user.UserRegisterRequest;
import com.mallcloud.init.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 * @author Admin
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    User userLogin(String userAccount, String userPassword);

    /**
     * 获取用户信息
     */
    User getUserInfo(long id);

    /**
     * 分页获取用户信息
     */
    List<User> getUsersByPage(int page, int size);

    /**
     * 删除用户
     */
    void deleteUserByIds(List<Integer> userIdList);

    /**
     * 删除用户
     */
    boolean deleteUserById(Long id);

    /**
     * 用户注册
     */
    Boolean userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 根据用户名查询用户
     */
    User getUserByAccount(String username);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 根据学号删除用户
     */
    void deleteUserByStudentNumber(String studentNumber);

}
