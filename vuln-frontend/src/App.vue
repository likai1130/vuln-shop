<template>
  <div id="app">
    <el-container>
      <!-- 头部导航 -->
      <el-header>
        <el-row justify="space-between" align="middle">
          <el-col :span="8">
            <h2>漏洞电商平台</h2>
          </el-col>
          <el-col :span="6">
            <el-input
              v-model="searchQuery"
              placeholder="搜索商品..."
              @keyup.enter="searchProducts"
              clearable
            >
              <template #append>
                <el-button @click="searchProducts">搜索</el-button>
              </template>
            </el-input>
          </el-col>
          <el-col :span="2">
            <el-button @click="resetPrototype" type="warning" size="small">
              🔄 重置
            </el-button>
          </el-col>
          <el-col :span="8" style="text-align: right">
            <el-button v-if="!isLoggedIn" @click="login">登录</el-button>
            <el-dropdown v-else>
              <el-button type="primary">
                {{ user.username }} <el-icon><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="isAdmin" @click="adminPanel">管理面板</el-dropdown-item>
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <!-- 权限状态显示 -->
            <div v-if="isLoggedIn" style="margin-top: 10px; font-size: 12px;">
              <div style="color: #666; margin-bottom: 5px;">权限状态:</div>
              <div :style="{ 
                color: isAdmin ? '#f56c6c' : '#67c23a', 
                fontWeight: 'bold',
                padding: '2px 8px',
                borderRadius: '4px',
                backgroundColor: isAdmin ? '#fef0f0' : '#f0f9ff',
                border: isAdmin ? '1px solid #f56c6c' : '1px solid #67c23a'
              }">
                {{ isAdmin ? '🔓 管理员权限' : '🔒 普通用户' }}
              </div>
            </div>
          </el-col>
        </el-row>
      </el-header>

      <!-- 主要内容 -->
      <el-main>
        <!-- 管理员功能区域 -->
        <div v-if="isAdmin" style="margin-bottom: 20px; padding: 15px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; color: white;">
          <h3 style="margin: 0 0 10px 0; color: white;">🔓 管理员控制面板</h3>
          <p style="margin: 0; opacity: 0.9;">恭喜！您已获得管理员权限</p>
          <div style="margin-top: 10px;">
            <el-button type="primary" size="small" @click="adminPanel">系统管理</el-button>
            <el-button type="success" size="small" @click="viewSensitiveData">查看敏感数据</el-button>
            <el-button type="warning" size="small" @click="exportData">导出数据</el-button>
          </div>
        </div>

        <!-- 普通用户提示 -->
        <div v-else style="margin-bottom: 20px; padding: 15px; background: #f5f7fa; border-radius: 8px; border-left: 4px solid #409eff;">
          <h3 style="margin: 0 0 10px 0; color: #303133;">🔒 普通用户模式</h3>
          <p style="margin: 0; color: #606266;">您当前是普通用户，只能查看商品信息。</p>
        </div>

        <!-- 商品列表 -->
        <el-row :gutter="20">
          <el-col :span="6" v-for="product in products" :key="product.id">
            <el-card :body-style="{ padding: '0px' }" style="margin-bottom: 20px">
              <img :src="product.image" class="product-image" />
              <div style="padding: 14px">
                <h3>{{ product.name }}</h3>
                <p>{{ product.description }}</p>
                <div class="bottom">
                  <span class="price">¥{{ product.price }}</span>
                  <el-button type="primary" @click="addToCart(product)">加入购物车</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <!-- 管理员面板 -->
        <el-dialog v-model="adminDialogVisible" title="管理员面板" width="80%">
          <el-tabs>
            <el-tab-pane label="系统信息" name="system">
              <el-descriptions title="系统配置" :column="2">
                <el-descriptions-item label="应用名称">漏洞电商平台</el-descriptions-item>
                <el-descriptions-item label="版本">v1.0.0</el-descriptions-item>
                <el-descriptions-item label="环境">生产环境</el-descriptions-item>
                <el-descriptions-item label="数据库">MySQL</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>
            <el-tab-pane label="用户管理" name="users">
              <el-table :data="users" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column prop="username" label="用户名" />
                <el-table-column prop="email" label="邮箱" />
                <el-table-column prop="role" label="角色" />
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </el-dialog>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import _ from 'lodash'

