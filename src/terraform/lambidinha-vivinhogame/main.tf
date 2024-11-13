provider "aws" {
  region = "us-east-1"
}

# Usa a LabRole existente pela limitação do Learner Lab
data "aws_iam_role" "LabRole" {
  name = "LabRole"
}

# API Gateway
resource "aws_apigatewayv2_api" "http_api" {
  name          = "http-nsync-vivinho"
  protocol_type = "HTTP"
}

# Stage da API
resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true
}

# Cria Lambdas
module "lambda_get_ec2_live" {
  source        = "./modules/lambda"
  function_name = "get_ec2_live"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "./deployments/get_ec2_live.zip"
  layers        = []
  environment_variables = {}
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

module "lambda_stress_ec2" {
  source        = "./modules/lambda"
  function_name = "stress_ec2"
  role_arn      = data.aws_iam_role.LabRole.arn
  zip_file      = "./deployments/stress_ec2.zip"
  # Usa Klayers para requests, evitando a necessidade de instalar novamente https://github.com/keithrozario/Klayers
  layers        = ["arn:aws:lambda:us-east-1:770693421928:layer:Klayers-p311-requests:12"]
  environment_variables = {}
  api_gw_execution_arn = aws_apigatewayv2_api.http_api.execution_arn
}

# Criar rotas no Gateway para as Lambdas
module "api_gateway_get_ec2_live" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "GET"
  path              = "/getec2live"
  lambda_invoke_arn = module.lambda_get_ec2_live.lambda_invoke_arn
}

module "api_gateway_stress_ec2" {
  source            = "./modules/api_gateway"
  api_id            = aws_apigatewayv2_api.http_api.id
  method            = "GET"
  path              = "/stressec2"
  lambda_invoke_arn = module.lambda_stress_ec2.lambda_invoke_arn
}

