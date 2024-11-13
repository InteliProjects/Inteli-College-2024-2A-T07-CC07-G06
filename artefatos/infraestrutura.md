<div align="justify">

# Infraestrutura

Este documento descreve a infraestrutura criada e apresentada no vídeo disponível no seguinte link:

[Link para o vídeo de apresentação](https://youtu.be/D7060T8jeVQ?si=a-vLPZFxYtizylPI)

## Introdução

Neste vídeo, demonstramos a implementação de uma infraestrutura robusta na AWS para o projeto de Sistema de Inventário Distribuído, contendo diversos componentes essenciais para garantir alta disponibilidade, escalabilidade e segurança. Abaixo estão os detalhes de cada módulo configurado e mostrado no console da AWS.

## 1. Estrutura de Redes com Subrede Pública e Privada

Foi configurada uma VPC (Virtual Private Cloud) principal, com subredes públicas e privadas distribuídas em duas zonas de disponibilidade (Availability Zones). Isso garante alta disponibilidade e isolação entre os serviços públicos e privados. 

- As **subredes privadas** hospedam serviços internos, como o banco de dados, que não precisam de exposição direta à internet.
- As **subredes públicas** permitem a exposição de serviços como o NAT Gateway e instâncias EC2.

**Visualização no vídeo:** VPC e subredes mostradas no console da AWS.

## 2. Acesso à Internet via NAT Gateway

O NAT Gateway foi configurado para permitir que as instâncias nas subredes privadas acessem a internet de forma segura, sem exposição direta.

- Temos como exemplo, os NAT Gateways nomeados como `NSync1-nat-public1-us-east-1a` e `NSync1-vpce-s3`, os quais estão associados à sub-rede privada, possibilitando o acesso à internet para essas instâncias.

**Visualização no vídeo:** NAT Gateway e sua associação com a subrede privada mostrados no console.

## 3. Auto Scaling Group

O Auto Scaling Group foi configurado para ajustar automaticamente o número de instâncias EC2 de acordo com a demanda. Ele garante que a aplicação mantenha alta performance, escalando os recursos conforme necessário.

**Visualização no vídeo:** Configuração do Auto Scaling Group no console da AWS.

## 4. Application Load Balancer (ELB)

O Application Load Balancer distribui o tráfego entre as instâncias EC2, garantindo uma melhor performance e disponibilidade para a aplicação.

**Visualização no vídeo:** Application Load Balancer configurado e operando no console da AWS.

## 5. Servidor Virtual (VPS) utilizando AWS EC2

As instâncias EC2 funcionam como servidores virtuais privados, gerenciando as requisições e sendo parte do Auto Scaling Group. Essas instâncias recebem o tráfego balanceado pelo Load Balancer.

**Visualização no vídeo:** Instâncias EC2 sendo mostradas no console.

## 6. Banco de Dados Gerenciado utilizando AWS RDS

O banco de dados relacional foi configurado utilizando o serviço gerenciado Amazon RDS. Ele é replicado entre as subredes privadas para garantir alta disponibilidade e redundância dos dados.

**Visualização no vídeo:** Banco de dados RDS e suas configurações mostradas no console da AWS.

## 7. Bastion Host

O Bastion Host foi configurado para permitir acesso seguro às instâncias EC2 na subrede privada, garantindo que apenas usuários autorizados possam se conectar a esses servidores.

**Visualização no vídeo:** Bastion Host e suas configurações mostradas no console.

## 8. Gerenciamento do Ambiente Virtual de Rede utilizando AWS VPC

Toda a infraestrutura foi gerenciada utilizando a VPC (Virtual Private Cloud), que permite o gerenciamento das redes, subredes e a comunicação entre os serviços.

**Visualização no vídeo:** Gerenciamento da VPC mostrado no console da AWS.

## Conclusão

Nesta apresentação, mostramos como configuramos uma infraestrutura na AWS com alta disponibilidade, segurança e escalabilidade. Os módulos incluem redes públicas e privadas, balanceamento de carga, auto-escalabilidade, banco de dados gerenciado e acesso seguro via Bastion Host.


</div>
