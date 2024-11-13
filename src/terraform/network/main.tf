provider "aws" {
  region = "us-east-1"
}

# vpc
resource "aws_vpc" "nsync-vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "nsync-vpc"
  }
}

# internet gateway
resource "aws_internet_gateway" "nsync-igw" {
  vpc_id = aws_vpc.nsync-vpc.id

  tags = {
    Name = "nsync-igw"
  }
}

# route table for public subnets
resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.nsync-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.nsync-igw.id
  }

  tags = {
    Name = "public_route_table"
  }
}

# public subnet 1 | us-east-1a
resource "aws_subnet" "nsync-subnet-public1" {
  vpc_id            = aws_vpc.nsync-vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "nsync-subnet-public1"
  }
}

# public subnet 2 | us-east-1b
resource "aws_subnet" "nsync-subnet-public2" {
  vpc_id            = aws_vpc.nsync-vpc.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1b"

  tags = {
    Name = "nsync-subnet-public2"
  }
}

# association of public route table with public subnets
resource "aws_route_table_association" "public_association1" {
  subnet_id      = aws_subnet.nsync-subnet-public1.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_association2" {
  subnet_id      = aws_subnet.nsync-subnet-public2.id
  route_table_id = aws_route_table.public_route_table.id
}

# private subnet 1 | us-east-1a
resource "aws_subnet" "nsync-subnet-private1" {
  vpc_id            = aws_vpc.nsync-vpc.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "nsync-subnet-private1"
  }
}

# private subnet 2 | us-east-1b
resource "aws_subnet" "nsync-subnet-private2" {
  vpc_id            = aws_vpc.nsync-vpc.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "us-east-1b"

  tags = {
    Name = "nsync-subnet-private2"
  }
}

# nat gateway
resource "aws_eip" "nat_eip" {
  domain = "vpc"
}

resource "aws_nat_gateway" "nat_gateway" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.nsync-subnet-public1.id

  tags = {
    Name = "nat_gateway"
  }
}

# route table for private subnets
resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.nsync-vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat_gateway.id
  }

  tags = {
    Name = "private_route_table"
  }
}

# private route table association with private subnets
resource "aws_route_table_association" "private_association1" {
  subnet_id      = aws_subnet.nsync-subnet-private1.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "private_association2" {
  subnet_id      = aws_subnet.nsync-subnet-private2.id
  route_table_id = aws_route_table.private_route_table.id
}

# ssh security group
resource "aws_security_group" "allow_ssh" {
  vpc_id = aws_vpc.nsync-vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_ssh"
  }
}

# http security group
resource "aws_security_group" "allow_http" {
  vpc_id = aws_vpc.nsync-vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "allow_http"
  }
}
