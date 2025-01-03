package com.mallcloud.init.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置类
 * @author Admin
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha captchaProducer() {
        Properties properties = new Properties();
        // 配置验证码的属性
        // 是否有边框
        properties.setProperty("kaptcha.border", "no");
        // 字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 字符间隔
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "40");
        // 图片宽度
        properties.setProperty("kaptcha.image.width", "200");
        // 图片高度
        properties.setProperty("kaptcha.image.height", "50");
        // 字符长度
        properties.setProperty("kaptcha.textproducer.char.length", "5");
        // 干扰线
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");

        Config config = new Config(properties);
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
