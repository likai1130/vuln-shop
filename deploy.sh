#!/bin/bash

echo "🚀 开始部署漏洞电商平台到Kubernetes..."

# 创建命名空间
echo "📝 创建命名空间..."
kubectl apply -f k8s-namespace.yaml

# 部署后端
echo "📦 部署后端服务..."
kubectl apply -f vuln-backend/k8s-deployment.yaml

# 等待后端启动
echo "⏳ 等待后端服务启动..."
kubectl wait --for=condition=available --timeout=300s deployment/vuln-backend -n vuln-ecommerce

# 部署前端
echo "📦 部署前端服务..."
kubectl apply -f vuln-frontend/k8s-deployment.yaml

# 等待前端启动
echo "⏳ 等待前端服务启动..."
kubectl wait --for=condition=available --timeout=300s deployment/vuln-frontend -n vuln-ecommerce

echo "✅ 部署完成！"
echo ""
echo "📋 服务状态："
kubectl get pods -n vuln-ecommerce
echo ""
echo "🌐 Ingress状态："
kubectl get ingress -n vuln-ecommerce
echo ""
echo "💡 获取访问地址："
echo "kubectl get ingress -n vuln-ecommerce -o jsonpath='{.items[0].status.loadBalancer.ingress[0].ip}'"
