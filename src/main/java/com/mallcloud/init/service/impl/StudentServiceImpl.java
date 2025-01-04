package com.mallcloud.init.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mallcloud.init.mapper.StudentMapper;
import com.mallcloud.init.model.dto.StudentInsertDTO;
import com.mallcloud.init.model.dto.StudentUpdateDTO;
import com.mallcloud.init.model.entity.Student;
import com.mallcloud.init.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Admin
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private UserServiceImpl userService;

    @Override
    public List<Student> getStudentsByPage(int page, int size) {
        // 1. 计算偏移量
        int offset = (page - 1) * size;
        // 2. 查询用户列表
        return studentMapper.getStudentsByPage(offset, size);
    }

    @Override
    @Transactional
    public boolean deleteStudentById(Long id) {
        // 1. 校验参数
        if (id == null || id <= 0) {
            return false;
        }
        // 从数据库查询出studentNumber
        Student student = studentMapper.getStudentInfoById(id);
        if (student == null) {
            return false;
        }
        if (student.getStudentNumber() == null || student.getStudentNumber().isEmpty()) {
            return false;
        }
        String studentNumber = student.getStudentNumber();
        userService.deleteUserByStudentNumber(studentNumber);
        // 2. 进行删除
        return studentMapper.deleteStudentById(id) > 0;
    }

    @Override
    public boolean updateStudentById(StudentUpdateDTO studentUpdateDTO) {
        // 1. 判断参数是否为空
        if (studentUpdateDTO == null) {
            return false;
        }
        if (studentUpdateDTO.getName() == null || studentUpdateDTO.getName().isEmpty()) {
            return false;
        }
        if (studentUpdateDTO.getStudentNumber() == null || studentUpdateDTO.getStudentClass().isEmpty()) {
            return false;
        }
        // 2. 更新用户信息
        Student student = new Student();
        student.setName(studentUpdateDTO.getName());
        student.setGrade(studentUpdateDTO.getGrade());
        student.setStudentClass(studentUpdateDTO.getStudentClass());
        student.setStudentNumber(studentUpdateDTO.getStudentNumber());
        student.setStudentId(studentUpdateDTO.getStudentId());
        return studentMapper.updateStudentById(student) > 0;
    }

    public Boolean insertStudent(StudentInsertDTO studentInsertDTO) {
        // 1. 判断参数是否为空
        if (studentInsertDTO == null) {
            return false;
        }
        if (studentInsertDTO.getName() == null || studentInsertDTO.getName().isEmpty()) {
            return false;
        }
        if (studentInsertDTO.getStudentNumber() == null || studentInsertDTO.getStudentClass().isEmpty()) {
            return false;
        }
        // 2. 插入用户信息
        Student student = new Student();
        student.setName(studentInsertDTO.getName());
        student.setGrade(studentInsertDTO.getGrade());
        student.setStudentClass(studentInsertDTO.getStudentClass());
        student.setStudentNumber(studentInsertDTO.getStudentNumber());
        return studentMapper.insertStudent(student) > 0;
    }

    @Override
    public Integer getCounts() {
        // 查询总数
        return studentMapper.count();
    }
}




