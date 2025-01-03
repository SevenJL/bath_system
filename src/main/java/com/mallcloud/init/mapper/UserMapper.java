package com.mallcloud.init.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mallcloud.init.model.entity.User;

import java.util.List;

/**
 * 用户数据库操作
 * @author Admin
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户信息
     */
    User selectWithUserAccount(String username, String password);

    /**
     * 根据账号获取用户
     */
    User getUserByAccount(String userAccount);

    /**
     * 获取用户信息
     */
    User getUserInfo(String userAccount);

    /**
     * 根据id获取用户信息
     */
    User getUserInfoById(long id);

    /**
     * 分页查询用户信息
     */
    List<User> selectUsersByPage(int offset, int size);

    /**
     * 批量删除用户
     */
    void deleteUserByIds(List<Integer> userIds);

    /**
     * 根据id删除用户
     */
    int deleteUserById(Long id);

    /**
     * 用户注册
     */
    Boolean userRegister(String userAccount, String hashPassword, String email);
}




