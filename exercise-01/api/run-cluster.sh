#!/bin/bash

kind create cluster --name mapek --config kubernetes/kind/cluster.yaml
kind get kubeconfig --name mapek > src/main/resources/config
