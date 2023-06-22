FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-jar", "/app/app.jar"]
