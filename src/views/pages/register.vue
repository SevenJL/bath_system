<template>
  <div class="register-bg">
    <div class="register-container">
      <div class="register-header">
        <img class="logo mr10" src="../../assets/img/logo.svg" alt="" />
        <div class="register-title">用户注册</div>
      </div>
      <!-- 注册表单 -->
      <el-form :model="param" :rules="rules" ref="registerForm" size="large">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="用户名">
            <template #prepend>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 邮箱 -->
        <el-form-item prop="email">
          <el-input v-model="param.email" placeholder="邮箱">
            <template #prepend>
              <el-icon>
                <Mail />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
              type="password"
              v-model="param.password"
              placeholder="请输入密码"
              @keyup.enter="submitForm(registerForm)"
          >
            <template #prepend>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item prop="checkPassword">
          <el-input
              type="password"
              v-model="param.checkPassword"
              placeholder="请确认密码"
              @keyup.enter="submitForm(registerForm)"
          >
            <template #prepend>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 验证码 -->
        <el-form-item prop="captcha" class="verify-code-item">
          <div class="verify-code-container">
            <el-input
                class="verify-input"
                v-model="param.captcha"
                placeholder="验证码"
            />
            <el-button @click="getCodeImage" class="verify-btn">获取验证码</el-button>
            <img
                v-if="verifyCodeVisible"
                id="register-verify-code"
                class="verify-code-img"
                :src="verifyCodeSrc"
                alt="验证码"
                @click="getCodeImage"
            />
          </div>
        </el-form-item>

        <!-- “记住信息” 可选需求 -->
        <div class="pwd-tips">
          <el-checkbox class="pwd-checkbox" v-model="checked" label="记住注册信息" />
        </div>

        <!-- 注册按钮 -->
        <el-button
            class="register-btn"
            type="primary"
            size="large"
            @click="submitForm(registerForm)"
        >
          注册
        </el-button>

        <!-- 去登录链接 -->
        <p class="register-text">
          已有账号？
          <el-link type="primary" @click="$router.push('/login')">去登录</el-link>
        </p>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { Lock, User } from '@element-plus/icons-vue'; // 引入 Mail 图标
import { getVerifyCode, registerUser } from '@/api'; // 确保 registerUser 接口已实现

// 验证码是否显示
const verifyCodeVisible = ref(false);
const verifyCodeSrc = ref(''); // 存储验证码图片的 src

// 注册信息接口
interface RegisterInfo {
  username: string;
  email: string;
  password: string;
  checkPassword: string;
  captcha: string;
}

// 从 LocalStorage 中读取存储信息（若有）
const regStr = localStorage.getItem('register-param');
const defParam = regStr ? JSON.parse(regStr) : null;
const checked = ref(!!regStr);

// 路由实例
const router = useRouter();

// 表单数据
const param = reactive<RegisterInfo>({
  username: defParam ? defParam.username : '',
  email: defParam ? defParam.email : '',
  password: defParam ? defParam.password : '',
  checkPassword: defParam ? defParam.checkPassword : '',
  captcha: '',
});

// 表单引用
const registerForm = ref<FormInstance>();

// 表单验证规则
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 20, message: '用户名长度在4到20个字符', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' },
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== param.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
  captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
};

// 提交表单
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;

  try {
    await formEl.validate();
  } catch (error) {
    ElMessage.error('请完善表单信息');
    return;
  }

  // 组织后端所需的注册参数
  const userRegisterRequest = {
    userAccount: param.username,
    userPassword: param.password,
    checkPassword: param.checkPassword,
    email: param.email,
    captcha: param.captcha,
  };

  try {
    // 调用后端注册接口
    const response = await registerUser(userRegisterRequest);
    const { code, data, message } = response.data;
    if (code === 200) {
      ElMessage.success('注册成功');
      console.log('注册用户信息:', data);

      // 如果选择了记住信息，则存储到 localStorage
      if (checked.value) {
        localStorage.setItem('register-param', JSON.stringify(param));
      } else {
        localStorage.removeItem('register-param');
      }

      // 注册成功后可以直接跳转到登录页面
      await router.push('/login');
    } else {
      ElMessage.error(message || '注册失败，请检查输入');
    }
  } catch (error) {
    console.error('注册接口调用失败:', error);
    ElMessage.error('注册失败，请稍后重试');
  }
};

// 获取验证码图片
const getCodeImage = async () => {
  verifyCodeVisible.value = true;
  try {
    // 调用后端接口获取验证码的 Base64 编码
    const response = await getVerifyCode();
    const data = response.data;
    const base64 = data.data;

    // 更新验证码图片的 src
    verifyCodeSrc.value = `data:image/png;base64,${base64}`;
  } catch (error) {
    console.error('获取验证码失败:', error);
    ElMessage.error('获取验证码失败，请稍后重试');
  }
};

// 初始化时获取一次验证码
getCodeImage();
</script>

<style scoped>
.register-bg {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: url(../../assets/img/login-bg.jpg) center/cover no-repeat;
}

.register-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
}

.logo {
  width: 35px;
  margin-right: 10px;
}

.register-title {
  font-size: 22px;
  color: #333;
  font-weight: bold;
}

.register-container {
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

.register-btn {
  display: block;
  width: 100%;
}

.register-text {
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
  margin-bottom: 20px;
  display: flex; /* 使用 flex 布局排列子元素 */
  align-items: center; /* 垂直居中 */
  gap: 10px; /* 设置子元素之间的间距 */
}

.verify-input {
  width: 110px;
  margin-right: 10px;
  flex: 1; /* 输入框宽度自适应 */
}

.verify-btn {
  white-space: nowrap; /* 防止按钮内容换行 */
}

.verify-code-img {
  height: 40px; /* 设置验证码图片高度 */
  width: 100px; /* 宽度自适应 */
  border: 1px solid #dcdfe6; /* 图片边框 */
  border-radius: 4px; /* 图片圆角 */
  cursor: pointer; /* 鼠标移上变为手型 */
}
</style>
