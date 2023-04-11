#!/bin/bash

mvn clean package -DskipTests
docker build -t mapek:latest .
kind load docker-image --name mapek mapek:latest

kubectl apply -f kubernetes/deploy.yaml