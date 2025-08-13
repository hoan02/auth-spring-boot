#!/bin/bash

# Cài đặt MicroK8s
sudo snap install microk8s --classic

# Thêm user vào group microk8s
sudo usermod -a -G microk8s $USER
newgrp microk8s

# Enable các addon cần thiết
microk8s enable dns
microk8s enable registry
microk8s enable ingress
microk8s enable storage

# Chờ cho các addon khởi động
microk8s status --wait-ready

# Cấu hình kubectl alias
echo "alias kubectl='microk8s kubectl'" >> ~/.bashrc
source ~/.bashrc

# Tạo namespace và secret cho registry
microk8s kubectl create namespace auth-service
microk8s kubectl create secret docker-registry registry-secret \
  --docker-server=registry.container-registry.svc.cluster.local:5000 \
  --docker-username=admin \
  --docker-password=admin \
  --docker-email=admin@example.com \
  -n auth-service

echo "MicroK8s setup completed!"
