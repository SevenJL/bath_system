import request from '../utils/request';
import {LoginParams} from "@/types/login";
import {PageParams, Student} from "@/types/student";
import {Teacher} from "@/types/teacher";




// 获取表格数据
export const fetchData = () => {
    return request({
        url: './mock/table.json',
        method: 'get'
    });
};

// 获取用户数据
export const fetchUserData = () => {
    return request({
        url: './mock/user.json',
        method: 'get'
    });
};

// 获取角色数据
export const fetchRoleData = () => {
    return request({
        url: './mock/role.json',
        method: 'get'
    });
};

// 登录请求
export const loginUser = (data: LoginParams) => {
    return request({
        url: 'api/user/login',
        method: 'post',
        data
    });
};

// 获取学生列表数据
export const getStudentDataByPage = (data: PageParams) => {
    return request({
        url: '/api/student/page?page=' + data.page + '&size=' + data.size,
        method: 'get',
    });
};

// 删除学生
export const deleteStudent = (id: number) => {
    return request({
        url: `/api/student/${id}`,
        method: 'delete',
    });
};

// 更新学生信息
export const updateStudent = (data: Student) => {
    return request({
        url: '/api/student/update',
        method: 'post',
        data
    });
}

// 新增学生
export const addStudent = (data: Student) => {
    return request({
        url: '/api/student/add',
        method: 'post',
        data
    });
}

// 获取教师列表数据
export const getTeacherDataByPage = (data: PageParams) => {
    return request({
        url: '/api/teacher/page?page=' + data.page + '&size=' + data.size,
        method: 'get',
    });
};

// 删除教师
export const deleteTeacher = (id: number) => {
    return request({
        url: `/api/teacher/${id}`,
        method: 'delete',
    });
};

// 更新教师信息
export const updateTeacher = (data: Teacher) => {
    return request({
        url: '/api/teacher/update',
        method: 'post',
        data
    });
}

// 新增教师
export const addTeacher = (data: Teacher) => {
    return request({
        url: '/api/teacher/add',
        method: 'post',
        data
    });
}

// 获取验证码
export const getVerifyCode = () => {
    return request({
        url: '/api/captcha',
        method: 'get',
    });
}

// 注册用户
export const registerUser = (data: LoginParams) => {
    return request({
        url: '/api/user/register',
        method: 'post',
        data
    });
}

// 签到逻辑
export const signIn = (userId: number) => {
    return request({
        url: '/api/sign-in/sign?userId=' + userId,
        method: 'get',
    });
}

// 预约逻辑
export const appointment = (data) => {
    return request({
        url: '/api/reservations',
        method: 'post',
        data
    });
}

// 确认预约
export function confirmReservation(data) {
    console.log(data);
    return request({
        url: '/api/reservations/confirm',
        method: 'post',
        data
    })
}

// 取消预约
export function deleteReservation(reservationId) {
    return request({
        url: `/api/reservations/${reservationId}/cancel`,
        method: 'put',
    });
}

// 获取预约列表
export function getReservations(data: PageParams) {
    console.log(data);
    return request({
        url: '/api/reservations/page',
        method: 'post',
        data
    });
}
// 获取图表信息
export function getChartInfo() {
    return request({
        url: '/api/sign-in/classify',
        method: 'get',
    });
}
