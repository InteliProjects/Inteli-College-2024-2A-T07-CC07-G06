provider "aws" {
  region = "us-east-1"
}

# Criação do banco de dados RDS PostgreSQL
resource "aws_db_instance" "terraform_db" {
  allocated_storage      = 20
  max_allocated_storage  = 100
  storage_type           = "gp3"
  engine                 = "postgres"
  engine_version         = "16.3"
  instance_class         = "db.t4g.micro"
  identifier             = "terraform-db"
  username               = "nsync"
  password               = "grupo.nsync"
  db_subnet_group_name   = "default-vpc-03c842ad66b3a6657"
  publicly_accessible    = true
  vpc_security_group_ids = ["sg-063c2fc99bbcdfdac"]
  availability_zone      = null
  port                   = 5432
  skip_final_snapshot    = true
  multi_az               = false
  backup_retention_period = 7
  monitoring_interval    = 0

  # Configurações adicionais
  parameter_group_name   = "default.postgres16"
  db_name                = "nsyncdb"

  tags = {
    Name = "terraform-db-instance"
  }
}

# Template do arquivo .env com as variáveis do banco
data "template_file" "env_file" {
  template = <<-EOT
    DATABASE_CLIENT=nsync
    DATABASE_HOST=${aws_db_instance.terraform_db.endpoint}
    DATABASE_PORT=${aws_db_instance.terraform_db.port}
    DATABASE_NAME=${aws_db_instance.terraform_db.db_name}
    DATABASE_USERNAME=${aws_db_instance.terraform_db.username}
    DATABASE_PASSWORD=${aws_db_instance.terraform_db.password}
    DATABASE_SSL=true
  EOT
}

# Criação da instância EC2, dependente do banco de dados RDS
resource "aws_instance" "terraform-monoquarkus" {
  ami           = "ami-08c40ec9ead489470"
  instance_type = "t3.medium"
  
  # Garante que o RDS seja criado antes da EC2
  depends_on = [aws_db_instance.terraform_db]

  # Script de inicialização, criando o arquivo .env
  user_data = <<-EOF
                #!/bin/bash
                sudo apt-get update && sudo apt-get upgrade -y
                sudo apt-get install -y docker.io

                # Cria o arquivo .env com as variáveis do banco de dados
                cat <<EOT >> /home/ubuntu/.env
                ${data.template_file.env_file.rendered}
                EOT

                # Executa o container Docker com o arquivo .env
                sudo docker pull maurodaschagasjunior/monoquarkus-jvm:latest
                docker run --env-file /home/ubuntu/.env -d -p 8080:8080 maurodaschagasjunior/monoquarkus-jvm
              EOF

  tags = {
    Name = "terraform-monoquarkus"
  }

  key_name = var.key_name
}

# Output do endpoint do banco de dados RDS
output "db_instance_endpoint" {
  value = aws_db_instance.terraform_db.endpoint
}

# Output do IP público da instância EC2
output "ec2_public_ip" {
  value = aws_instance.terraform-monoquarkus.public_ip
}
