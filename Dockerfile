# 1. 使用官方 Maven 镜像完成打包
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. 使用 WildFly 运行环境
FROM quay.io/wildfly/wildfly:latest
# 将打包好的 war 包复制到 WildFly 的部署目录
COPY --from=build /app/target/*.war /opt/jboss/wildfly/standalone/deployments/
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]