package com.mallcloud.init.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class StudentInsertDTO implements Serializable {
    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年级
     */
    private String grade;

    /**
     * 班级
     */
    private String studentClass;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    
}
