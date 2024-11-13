terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}


# Lab Role do Learner Lab
data "aws_iam_role" "LabRole" {
  name = "LabRole"
}

# API Gateway
resource "aws_apigatewayv2_api" "http_api" {
  name          = "http-go-nsync-api"
  protocol_type = "HTTP"
}

# Stage da API
resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true
}

# Criando as Lambdas

module "lambda_cep_calculate_days" {
  source        = "./modules/lambda"
  function_name = "cep_calculate_days"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "${path.module}/lambda_go_codes/CepCalculateDays/lambda-handler.zip"
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
    RDS_PORT     = var.rds_port
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_get_products" {
  source        = "./modules/lambda"
  function_name = "get_products"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "${path.module}/lambda_go_codes/GetProducts/lambda-handler.zip"
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
    RDS_PORT     = var.rds_port
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_get_product_by_id" {
  source        = "./modules/lambda"
  function_name = "get_product_by_id"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "${path.module}/lambda_go_codes/GetProductById/lambda-handler.zip"
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
    RDS_PORT     = var.rds_port
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_get_products_by_ids" {
  source        = "./modules/lambda"
  function_name = "get_products_by_ids"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "${path.module}/lambda_go_codes/GetProductsByIds/lambda-handler.zip"
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
    RDS_PORT     = var.rds_port
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_purchases" {
  source        = "./modules/lambda"
  function_name = "purchases"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "${path.module}/lambda_go_codes/Purchases/lambda-handler.zip"
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
    RDS_PORT     = var.rds_port
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

# Criando as rotas na API Gateway para as Lambdas

module "api_gateway_cep_calculate_days" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "POST"
  path              = "/cep/calculate_days"
  lambda_invoke_arn = module.lambda_cep_calculate_days.lambda_invoke_arn
}

module "api_gateway_get_products" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "GET"
  path              = "/products"
  lambda_invoke_arn = module.lambda_get_products.lambda_invoke_arn
}

module "api_gateway_get_product_by_id" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "GET"
  path              = "/products/{id}"
  lambda_invoke_arn = module.lambda_get_product_by_id.lambda_invoke_arn
}

module "api_gateway_get_products_by_ids" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "POST"
  path              = "/products/ids"
  lambda_invoke_arn = module.lambda_get_products_by_ids.lambda_invoke_arn
}

module "api_gateway_purchases" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "POST"
  path              = "/purchases"
  lambda_invoke_arn = module.lambda_purchases.lambda_invoke_arn
}