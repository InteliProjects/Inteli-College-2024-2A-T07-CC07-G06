terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.16"
    }
  }

  required_version = ">= 1.2.0"
}

provider "aws" {
  profile = "default"
}

resource "aws_instance" "app_server" {
  ami           = "ami-0e86e20dae9224db8"
  instance_type = "m4.large"

  key_name = "kube_inst"

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              snap install microk8s --classic
              microk8s status --wait-ready
              microk8s enable dns storage
              microk8s kubectl get nodes
              # Alias kubectl for ease of use
              snap alias microk8s.kubectl kubectl
              EOF

  # looks at the variables.tf
  tags = {
    Name = var.instance_name
  }

  # Transfer the deployment files to EC2
  provisioner "file" {
    source      = var.product-deployment  # Local path
    destination = "/home/ubuntu/products-service.yaml"  # EC2 path

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  provisioner "file" {
    source      = var.cep-deployment  # Local path
    destination = "/home/ubuntu/cep-estimator.yaml"  # EC2 path

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  provisioner "file" {
    source      = var.csv-deployment  # Local path
    destination = "/home/ubuntu/upload-csv-service.yaml"  # EC2 path

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  provisioner "file" {
    source      = var.purchase-deployment  # Local path
    destination = "/home/ubuntu/purchase-service.yaml"  # EC2 path

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  provisioner "file" {
    source      = var.nginx-deployment  # Local path
    destination = "/home/ubuntu/nginx-deployment.yaml"  # EC2 path

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  provisioner "remote-exec" {
    inline = [
      # Update and install necessary dependencies
      "sudo apt-get update -y",
      "sudo apt-get upgrade -y",
      
      # Install MicroK8s
      "sudo snap install microk8s --classic",
      
      # Add the current user (ubuntu) to the microk8s group
      "sudo usermod -aG microk8s ubuntu",
      
      # Allow some time for changes to propagate
      "sudo chown -f -R ubuntu ~/.kube",
      
      # Start MicroK8s and enable DNS, storage
      "sudo microk8s start",
      "sudo microk8s status --wait-ready",
      "sudo microk8s enable dns storage",
      
      # Wait for services to be ready
      "sleep 10",
      
      # Ensure MicroK8s is running
      "sudo microk8s.status",

      # Wait for MicroK8s to be ready
      "sudo microk8s.status --wait-ready",

      # Apply product deployment
      "sudo microk8s.kubectl apply -f /home/ubuntu/products-service.yaml",

      # Apply cep deployment
      "sudo microk8s.kubectl apply -f /home/ubuntu/cep-estimator.yaml",

      # Apply csv deployment
      "sudo microk8s.kubectl apply -f /home/ubuntu/upload-csv-service.yaml",

      # Apply purchase deployment
      "sudo microk8s.kubectl apply -f /home/ubuntu/purchase-service.yaml",

      # Apply the NGINX deployment
      "sudo microk8s.kubectl apply -f /home/ubuntu/nginx-deployment.yaml",  
    ]

    # Ensure we can SSH into the instance
    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = file(var.ssh_key_path) # Path to your private key
      host        = self.public_ip
    }
  }

  # Security group to allow HTTP, HTTPS, and SSH traffic
  vpc_security_group_ids = [aws_security_group.microk8s_sg.id]

  
}

resource "aws_security_group" "microk8s_sg" {
  name        = "microk8s-sg"
  description = "Allow SSH and HTTP/HTTPS access"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 30005
    to_port     = 30005
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
