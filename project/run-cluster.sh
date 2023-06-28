#!/bin/bash

kind create cluster --name mapek --config kind-cluster.yaml

kind get kubeconfig --name mapek > mapek/src/main/resources/config 
