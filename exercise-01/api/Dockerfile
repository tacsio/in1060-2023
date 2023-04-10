FROM eclipse-temurin:17-jdk-alpine

RUN addgroup springgroup; adduser --ingroup springgroup --disabled-password spring
USER spring

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
