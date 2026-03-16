FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY . .

RUN mvn dependency:go-offline  -B -e -X

RUN mvn clean package -DskipTests -X

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/task_manager_demo-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
