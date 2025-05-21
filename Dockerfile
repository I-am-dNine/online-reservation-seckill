# 官方 Java 17 
FROM eclipse-temurin:17-jdk-jammy

# 设置容器中的工作目录
WORKDIR /app

# 打包好的 jar 复制进容器内
COPY target/seckill-0.0.1-SNAPSHOT.jar app.jar

# 开放端口（Spring Boot 默认是 8080）
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
