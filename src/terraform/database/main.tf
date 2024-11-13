# Define o provedor AWS
provider "aws" {
  region = "us-east-1" 
}

# Definindo o Security Group para a instância EC2
resource "aws_security_group" "existing_sg" {
  id = "sg-0ec0b58440e94d6be"
}

# Criação da instância EC2 com Ubuntu
resource "aws_instance" "postgresql" {
  ami           = "ami-0d5eff06f840b45e9" # AMI do Ubuntu 20.04 
  instance_type = "t3.medium"
  
  # Usando a key pair existente
  key_name      = "NSYNC-CHAVE-SUPER-SECRETA-PONTO-OFICIAL"  # Nome da key pair sem a extensão .pem

  # Associação do Security Group criado acima e sub-rede pública existente dentro da VPC
  vpc_security_group_ids = [aws_security_group.existing_sg.id]
  subnet_id              = "subnet-035b4c92546d1e04e"

  # Script de inicialização para instalar e configurar PostgreSQL
  user_data = <<-EOF
    #!/bin/bash
    # Atualiza os pacotes da instância
    sudo apt-get update -y
    sudo apt-get upgrade -y

    # Instala o PostgreSQL
    sudo apt-get install -y postgresql postgresql-contrib

    # Configura senha do usuário postgres
    sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'mypassword';"

    # Configurações para permitir conexões externas
    echo "host    all             all             0.0.0.0/0            md5" | sudo tee -a /etc/postgresql/*/main/pg_hba.conf
    sudo sed -i "s/#listen_addresses = 'localhost'/listen_addresses = '*'/g" /etc/postgresql/*/main/postgresql.conf

    # Reinicia o PostgreSQL para aplicar as mudanças
    sudo systemctl restart postgresql

    # Criação do banco de dados e usuário conforme solicitado
    sudo -u postgres psql <<EOSQL
      CREATE DATABASE nsyncdb;
      CREATE USER nsync WITH PASSWORD 'grupo.nsync';
      GRANT ALL PRIVILEGES ON DATABASE nsyncdb TO nsync;
      ALTER USER nsync WITH SUPERUSER;
      GRANT CREATE ON DATABASE nsyncdb TO nsync;
      GRANT CREATE ON SCHEMA public TO nsync;
      GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO nsync;
      GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO nsync;
      \q
    EOSQL
  EOF

  # Tags para a instância
  tags = {
    Name = "🎲 EC2-With-PostgreSQL-Terraform"
  }
}

# Criação de um novo Elastic IP
resource "aws_eip" "new_postgresql_eip" {
  vpc = true  # Define que o Elastic IP será para uma VPC

  # Tags para o Elastic IP
  tags = {
    Name = "PostgreSQL-Ec2"
  }
}

# Associação do novo Elastic IP à instância EC2
resource "aws_eip_association" "postgresql_eip_assoc" {
  instance_id   = aws_instance.postgresql.id
  allocation_id = aws_eip.new_postgresql_eip.id
}

# Saída do novo Elastic IP público da instância EC2
output "new_ec2_public_ip" {
  value = aws_eip.new_postgresql_eip.public_ip
}
