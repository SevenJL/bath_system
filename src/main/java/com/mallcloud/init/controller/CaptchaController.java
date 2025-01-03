package com.mallcloud.init.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.mapper.CaptchaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 验证码控制器
 * @author Admin
 */
@Slf4j
@RestController
public class CaptchaController {

    private final DefaultKaptcha captchaProducer;

    private final CaptchaMapper captchaMapper;

    public CaptchaController(DefaultKaptcha captchaProducer, CaptchaMapper captchaMapper) {
        this.captchaProducer = captchaProducer;
        this.captchaMapper = captchaMapper;
    }

    @GetMapping("/captcha")
    public BaseResponse getCaptcha(HttpServletResponse response, HttpServletRequest request) throws IOException {
        // 设置响应类型为图片
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        // 生成验证码文本
        String captchaText = captchaProducer.createText();
        log.info("验证码文本：{}", captchaText);
        // 将验证码文本存储在Session中
        request.getSession().setAttribute("captcha", captchaText);
        captchaMapper.addCaptchaCode(captchaText);

        // 根据文本生成验证码图片
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        // 将验证码图片转换为 Base64 字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "JPEG", baos);
        byte[] imageBytes = baos.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // 返回 Base64 编码的图片
        return ResultUtils.success(base64Image);
    }
}
