package com.mallcloud.init.model.enums;

import lombok.Getter;

/**
 * 反馈类型枚举
 * @author Admin
 */
@Getter
public enum FEEDBACKTYPE {

    /**
     * 1. 投诉
     */
    COMPLAINT(1, "投诉"),

    /**
     * 2. 建议
     */
    SUGGESTION(2, "建议"),

    /**
     * 3. 举报
     */
    REPORT(3, "举报"),

    /**
     * 4. 其他
     */
    OTHER(4, "其他");

    private final int code;

    private final String desc;

   FEEDBACKTYPE(int code, String desc){
       this.code = code;
       this.desc = desc;
   }
}
