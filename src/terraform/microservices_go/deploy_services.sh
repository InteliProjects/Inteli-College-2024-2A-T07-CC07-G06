# apply Kubernetes deployments
sudo microk8s kubectl apply -f /home/ubuntu/deployments/products-service-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/distributors-service-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/products-distributors-service-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/delivery-service-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/purchase-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/sales-orders-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/sales-orders-products-deployment.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/csv-processor-deployment.yaml

# apply NGINX deployment
sudo microk8s kubectl apply -f /home/ubuntu/deployments/nginx-deployment.yaml

# apply Horizontal Pod Autoscaler (HPA) configurations
sudo microk8s kubectl apply -f /home/ubuntu/deployments/csv-processor-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/delivery-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/distributors-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/products-distributors-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/products-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/purchase-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/sales-orders-products-service-hpa.yaml
sudo microk8s kubectl apply -f /home/ubuntu/deployments/sales-orders-service-hpa.yaml
