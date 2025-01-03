package com.mallcloud.init.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.mapper.CaptchaMapper;
import com.mallcloud.init.mapper.UserMapper;
import com.mallcloud.init.model.entity.User;
import com.mallcloud.init.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.mallcloud.init.common.ErrorCode.PARAMS_ERROR;
import static com.mallcloud.init.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 * @author Admin
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private CaptchaMapper captchaMapper;

    @Override
    public List<User> getUsersByPage(int page, int size) {
        // 1. 计算偏移量
        int offset = (page - 1) * size;
        // 2. 查询用户列表
        return userMapper.selectUsersByPage(offset, size);
    }

    @Override
    @Transactional // 添加事务注解
    public void deleteUserByIds(List<Integer> userIds) {
        // 调用userMapper的deleteUserByIds删除方法
        userMapper.deleteUserByIds(userIds);
    }

    @Override
    public User userLogin(String userAccount, String userPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(PARAMS_ERROR, "参数为空");
        }
        // 查询用户是否存在
        User user = userMapper.getUserByAccount(userAccount);

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 2. 密码匹配
        // 对传入的密码进行加密
        String hashPassword = MD5.create().digestHex(userPassword);
        // 比较用户输入的密码的哈希值是否和数据库中的哈希密码是否一致
        if (!Objects.equals(user.getPassword(), hashPassword)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 3. 查询用户信息并返回
        return userMapper.getUserInfo(userAccount);
    }

    @Override
    public boolean deleteUserById(Long id) {
        // 1. 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(PARAMS_ERROR);
        }
        // 2. 进行删除
        return userMapper.deleteUserById(id) > 0;
    }

    @Override
    public User getUserInfo(long id) {
        return userMapper.getUserInfoById(id);
    }


    @Override
    public Boolean userRegister(String userAccount, String userPassword, String email) {
        // 校验是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword,email)) {
            throw new BusinessException(PARAMS_ERROR);
        }
        // 对密码进行哈希加密
        String hashPassword = MD5.create().digestHex(userPassword);
        log.info("加密后的password = {}", hashPassword);
        return userMapper.userRegister(userAccount, hashPassword,email);
    }

    @Override
    public User getUserByAccount(String username) {
        // 1. 校验参数
        if (StringUtils.isBlank(username)) {
            throw new BusinessException(PARAMS_ERROR);
        }
        return userMapper.getUserByAccount(username);
    }

    /**
     * 获取当前登录用户
     *
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getUserId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getUserId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
}
