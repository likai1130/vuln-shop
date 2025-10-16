#!/bin/bash

echo "🚀 开始构建漏洞电商平台..."

# 构建后端
echo "📦 构建后端镜像..."
cd vuln-backend
docker build -t vuln-backend:1.0.1 .
cd ..

# 构建前端
echo "📦 构建前端镜像..."
cd vuln-frontend
npm install
npm run build
docker build -t vuln-frontend:1.0.1 .
cd ..

echo "✅ 构建完成！"
echo ""
echo "📋 可用镜像："
docker images | grep vuln
