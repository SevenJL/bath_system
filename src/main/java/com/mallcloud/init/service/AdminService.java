package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.entity.Admin;

/**
 * @author admin
 */
public interface AdminService extends IService<Admin> {

    /**
     * 获取管理员信息
     */
    Admin getAdminInfoByName(String username);


}
