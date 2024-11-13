provider "aws" {
  region = "us-east-1"
}

# Cria a Lambda Layer
resource "aws_lambda_layer_version" "psycopg2_layer" {
  filename            = "layer.zip"
  layer_name          = "psycopg2-layer"
  compatible_runtimes = ["python3.11"]
  source_code_hash    = filebase64sha256("layer.zip")
}

# Usa a LabRole existente pela limitação do Learner Lab
data "aws_iam_role" "LabRole" {
  name = "LabRole"
}

# API Gateway
resource "aws_apigatewayv2_api" "http_api" {
  name          = "http-nsync"
  protocol_type = "HTTP"
}

# Stage da API
resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true
}

# Cria Lambdas
module "lambda_get_products" {
  source        = "./modules/lambda"
  function_name = "get_products"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "get_products.zip"
  layers        = [aws_lambda_layer_version.psycopg2_layer.arn]
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_get_product_by_id" {
  source        = "./modules/lambda"
  function_name = "get_product_by_id"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "get_product_by_id.zip"
  layers        = [aws_lambda_layer_version.psycopg2_layer.arn]
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_get_products_by_ids" {
  source        = "./modules/lambda"
  function_name = "get_products_by_ids"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "get_products_by_ids.zip"
  layers        = [aws_lambda_layer_version.psycopg2_layer.arn]
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_purchases" {
  source        = "./modules/lambda"
  function_name = "purchases"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "purchases.zip"
  layers        = [aws_lambda_layer_version.psycopg2_layer.arn]
  environment_variables = {
    RDS_DB       = var.rds_db
    RDS_HOST     = var.rds_host
    RDS_PASSWORD = var.rds_password
    RDS_USER     = var.rds_user
  }
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

# Criar rotas no Gateway para as Lambdas
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
