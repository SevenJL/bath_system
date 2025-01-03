package com.mallcloud.init.constant;

import lombok.Data;

/**
 * @author Admin
 */
@Data
public class MagicConstant {
    // 时间段名称
    public static final String TIME_SLOT_6_9 = "6:00-9:00";
    public static final String TIME_SLOT_9_12 = "9:00-12:00";
    public static final String TIME_SLOT_12_15 = "12:00-15:00";
    public static final String TIME_SLOT_15_18 = "15:00-18:00";
    public static final String TIME_SLOT_18_21 = "18:00-21:00";
    public static final String TIME_SLOT_21_AFTER = "21:00-6:00";

    // 时间格式
    public static final String TIME_FORMAT = "HH:mm";

    // 字段名称
    public static final String SIGN_IN_TIME = "signInTime";
    public static final String SIGN_OUT_TIME = "signOutTime";

    private MagicConstant() {
        // 私有化构造器，防止实例化
    }
}
