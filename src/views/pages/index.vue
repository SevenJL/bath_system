<template>
  <div class="home-container">
    <!-- 顶部右上角退出登录按钮 -->
    <div class="logout-container">
      <el-button
          type="primary"
          size="small"
          @click="logout"
      >
        退出登录
      </el-button>
    </div>

    <!-- 顶部欢迎标题 -->
    <div class="welcome-header">
      <h1>欢迎来到学生澡堂管理系统</h1>
      <p>管理学生澡堂信息更加简单高效！</p>
    </div>

    <!-- 管理员和普通用户的快捷卡片 -->
    <div v-if="isAdminBoole" class="shortcut-cards">
      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="$router.push('/system-student')"
      >
        <el-icon><Collection /></el-icon>
        <div class="card-content">
          <h3>管理功能</h3>
        </div>
      </el-card>
    </div>

    <!-- 如果不是管理员，显示普通的签到、预约和反馈功能 -->
    <div v-else class="shortcut-cards">
      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="handleCheckin"
      >
        <el-icon><User /></el-icon>
        <div class="card-content">
          <h3>签到</h3>
          <p>进行学生签到</p>
        </div>
      </el-card>

      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="handleSubmitPage"
      >
        <el-icon><Plus /></el-icon>
        <div class="card-content">
          <h3>预约</h3>
          <p>预约班级和教师</p>
        </div>
      </el-card>

      <el-card
          class="shortcut-card"
          shadow="hover"
          @click="handleFeedbackPage"
      >
        <el-icon><Message /></el-icon>
        <div class="card-content">
          <h3>反馈</h3>
          <p>提交您的意见和建议</p>
        </div>
      </el-card>

      <!-- 预约对话框 -->
      <el-dialog
          title="预约"
          v-model="appointmentDialogVisible"
          width="30%"
          @close="resetAppointmentForm"
      >
        <el-form ref="appointmentFormRef" :model="appointmentForm" :rules="appointmentRules" label-width="100px">
          <!-- 日期选择 -->
          <el-form-item label="预约日期" prop="date">
            <el-date-picker
                v-model="appointmentForm.date"
                type="date"
                placeholder="选择日期"
                :disabled-date="disablePastDates"
            ></el-date-picker>
          </el-form-item>

          <!-- 时间段选择 -->
          <el-form-item label="预约时间" prop="time">
            <el-time-select
                v-model="appointmentForm.time"
                :picker-options="timeOptions"
                placeholder="选择时间段"
            ></el-time-select>
          </el-form-item>

          <!-- 提交和重置按钮 -->
          <el-form-item>
            <el-button type="primary" @click="handleAppointmentSubmit" :loading="appointmentLoading">提交预约</el-button>
            <el-button @click="resetAppointmentForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

      <!-- 反馈对话框 -->
      <el-dialog
          title="反馈"
          v-model="feedbackDialogVisible"
          width="30%"
          @close="resetFeedbackForm"
      >
        <el-form ref="feedbackFormRef" :model="feedbackForm" :rules="feedbackRules" label-width="100px">
          <!-- 反馈类型选择 -->
          <el-form-item label="反馈类型" prop="feedbackType">
            <el-select v-model="feedbackForm.feedbackType" placeholder="请选择反馈类型">
              <el-option label="建议" value="建议"></el-option>
              <el-option label="问题" value="问题"></el-option>
              <el-option label="其他" value="其他"></el-option>
            </el-select>
          </el-form-item>

          <!-- 反馈内容输入 -->
          <el-form-item label="反馈内容" prop="feedbackText">
            <el-input
                type="textarea"
                v-model="feedbackForm.feedbackText"
                placeholder="请输入您的反馈意见或建议"
                rows="6"
            ></el-input>
          </el-form-item>

          <!-- 提交和重置按钮 -->
          <el-form-item>
            <el-button type="primary" @click="handleFeedbackSubmit" :loading="feedbackLoading">提交反馈</el-button>
            <el-button @click="resetFeedbackForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { User, Plus, Collection, Message } from "@element-plus/icons-vue";
import { signIn, appointment, submitFeedback } from "@/api"; // 确保 'appointment' 和 'submitFeedback' API 已导入
import { ElMessage } from "element-plus";

// 路由实例
const router = useRouter();

// 判断是否是管理员
const isAdminBoole = ref(false);

const isAdmin = () => {
  const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}');
  if (userInfo.userRole === 'admin') {
    console.log('当前用户是管理员');
    isAdminBoole.value = true;
  } else {
    console.log('当前用户不是管理员');
    isAdminBoole.value = false;
  }
};
isAdmin();

// 退出登录函数
const logout = () => {
  localStorage.removeItem('user_info');
  // 跳转到登录页面
  router.push('/login');
};

