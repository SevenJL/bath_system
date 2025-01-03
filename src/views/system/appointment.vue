<template>
  <div class="appointment-container">
    <el-card class="box-card" shadow="always">
      <h2 class="title">预约与签到</h2>
      <!-- 预约与签到 Tabs -->
      <el-tabs v-model="activeTab">
        <!-- Tab 1: 预约功能 -->
        <el-tab-pane label="预约" name="appointment">
          <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
            <!-- 日期选择 -->
            <el-form-item label="预约日期" prop="date">
              <el-date-picker
                  v-model="form.date"
                  type="date"
                  placeholder="选择日期"
                  :disabled-date="disablePastDates"
              ></el-date-picker>
            </el-form-item>

            <!-- 时间段选择 -->
            <el-form-item label="预约时间" prop="time">
              <el-time-select
                  v-model="form.time"
                  :picker-options="timeOptions"
                  placeholder="选择时间段"
              ></el-time-select>
            </el-form-item>

            <!-- 提交按钮 -->
            <el-form-item>
              <el-button type="primary" @click="handleSubmit">提交预约</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- Tab 2: 签到功能 -->
        <el-tab-pane label="签到" name="checkin">
          <div class="checkin-container">
            <!-- 签到按钮部分 -->
            <div class="checkin-button">
              <el-button type="success" @click="handleCheckin">签到</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script lang="ts">
import {appointment, signIn} from "@/api";
import {ElMessage} from 'element-plus';
export default {
  data() {
    return {
      activeTab: "appointment",
      form: {
        username: "",
        date: null,
        time: null,
      },
      rules: {
        date: [{ required: true, message: "请选择日期", trigger: "change" }],
        time: [{ required: true, message: "请选择时间", trigger: "change" }],
      },
      checkinForm: {
        username: "",
      },
      timeOptions: {
        start: "09:00",
        end: "22:00",
        step: "00:30",
        minTime: "09:00",
        maxTime: "22:00",
      },
      checkinRecords: [],
    };
  },
  methods: {
    disablePastDates(date) {
      return date.getTime() < Date.now() - 86400000;
    },
    async handleSubmit() {
      if (!this.form.date || !this.form.time) {
        console.error("日期或时间未选择");
        return;
      }

      // 将日期和时间结合为一个完整的时间格式
      const date = this.form.date.toISOString().split("T")[0]; // 获取 YYYY-MM-DD
      const time = this.form.time; // 获取 HH:mm

      // 组合成 ISO 时间格式 (yyyy-MM-ddTHH:mm:ss)
      const dateTimeISO = `${date}T${time}:00`;

      // 或组合成自定义格式 (yyyy-MM-dd HH:mm:ss)
      const dateTimeCustom = `${date} ${time}:00`;

      console.log("ISO 格式:", dateTimeISO);
      console.log("自定义格式:", dateTimeCustom);

      // 提交预约信息到后端
      const loginParam = JSON.parse(localStorage.getItem("user_info") || "{}");
      console.log("预约人信息:", loginParam);
      const res = await appointment({
        userId: loginParam.userId,
        time: dateTimeISO,
      });
      console.log("预约结果:", res);
      if (res.data.code === 200) {
        ElMessage.success("预约成功");
      } else {
        ElMessage.error(res.data.message);
      }
    },
    async handleCheckin() {
      const loginParam = JSON.parse(localStorage.getItem("user_info") || "{}");
      const res = await signIn(loginParam.userId);
      if (res.data.code === 200) {
        ElMessage.success(res.data.message);
      } else {
        ElMessage.error(res.data.message);
      }
    },
  },
};
</script>

<style scoped>
/* 容器样式 */
.appointment-container {
  max-width: 800px;
  margin: 50px auto;
  padding: 20px;
  background: linear-gradient(135deg, #ffffff, #f9f9f9);
  border-radius: 16px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  animation: fadeIn 0.8s ease-in-out;
}

/* 标题样式 */
.title {
  text-align: center;
  margin-bottom: 30px;
  font-size: 28px;
  color: #333;
  font-weight: bold;
  font-family: "Arial", sans-serif;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.1);
}

/* 签到按钮样式 */
.checkin-button {
  display: flex;
  justify-content: center; /* 居中按钮 */
  margin: 20px 0; /* 增加上下间距 */
}
.el-button[type="success"] {
  background: linear-gradient(90deg, #4caf50, #81c784);
  color: white;
  border-radius: 20px;
  padding: 12px 24px;
  font-size: 18px;
  font-weight: bold;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}
.el-button[type="success"]:hover {
  background: linear-gradient(90deg, #66bb6a, #a5d6a7);
  transform: scale(1.1);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}
.el-button[type="success"]:active {
  transform: scale(1.05);
  background: linear-gradient(90deg, #388e3c, #66bb6a);
}

/* 签到记录样式 */
.checkin-records {
  margin-top: 20px;
}
.checkin-records h3 {
  margin-bottom: 10px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}
</style>
