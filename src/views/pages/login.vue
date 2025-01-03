<template>
  <div class="login-bg">
    <div class="login-container">
      <div class="login-header">
        <img class="logo mr10" src="../../assets/img/logo.svg" alt=""/>
        <div class="login-title">后台管理系统</div>
      </div>
      <el-form :model="param" :rules="rules" ref="login" size="large">
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="用户名">
            <template #prepend>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              type="password"
              placeholder="密码"
              v-model="param.password"
              @keyup.enter="submitForm(login)"
          >
            <template #prepend>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="verifyCode" class="verify-code-item">
          <div class="verify-code-container">
            <el-input v-model="param.verifyCode" placeholder="验证码" class="verify-input"></el-input>
            <el-button @click="getCodeImage" class="verify-btn">获取验证码</el-button>
            <img v-if="verifyCode" id="verify-code" class="verify-code-img" src="" alt="">
          </div>
        </el-form-item>
        <div class="pwd-tips">
          <el-checkbox class="pwd-checkbox" v-model="checked" label="记住密码"/>
          <el-link type="primary" @click="$router.push('/reset-pwd')">忘记密码</el-link>
        </div>
        <el-button class="login-btn" type="primary" size="large" @click="submitForm(login)">登录</el-button>
        <p class="login-text">
          没有账号？
          <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
        </p>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive} from 'vue';
import {useTabsStore} from '@/store/tabs';
import {usePermissStore} from '@/store/permiss';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import type {FormInstance, FormRules} from 'element-plus';
import {Lock, User} from "@element-plus/icons-vue";
import {getVerifyCode, loginUser} from "@/api";

// 验证码是否存在
const verifyCode = ref(false);


// 登录信息
interface LoginInfo {
  username: string;
  password: string;
  verifyCode: string;
}

// 登录信息
const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(!!lgStr);

// 路由
const router = useRouter();
const param = reactive<LoginInfo>({
  userId: defParam ? defParam.userId : '',
  username: defParam ? defParam.username : '',
  password: defParam ? defParam.password : '',
  verifyCode: '',
});

// 表单验证
const rules: FormRules = {
  username: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur',
    },
  ],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}],
  verifyCode: [{required: true, message: '请输入验证码', trigger: 'blur'}],
};

// 权限
const permiss = usePermissStore();
const login = ref<FormInstance>();

// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;

  // 表单验证
  await formEl.validate(async (valid: boolean) => {
    if (!valid) {
      ElMessage.error('请完善表单信息');
      return false;
    }

    // 准备登录请求参数
    const userLoginRequest = {
      userId: param.userId,
      userAccount: param.username,
      userPassword: param.password,
      captcha: param.verifyCode,
    };

    try {
      // 调用后端登录接口
      const response = await loginUser(userLoginRequest);
      console.log('登录接口返回:', response);

      // 检查接口返回
      const {code, data, message} = response.data;
      if (code === 200) {
        ElMessage.success('登录成功');

        // 设置权限
        localStorage.setItem('vuems_name', param.username);
        const keys = permiss.defaultList[param.username === 'admin' ? 'admin' : 'user'];
        permiss.handleSet(keys);

        // 清除
        localStorage.removeItem('user_info');
        // 保存登录信息
        localStorage.setItem('user_info', JSON.stringify(data));

        // 记住密码逻辑
        if (checked.value) {
          localStorage.setItem('login-param', JSON.stringify(param));
        } else {
          localStorage.removeItem('login-param');
        }

        // 跳转到首页
        await router.push('/');
      } else {
        // 登录失败
        ElMessage.error(message || '登录失败，请检查输入');
      }
    } catch (error) {
      // 请求错误处理
      console.error('登录接口调用失败:', error);
      ElMessage.error('登录失败，请稍后重试');
    }
  });
};

// 清除tabs
const tabs = useTabsStore();
tabs.clearTabs();

// 获取验证码图片
const getCodeImage = async () => {
  verifyCode.value = true;
  try {
    // 调用后端接口获取验证码的 Base64 编码
    const response = await getVerifyCode();

    // 假设返回的是 JSON 格式，结构可能是 { data: 'base64_string' }
    const data = await response.data;

    // 从返回的 data 中获取 base64 字符串
    const base64 = data.data;
    // base64 字符串赋值给 img 标签的 src 属性
    const img = document.getElementById('verify-code') as HTMLImageElement;
    img.src = `data:image/png;base64,${base64}`;
    // 设置 img 标签的高度
    img.style.height = '40px';
    img.style.width = '30%';
    // 设置靠右
    img.style.float = 'right';
  } catch (error) {
    console.error('获取验证码失败:', error);
  }
};
</script>

<style scoped>
.login-bg {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: url(../../assets/img/login-bg.jpg) center/cover no-repeat;
}

.login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
}

.logo {
  width: 35px;
}

.login-title {
  font-size: 22px;
  color: #333;
  font-weight: bold;
}

.login-container {
  width: 450px;
  border-radius: 5px;
  background: #fff;
  padding: 40px 50px 50px;
  box-sizing: border-box;
}

.pwd-tips {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  margin: -10px 0 10px;
  color: #787878;
}

.pwd-checkbox {
  height: auto;
}

.login-btn {
  display: block;
  width: 100%;
}

.login-tips {
  font-size: 12px;
  color: #999;
}

.login-text {
  display: flex;
  align-items: center;
  margin-top: 20px;
  font-size: 14px;
  color: #787878;
}

.verify-code-item {
  margin-bottom: 20px; /* 适配整体间距 */
}

.verify-code-container {
  display: flex; /* 使用 flex 布局排列子元素 */
  align-items: center; /* 垂直居中 */
  gap: 10px; /* 设置子元素之间的间距 */
}

.verify-input {
  flex: 1; /* 输入框宽度自适应 */
}

.verify-btn {
  white-space: nowrap; /* 防止按钮内容换行 */
}

.verify-code-img {
  height: 40px; /* 设置验证码图片高度 */
  width: auto; /* 宽度自适应 */
  border: 1px solid #dcdfe6; /* 图片边框 */
  border-radius: 4px; /* 图片圆角 */
  cursor: pointer; /* 鼠标移上变为手型 */
}

</style>
