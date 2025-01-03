package com.mallcloud.init.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mallcloud.init.model.entity.Admin;


/**
 * @author Admin
 */
public interface AdminMapper extends BaseMapper<Admin> {


    /**
     * 根据管理员名称查询管理员
     */
    Admin selectByAdminName(String adminName);
}




