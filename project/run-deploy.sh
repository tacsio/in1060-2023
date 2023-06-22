#!/bin/bash

# DATABASE
kubectl apply -f infra.yaml

# API
cd api

mvn clean package -DskipTests
docker build -t api:latest .
kind load docker-image --name mapek api:latest

kubectl apply -f kubernetes/deploy.yaml

cd -

# MAPE-K
#cd mapek

#mvn clean package -DskipTests
#docker build -t mapek:latest .
#kind load docker-image --name mapek mapek:latest
#kubectl apply -f kubernetes/deploy.yaml

#cd -
