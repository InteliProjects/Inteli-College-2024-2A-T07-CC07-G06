variable "instance_name" {
  description = "Value of the Name tag for the EC2 instance"
  type        = string
  default     = "KubernetesServerInstance"
}

variable "ssh_key_path" {
  description = "Key to connect with the aws_instance"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/terraform-kubernetes/kube_inst.pem"
}

# deployments

variable "product-deployment" {
  description = "File of deployment of microservice: product"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/deployments/products-service.yaml"
}

variable "cep-deployment" {
  description = "File of deployment of microservice: cep-estimator"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/deployments/cep-estimator.yaml"
}

variable "csv-deployment" {
  description = "File of deployment of microservice: upload-csv"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/deployments/upload-csv-service.yaml"
}

variable "purchase-deployment" {
  description = "File of deployment of microservice: purchase"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/deployments/purchase-service.yaml"
}

variable "nginx-deployment" {
  description = "File of deployment of microservice: nginx"
  type        = string
  default     = "C:/Users/Inteli/OneDrive/Documentos/deployments/nginx-deployment.yaml"
}