FROM eclipse-temurin:17-jdk-alpine

RUN apk add --update curl
#RUN addgroup springgroup; adduser --ingroup springgroup --disabled-password spring
#USER spring

WORKDIR /app


ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-jar", "/app/app.jar"]
