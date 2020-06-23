#!/bin/bash

echo "SETTING UP APPLICATION IN MINIKUBE"
echo "--"
echo "Step 1/3 - Enable the ingress controller (might take a while to activate)"
minikube addons enable ingress

echo "Step 2/3 - Deploying application"
kubectl apply -f echoservice-namespace.yml
kubectl apply -f echoservice-deployment.yml

echo "Step 3/3 - Exposing applicaiton via ingress"
kubectl expose deployment echoservice --type=NodePort --port=9090 --namespace=echoservice-ns
kubectl apply -f echoservice-ingress.yml
echo "ALL DONE!"
echo "Wait for everything to start then verify with ./verify.sh"