package com.mallcloud.init.controller;

import com.mallcloud.init.common.BaseResponse;
import com.mallcloud.init.common.ErrorCode;
import com.mallcloud.init.common.ResultUtils;
import com.mallcloud.init.exception.BusinessException;
import com.mallcloud.init.model.dto.StudentInsertDTO;
import com.mallcloud.init.model.dto.StudentUpdateDTO;
import com.mallcloud.init.model.entity.Student;
import com.mallcloud.init.model.vo.PageVO;
import com.mallcloud.init.service.impl.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mallcloud.init.common.ErrorCode.PARAMS_ERROR;


/**
 * 学生控制器
 * @author Admin
 */
@Slf4j
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    /**
     * 分页查询商品
     * @param page 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/page")
    public BaseResponse<PageVO<Student>> getStudentsByPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        
        // 1. 调用查询方法
        List<Student> studentsByPage = studentService.getStudentsByPage(page, size);
        
        // 2. 判断分页数据是否为空
        if (studentsByPage.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "没有查询到商品数据！");
        }
        
        // 3. 查询总数
        Integer total = studentService.getCounts();
        PageVO<Student> pageVO = new PageVO<>();
        pageVO.setData(studentsByPage);
        pageVO.setPageTotal(total);
        
        // 4. 返回分页结果
        return ResultUtils.success(pageVO);
    }


    /**
     * 删除用户
     */
    @DeleteMapping("{id}")
    public BaseResponse<Boolean> deleteStudent(@PathVariable Long id) {
        // 1. 校验传入的参数是否为NULL
        if (id == null || id <= 0) {
            throw new BusinessException(PARAMS_ERROR);
        }
        
        // 2. 进行删除
        boolean b = studentService.deleteStudentById(id);
        
        // 3. 返回数据
        return ResultUtils.success(b);
    }

    /**
     * 更新学生信息
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateStudent(@RequestBody StudentUpdateDTO studentUpdateDTO) {
        if(studentUpdateDTO == null) {
            throw new BusinessException(PARAMS_ERROR);
        }
        // 调用更新方法
        boolean b = studentService.updateStudentById(studentUpdateDTO);
        return ResultUtils.success(b);
    }

    /**
     * 添加学生
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addStudent(@RequestBody StudentInsertDTO studentInsertDTO) {
        
        // 调用添加方法
        Boolean b = studentService.insertStudent(studentInsertDTO);
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }
}
