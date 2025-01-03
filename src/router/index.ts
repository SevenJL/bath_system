import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';
import { usePermissStore } from '@/store/permiss';
import Home from '../views/home.vue';
import SystemStudent from '../views/system/student.vue';
import SystemTeacher from '../views/system/teacher.vue';
import SystemIndex from '../views/system/index.vue';
import SystemAppointment from '../views/system/appointment.vue';
import SystemReservation from '../views/system/reservation.vue';
import SystemBathUseage from '../views/system/bathuseage.vue';

import Login from '../views/pages/login.vue';
import Register from '../views/pages/register.vue';
import Forbidden from '../views/pages/403.vue';
import NotFound from '../views/pages/404.vue';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

// 路由配置
const routes: RouteRecordRaw[] = [
    {
        path: '/',
        redirect: '/system-student',
    },
    {
        path: '/',
        name: 'Home',
        component: Home,
        children: [
            {
                path: '/system-student',
                name: 'system-student',
                meta: {
                    title: '用户管理',
                    permiss: '11',
                },
                component: SystemStudent,
            },
            {
                path: '/system-teacher',
                name: 'system-teacher',
                meta: {
                    title: '老师管理',
                    permiss: '12',
                },
                component: SystemTeacher,
            },
            {
                path: '/system-index',
                name: 'system-index',
                meta: {
                    title: '首页管理',
                    permiss: '13',
                },
                component: SystemIndex,
            },
            {
                path: '/system-appointment',
                name: 'system-appointment',
                meta: {
                    title: '预约界面',
                    permiss: '14',
                },
                component: SystemAppointment,
            },
            {
                path: '/system-reservation',
                name: 'system-reservation',
                meta: {
                    title: '预约管理',
                    permiss: '15',
                },
                component: SystemReservation,
            },
            {
                path: '/system-bathuseage',
                name: 'system-bathuseage',
                meta: {
                    title: '使用情况',
                    permiss: '15',
                },
                component: SystemBathUseage,
            },
        ],
    },
    {
        path: '/login',
        meta: {
            title: '登录',
            noAuth: true,
        },
        component: Login,
    },
    {
        path: '/register',
        meta: {
            title: '注册',
            noAuth: true,
        },
        component: Register,
    },
    {
        path: '/403',
        meta: {
            title: '没有权限',
            noAuth: true,
        },
        component: Forbidden,
    },
    {
        path: '/404',
        meta: {
            title: '找不到页面',
            noAuth: true,
        },
        component: NotFound,
    },
    { path: '/:path(.*)', redirect: '/404' },
];

const router = createRouter({
    history: createWebHashHistory(),
    routes,
});

// 路由守卫
router.beforeEach((to, from, next) => {
    NProgress.start();
    const role = localStorage.getItem('vuems_name');
    const permiss = usePermissStore();

    if (!role && to.meta.noAuth !== true) {
        next('/login');
    } else if (typeof to.meta.permiss == 'string' && !permiss.key.includes(to.meta.permiss)) {
        // 如果没有权限，则进入403
        next('/403');
    } else {
        next();
    }
});

// 路由守卫
router.afterEach(() => {
    NProgress.done();
});

export default router;
