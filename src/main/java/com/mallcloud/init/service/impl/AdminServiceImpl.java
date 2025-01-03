package com.mallcloud.init.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.mapper.AdminMapper;
import com.mallcloud.init.model.entity.Admin;
import com.mallcloud.init.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Admin
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Admin getAdminInfoByName(String adminName) {
        return adminMapper.selectByAdminName(adminName);
    }
}




