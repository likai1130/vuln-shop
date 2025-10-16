# 多阶段构建
FROM maven:3.8.6-openjdk-8-slim AS build

WORKDIR /app

# 复制pom.xml和源代码
COPY pom.xml .
COPY src ./src

# 编译和打包
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:8-jre-alpine

WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=build /app/target/vuln-backend-1.0.0.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
