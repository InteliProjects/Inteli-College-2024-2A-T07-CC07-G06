variable "rds_db" {
  type = string
  description = "O nome do banco de dados RDS."
}

variable "rds_host" {
  type = string
  description = "O endpoint do banco de dados RDS."
}

variable "rds_password" {
  type = string
  description = "A senha do banco de dados RDS."
}

variable "rds_user" {
  type = string
  description = "O usuário do banco de dados RDS."
}

variable "rds_port" {
    type = string
    description = "A porta de acesso ao RDS"
}
