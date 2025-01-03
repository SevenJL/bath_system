package com.mallcloud.init.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mallcloud.init.model.dto.StudentUpdateDTO;
import com.mallcloud.init.model.entity.Student;

import java.util.List;

/**
*/
public interface StudentService extends IService<Student> {

    /**
     * 更新学生信息
     */
    boolean updateStudentById(StudentUpdateDTO studentUpdateDTO);

    /**
     * 删除学生信息
     */
    boolean deleteStudentById(Long id);

    /**
     * 分页查询学生信息
     */
    List<Student> getStudentsByPage(int page, int size);

    /**
     * 查询学生总数
     */
    Integer getCounts();
}
