package com.mallcloud.init.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * 验证码Mapper
 * @author Admin
 */
public interface CaptchaMapper {
    /**
     * 添加验证码
     */
    @Insert("insert into captchas (captcha_code) values (#{captchaCode})")
    void addCaptchaCode(String captchaCode);

    /**
     * 删除验证码
     */
    @Delete("delete from captchas where captcha_code = #{captchaCode}")
    int deleteCaptchaCode(String captchaCode);
}