// 签到功能
const handleCheckin = async () => {
  const loginParam = JSON.parse(localStorage.getItem("user_info") || "{}");
  try {
    const res = await signIn(loginParam.userId);
    if (res.data.code === 200) {
      ElMessage.success(res.data.message);
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error("签到接口错误", error);
    ElMessage.error("签到失败");
  }
};

// 控制预约对话框的可见性
const appointmentDialogVisible = ref(false);

// 打开预约对话框
const handleSubmitPage = () => {
  console.log("点击了预约按钮");
  appointmentDialogVisible.value = true;
};

// 定义预约表单数据和验证规则
const appointmentForm = reactive({
  date: null,
  time: null,
});

const appointmentRules = {
  date: [{ required: true, message: "请选择日期", trigger: "change" }],
  time: [{ required: true, message: "请选择时间", trigger: "change" }],
};

// 禁止选择过去的日期
const disablePastDates = (time: Date) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return time.getTime() < today.getTime();
};

// 时间选项配置
const timeOptions = {
  start: "09:00",
  end: "22:00",
  step: "00:30",
};

// 表单引用
const appointmentFormRef = ref(null);

// 重置预约表单
const resetAppointmentForm = () => {
  console.log("重置预约表单");
  appointmentForm.date = null;
  appointmentForm.time = null;
  if (appointmentFormRef.value) {
    appointmentFormRef.value.resetFields();
  }
};

// 预约提交状态
const appointmentLoading = ref(false);

// 提交预约
const handleAppointmentSubmit = () => {
  if (appointmentFormRef.value) {
    appointmentFormRef.value.validate(async (valid: boolean) => {
      console.log("预约表单验证结果:", valid);
      if (valid) {
        const dateTimeISO = `${appointmentForm.date.toISOString().split("T")[0]}T${appointmentForm.time}:00`;
        const userInfo = JSON.parse(localStorage.getItem("user_info") || "{}");
        console.log("提交预约数据:", { userId: userInfo.userId, time: dateTimeISO });
        appointmentLoading.value = true;
        try {
          const res = await appointment({ userId: userInfo.userId, time: dateTimeISO });
          if (res.data.code === 200) {
            ElMessage.success("预约成功");
            appointmentDialogVisible.value = false; // 关闭弹窗
            resetAppointmentForm();
          } else {
            ElMessage.error(res.data.message);
          }
        } catch (error) {
          console.error("预约接口错误", error);
          ElMessage.error("预约失败");
        } finally {
          appointmentLoading.value = false;
        }
      } else {
        ElMessage.error("请填写完整的预约信息");
      }
    });
  } else {
    ElMessage.error("表单未找到");
  }
};

// 控制反馈对话框的可见性
const feedbackDialogVisible = ref(false);

// 打开反馈对话框
const handleFeedbackPage = () => {
  console.log("点击了反馈按钮");
  feedbackDialogVisible.value = true;
};

// 定义反馈表单数据和验证规则
const feedbackForm = reactive({
  feedbackType: '',
  feedbackText: '',
});

const feedbackRules = {
  feedbackText: [
    { required: true, message: "请输入反馈内容", trigger: "blur" },
    { min: 10, max: 500, message: "反馈内容长度在10到500字符之间", trigger: "blur" },
  ],
};

// 表单引用
const feedbackFormRef = ref(null);

// 重置反馈表单
const resetFeedbackForm = () => {
  console.log("重置反馈表单");
  feedbackForm.feedbackType = '';
  feedbackForm.feedbackText = '';
  if (feedbackFormRef.value) {
    feedbackFormRef.value.resetFields();
  }
};

// 反馈提交状态
const feedbackLoading = ref(false);

// 提交反馈
const handleFeedbackSubmit = () => {
  if (feedbackFormRef.value) {
    feedbackFormRef.value.validate(async (valid: boolean) => {
      console.log("反馈表单验证结果:", valid);
      if (valid) {
        const userInfo = JSON.parse(localStorage.getItem("user_info") || "{}");
        const feedbackData = {
          userId: userInfo.userId,
          feedbackText: feedbackForm.feedbackText,
          feedbackType: feedbackForm.feedbackType,
        };
        console.log("提交反馈数据:", feedbackData);
        feedbackLoading.value = true;
        try {
          const res = await submitFeedback(feedbackData);
          if (res.data.code === 200) {
            ElMessage.success("反馈提交成功！感谢您的意见和建议。");
            feedbackDialogVisible.value = false; // 关闭弹窗
            resetFeedbackForm();
          } else {
            ElMessage.error(res.data.message || "反馈提交失败，请重试。");
          }
        } catch (error) {
          console.error("反馈接口错误", error);
          ElMessage.error("反馈提交失败");
        } finally {
          feedbackLoading.value = false;
        }
      } else {
        ElMessage.error("请填写完整的反馈信息");
      }
    });
  } else {
    ElMessage.error("表单未找到");
  }
};
</script>

<style scoped>
.home-container {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
}

.logout-container {
  position: absolute;
  top: 20px;
  right: 20px;
}

.welcome-header {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-header h1 {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.welcome-header p {
  font-size: 16px;
  color: #666;
}

.shortcut-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  width: 100%;
  max-width: 800px;
}

.shortcut-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.shortcut-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.shortcut-card .card-content h3 {
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.shortcut-card .card-content p {
  margin-top: 5px;
  font-size: 14px;
  color: #666;
}

.el-icon {
  font-size: 40px;
  color: #409eff;
}
</style>