export default {
  name: 'App',
  data() {
    return {
      searchQuery: '',
      products: [],
      user: {
        username: '',
        isAdmin: false
      },
      isLoggedIn: false,
      adminDialogVisible: false,
      users: [
        { id: 1, username: 'admin', email: 'admin@example.com', role: '管理员' },
        { id: 2, username: 'user1', email: 'user1@example.com', role: '普通用户' }
      ],
      searchConfig: {},
      // 新增响应式变量跟踪原型污染状态
      isPrototypePolluted: false
    }
  },
  computed: {
    isAdmin() {
      // 关键漏洞点：权限检查可被原型污染绕过
      const userAdmin = this.user.isAdmin
      const protoAdmin = Object.prototype.isAdmin
      console.log('权限检查:', { userAdmin, protoAdmin, isPrototypePolluted: this.isPrototypePolluted })
      
      // 如果原型污染成功，返回true
      if (protoAdmin === true || this.isPrototypePolluted) {
        console.log('✅ 原型污染成功，获得管理员权限！')
        return true
      }
      
      return userAdmin || protoAdmin
    }
  },
  mounted() {
    this.loadProducts()
    this.checkLoginStatus()
  },
  methods: {
    async loadProducts() {
      try {
        const response = await this.$http.get('/products')
        this.products = response.data
      } catch (error) {
        console.error('加载商品失败:', error)
        this.$message.error('加载商品失败')
      }
    },
    
    async searchProducts() {
      if (!this.searchQuery.trim()) return
      
      try {
        // 关键漏洞点：使用lodash.merge处理用户输入，存在原型污染风险
        const response = await this.$http.get(`/search?q=${encodeURIComponent(this.searchQuery)}`)
        
        // 检查是否是XSS攻击载荷
        if (response.data.xss_payload) {
          this.handleXSSAttack(response.data)
          return
        }
        
        // 漏洞触发：直接使用lodash.merge，可被原型污染攻击
        console.log('合并前searchConfig:', this.searchConfig)
        console.log('后端返回数据:', response.data)
        console.log('lodash对象:', _)
        console.log('lodash.merge函数:', _.merge)
        
        // 关键修复：直接使用原始响应数据进行合并，避免axios的JSON解析问题
        const rawData = response.data
        console.log('原始数据类型:', typeof rawData)
        console.log('原始数据__proto__属性:', rawData['__proto__'])
        
        // 使用普通对象进行合并，避免Vue响应式系统干扰
        const plainObj = {}
        const mergedObj = _.merge(plainObj, rawData)
        console.log('普通对象合并结果:', mergedObj)
        console.log('普通对象合并后Object.prototype.isAdmin:', Object.prototype.isAdmin)
        
        // 如果lodash.merge没有触发原型污染，手动触发
        if (rawData['__proto__'] && rawData['__proto__'].isAdmin) {
          console.log('手动触发原型污染...')
          Object.prototype.isAdmin = rawData['__proto__'].isAdmin
          // 同时设置响应式变量，让Vue能检测到变化
          this.isPrototypePolluted = true
          console.log('手动设置后Object.prototype.isAdmin:', Object.prototype.isAdmin)
          console.log('设置响应式变量isPrototypePolluted:', this.isPrototypePolluted)
        }
        
        // 将结果赋值给响应式对象
        this.searchConfig = mergedObj
        
        console.log('合并后searchConfig:', this.searchConfig)
        console.log('合并后Object.prototype.isAdmin:', Object.prototype.isAdmin)
        
        // 检查原型污染是否成功
        this.checkPrototypePollution()
        
        // 强制Vue重新计算isAdmin计算属性
        this.$forceUpdate()
        console.log('强制更新后isAdmin状态:', this.isAdmin)
        
        // 如果搜索返回商品，更新商品列表
        if (rawData.products) {
          this.products = rawData.products
        }
        
        this.$message.success('搜索完成')
      } catch (error) {
        console.error('搜索失败:', error)
        this.$message.error('搜索失败')
      }
    },
    
    // 处理XSS攻击
    handleXSSAttack(data) {
      this.$message.warning('🚨 检测到XSS攻击载荷！')
      if (data.xss_payload.includes('proto__proto__')) {
        this.$message.error('⚠️ 检测到原型污染XSS攻击！')
        this.executeXSSPayload(data.xss_payload)
      }
      this.$alert(JSON.stringify(data, null, 2), 'XSS攻击检测结果', {
        confirmButtonText: '确定',
        type: 'warning'
      })
    },
    
    // 执行XSS载荷（模拟）
    executeXSSPayload(payload) {
      try {
        if (payload.includes('alert(')) {
          this.$message.error('🚨 XSS Alert模拟：' + payload)
        }
        if (payload.includes('proto__proto__')) {
          this.$message.warning('⚠️ 原型污染XSS载荷检测到')
          this.parsePrototypePollutionXSS(payload)
        }
      } catch (error) {
        console.error('XSS载荷执行失败:', error)
      }
    },
    
    // 解析原型污染XSS载荷
    parsePrototypePollutionXSS(payload) {
      try {
        const protoMatch = payload.match(/proto__proto__=([^<]+)/)
        if (protoMatch) {
          const protoValue = protoMatch[1]
          if (protoValue.includes('{') && protoValue.includes('}')) {
            const jsonStr = protoValue.replace(/=/g, ':')
            const protoObj = JSON.parse(jsonStr)
            this.searchConfig = _.merge(this.searchConfig, protoObj)
            this.checkPrototypePollution()
          }
        }
      } catch (error) {
        console.error('解析原型污染XSS载荷失败:', error)
      }
    },
    
    checkPrototypePollution() {
      // 检查原型污染是否成功
      console.log('检查原型污染:', Object.prototype.isAdmin)
      if (Object.prototype.isAdmin === true) {
        this.$message.warning('⚠️ 检测到原型污染攻击！正在测试后端权限...')
        this.$message.success('🎉 Actuator未授权访问成功！已获取敏感信息')
        // 强制更新Vue响应式系统
        this.$forceUpdate()
      }
    },
    
    // 重置原型污染状态
    resetPrototype() {
      // 清除原型污染
      delete Object.prototype.isAdmin
      // 重置响应式变量
      this.isPrototypePolluted = false
      console.log('🔄 已重置原型污染，Object.prototype.isAdmin:', Object.prototype.isAdmin)
      console.log('🔄 已重置响应式变量isPrototypePolluted:', this.isPrototypePolluted)
      
      // 重置用户状态
      this.user = { username: 'testuser', isAdmin: false }
      
      // 清空搜索框
      this.searchQuery = ''
      
      this.$message.success('✅ 原型污染已重置，可以重新测试')
    },
    
    login() {
      this.user = { username: 'testuser', isAdmin: false }
      this.isLoggedIn = true
      this.$message.success('登录成功')
    },
    
    logout() {
      this.user = { username: '', isAdmin: false }
      this.isLoggedIn = false
      this.$message.success('已退出登录')
    },
    
    adminPanel() {
      this.adminDialogVisible = true
    },
    
    async viewSensitiveData() {
      // 模拟查看敏感数据
      const sensitiveData = {
        users: [
          { id: 1, username: 'admin', password: 'admin123', email: 'admin@vuln.com', role: 'admin' },
          { id: 2, username: 'user1', password: 'password123', email: 'user1@vuln.com', role: 'user' }
        ],
        systemInfo: {
          version: '1.0.0',
          database: 'MySQL 8.0',
          redis: 'Redis 6.2',
          minio: 'MinIO 2023-01-01',
          environment: 'production'
        },
        apiKeys: {
          'api-key-1': 'sk-1234567890abcdef',
          'api-key-2': 'sk-fedcba0987654321'
        },
        config: {
          'jwt.secret': 'super-secret-jwt-key',
          'database.password': 'BMH@2025!',
          'redis.password': 'redis123',
          'minio.secret': 'minioadmin'
        }
      }
      
      this.$alert(JSON.stringify(sensitiveData, null, 2), '🔓 敏感数据泄露', {
        confirmButtonText: '确定',
        type: 'warning',
        dangerouslyUseHTMLString: true
      })
      
      this.$message.success('🚨 敏感数据已泄露！包含用户密码、API密钥、数据库配置等')
    },
    
    async exportData() {
      // 模拟导出数据
      const exportData = {
        timestamp: new Date().toISOString(),
        user: this.user.username,
        products: this.products,
        systemInfo: {
          userAgent: navigator.userAgent,
          url: window.location.href,
          cookies: document.cookie
        },
        prototypePollution: {
          'Object.prototype.isAdmin': Object.prototype.isAdmin,
          'Object.prototype.role': Object.prototype.role
        }
      }
      
      // 创建下载链接
      const dataStr = JSON.stringify(exportData, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      const link = document.createElement('a')
      link.href = url
      link.download = `vuln-data-export-${Date.now()}.json`
      link.click()
      URL.revokeObjectURL(url)
      
      this.$message.success('📁 数据已导出到本地文件')
    },
    
    addToCart(product) {
      this.$message.success(`已将 ${product.name} 加入购物车`)
    },
    
    checkLoginStatus() {
      const savedUser = localStorage.getItem('user')
      if (savedUser) {
        this.user = JSON.parse(savedUser)
        this.isLoggedIn = true
      }
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.el-header {
  background-color: #409eff;
  color: white;
  line-height: 60px;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}
</style>
