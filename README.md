# 漏洞电商平台

一个用于安全测试的漏洞电商平台，包含前端原型污染漏洞和后端Actuator未授权访问漏洞。

## 漏洞说明

### 1. 前端原型污染漏洞 (CVE-2019-10744)
- **位置**: Vue.js前端使用lodash.merge处理用户输入
- **影响**: 可通过原型污染绕过权限检查
- **利用**: 在搜索框中输入包含`__proto__`的JSON数据

### 2. 后端Actuator未授权访问
- **位置**: Spring Boot Actuator端点
- **影响**: 可访问敏感系统信息、环境变量、配置等
- **利用**: 直接访问`/actuator/*`端点

### 3. 环境变量泄露
- **位置**: K8s部署中的环境变量
- **影响**: 数据库密码、Redis密码、MinIO密钥等敏感信息泄露
- **利用**: 通过Actuator的`/actuator/env`端点查看环境变量

### 4. MinIO对象存储未授权访问
- **位置**: MinIO对象存储服务
- **影响**: 可访问存储桶中的文件，上传恶意文件
- **利用**: 使用泄露的MinIO密钥直接访问对象存储

## 快速部署

### 方式1：本地Docker测试（推荐）

#### 简化版（只运行必要服务）
```bash
cd vuln-shop
./simple-test.sh
```

#### 完整版（包含MySQL、Redis、MinIO）
```bash
cd vuln-shop
./local-test.sh
```

### 方式2：Kubernetes部署

#### 1. 构建镜像
```bash
# 构建后端镜像
cd vuln-backend
mvn clean package
docker build -t vuln-backend:1.0.0 .

# 构建前端镜像
cd ../vuln-frontend
npm install
npm run build
docker build -t vuln-frontend:1.0.0 .
```

#### 2. 部署到Kubernetes
```bash
# 创建命名空间
kubectl apply -f k8s-namespace.yaml

# 部署后端
kubectl apply -f vuln-backend/k8s-deployment.yaml

# 部署前端
kubectl apply -f vuln-frontend/k8s-deployment.yaml
```

#### 3. 访问应用
```bash
# 获取Ingress地址
kubectl get ingress -n vuln-ecommerce

# 访问前端
curl http://<INGRESS-IP>/

# 访问Actuator端点
curl http://<INGRESS-IP>/actuator/health
curl http://<INGRESS-IP>/actuator/env
```

## 漏洞测试

### 测试原型污染
1. 打开前端页面
2. 在搜索框输入: `{"keyword":"test","__proto__":{"isAdmin":true}}`
3. 观察权限状态变化

### 测试Actuator未授权访问
1. 直接访问: `http://<INGRESS-IP>/actuator/env`
2. 查看敏感环境变量（数据库密码、Redis密码等）
3. 访问其他端点: `/actuator/beans`, `/actuator/configprops`等

### 测试环境变量泄露
1. 访问: `http://<INGRESS-IP>/actuator/env`
2. 搜索关键词: `SPRING_DATASOURCE_PASSWORD`, `SPRING_REDIS_PASSWORD`, `MINIO_SECRET_KEY`
3. 查看数据库、Redis和MinIO的敏感配置信息

### 测试MinIO未授权访问
1. 从环境变量中获取MinIO密钥
2. 使用MinIO客户端连接: `mc alias set vuln http://192.168.30.11:9000 minioadmin minioadmin`
3. 列出存储桶: `mc ls vuln/`
4. 上传/下载文件: `mc cp file.txt vuln/vulnshop/`

## 文件结构
```
vuln-shop/
├── vuln-frontend/          # Vue.js前端
│   ├── src/
│   │   ├── App.vue        # 主组件，包含原型污染漏洞
│   │   └── main.js        # 入口文件
│   ├── package.json       # 依赖配置
│   ├── Dockerfile         # 前端镜像
│   └── k8s-deployment.yaml # 前端K8s配置
├── vuln-backend/           # Spring Boot后端
│   ├── src/main/java/com/vuln/
│   │   ├── controller/    # API控制器
│   │   ├── entity/        # 数据实体
│   │   └── config/        # 配置类
│   ├── pom.xml            # Maven配置
│   ├── Dockerfile         # 后端镜像
│   └── k8s-deployment.yaml # 后端K8s配置
└── k8s-namespace.yaml     # K8s命名空间
```
