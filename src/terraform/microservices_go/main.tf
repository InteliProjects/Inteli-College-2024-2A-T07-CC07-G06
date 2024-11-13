provider "aws" {
  region = "us-east-1"
}

# generates a private key with RSA algorithm and 4096-bit size.
resource "tls_private_key" "terraform-key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

# creates an AWS key pair using the generated public key.
resource "aws_key_pair" "deployer_key" {
  key_name   = "deployer_key"
  public_key = tls_private_key.terraform-key.public_key_openssh
}

# creates a security group allowing SSH (port 22) inbound traffic.
resource "aws_security_group" "allow_ssh" {
  name        = "allow_shh"
  description = "Allow SHH inbound traffic"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# creates an EC2 instance with the specified AMI and instance type.
resource "aws_instance" "terraform-ec2-for-micro-go" {
  ami             = "ami-08c40ec9ead489470"
  instance_type   = "t3.medium"
  key_name        = aws_key_pair.deployer_key.key_name
  security_groups = [aws_security_group.allow_ssh.name]

  # user data script to set up Docker, unzip, and MicroK8s on the instance.
  user_data = <<-EOF
                #!/bin/bash
                sudo apt-get update && sudo apt-get upgrade -y
                sudo apt-get install -y docker.io
                sudo apt-get install -y unzip
                sudo snap install microk8s --classic --channel=1.31
                sudo usermod -aG microk8s ubuntu
                mkdir -p /home/ubuntu/deployments
                EOF

  # wait for the instance to become accessible
  provisioner "remote-exec" {
    inline = [
      "echo 'Waiting for instance to start...'"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  # uploads the 'deployments.zip' file to the EC2 instance.
  provisioner "file" {
    source      = "deployments.zip"
    destination = "/home/ubuntu/deployments.zip"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  # uploads the 'hpa.zip' file to the EC2 instance.
  provisioner "file" {
    source      = "hpa.zip"
    destination = "/home/ubuntu/hpa.zip"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  # uploads the 'deploy_services.sh' file to the EC2 instance.
  provisioner "file" {
    source      = "deploy_services.sh"
    destination = "/home/ubuntu/deployments/deploy_services.sh"

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  # unzips both 'deployments.zip' and 'hpa.zip' files on the EC2 instance.
  provisioner "remote-exec" {
    inline = [
      "sudo unzip /home/ubuntu/deployments.zip -d /home/ubuntu/deployments/",
      "sudo unzip /home/ubuntu/hpa.zip -d /home/ubuntu/deployments/"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  # make the 'deploy_services.sh' executable and run it
  provisioner "remote-exec" {
    inline = [
      "chmod +x /home/ubuntu/deploy_services.sh",
      "sudo /home/ubuntu/deploy_services.sh"
    ]

    connection {
      type        = "ssh"
      user        = "ubuntu"
      private_key = tls_private_key.terraform-key.private_key_pem
      host        = self.public_ip
    }
  }

  tags = {
    Name = "EC2ForGozinhos"
  }
}

# outputs the public IP address of the EC2 instance.
output "instance_ip" {
  value = aws_instance.terraform-ec2-for-micro-go.public_ip
}

# outputs the private key in PEM format (marked as sensitive).
output "private_key" {
  value     = tls_private_key.terraform-key.private_key_pem
  sensitive = true
}
