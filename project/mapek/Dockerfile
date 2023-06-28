FROM eclipse-temurin:17-jdk-alpine

RUN apk add --update curl
#RUN addgroup springgroup; adduser --ingroup springgroup --disabled-password spring
#USER spring

WORKDIR /app

RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
RUN chmod +x kubectl

COPY kubernetes/entrypoint.sh .
COPY src/main/resources/config .

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["/app/entrypoint.sh"]
