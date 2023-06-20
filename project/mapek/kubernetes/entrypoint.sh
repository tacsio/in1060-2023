#!/bin/sh

APISERVER=https://kubernetes.default.svc
SERVICEACCOUNT=/var/run/secrets/kubernetes.io/serviceaccount
TOKEN=$(cat ${SERVICEACCOUNT}/token)
CACERT=${SERVICEACCOUNT}/ca.crt

mkdir /root/.kube
curl --cacert ${CACERT} --header "Authorization: Bearer ${TOKEN}" -X GET ${APISERVER}/api  | grep 6443 | awk '{print "server: https://" $2}' | sed 's/\"//g'  | sed 's/\//\\\//g' | sed 's/"//g' | xargs -I {} echo "sed '5s/.*/    {}/' config > /root/.kube/config" | sh

java -jar /app/app.jar