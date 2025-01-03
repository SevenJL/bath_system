package com.mallcloud.init.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mallcloud.init.model.entity.Student;

import java.util.List;

/**
 * @author Admin
 */
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 插入学生信息
     */
    int insertStudent(Student student);

    /**
     * 分页查询学生信息
     */
    List<Student> getStudentsByPage(int offset, int size);

    /**
     * 删除学生信息
     */
    int deleteStudentById(Long id);

    /**
     * 根据学生id查询学生信息
     */
    Student getStudentInfoById(long id);

    /**
     * 更新学生信息
     */
    int updateStudentById(Student student);

    /**
     * 查询学生总数
     */
    Integer count();
}




