# 构建阶段
FROM node:18-alpine as build-stage

WORKDIR /app

# 复制package.json和package-lock.json
COPY package*.json ./

# 安装依赖 - 确保安装lodash 4.17.15 (存在CVE-2019-10744漏洞)
RUN npm install

# 复制源代码
COPY . .

# 构建应用
RUN npm run build

# 生产阶段
FROM nginx:alpine as production-stage

# 复制构建产物
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 复制nginx配置模板
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

# 暴露端口
EXPOSE 80