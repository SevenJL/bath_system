package com.mallcloud.init.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Admin
 */
public class EmailUtils {

    // 正则表达式，匹配有效的邮箱地址
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * 校验邮箱是否有效
     * @param email 需要校验的邮箱地址
     * @return 如果邮箱格式正确，返回 true；否则返回 false
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // 创建正则表达式对象
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        // 使用 matcher 进行匹配
        return matcher.matches();
    }

}
