package com.mallcloud.init.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生表
 */
@TableName(value ="student")
@Data
public class Student implements Serializable {
    /**
     * 学生id
     */
    @TableId(type = IdType.AUTO)
    private Integer studentId;

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