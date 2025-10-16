import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios'

const app = createApp(App)
app.use(ElementPlus)

// 配置axios
axios.defaults.baseURL = '/api'
app.config.globalProperties.$http = axios

app.mount('#app')
