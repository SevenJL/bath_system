import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    role: '', // 'admin' | 'teacher' | 'student'
    userId: '',
    username: '',
    // 其他用户信息...
  }),
  
  actions: {
    // 设置用户信息
    setUserInfo(userInfo: any) {
      this.role = userInfo.userRole
      this.userId = userInfo.userId
      this.username = userInfo.username
    },
    
    // 清除用户信息
    clearUserInfo() {
      this.role = ''
      this.userId = ''
      this.username = ''
    }
  }
}) 