# Integração da Aplicação: Frontend e Microsserviços

## Introdução

&emsp;Este documento descreve o processo de integração entre o _frontend_ da aplicação e os microsserviços _backend_, desenvolvidos em Go, que estão hospedados em uma instância EC2 na AWS. O _frontend_ está armazenado em um bucket S3, e o objetivo desta integração é validar a comunicação entre o _frontend_ e os microsserviços, garantindo que as alterações realizadas estejam funcionando corretamente.

## Frontend

- **Armazenamento:** Bucket S3 na AWS, com arquivos HTML, CSS, JavaScript e a pasta `_next` contendo a aplicação.
- **Configuração:** A URL do backend é configurada no arquivo `hooks/axios.tsx`, onde definimos o endereço do microsserviço no EC2, acrescentando `http://` e a porta de comunicação necessária (por exemplo, `:30000`).

## Backend

- **Plataforma:** Os microsserviços são desenvolvidos em Go e estão hospedados em uma instância EC2, divididos em vários serviços, como processamento de arquivos CSV, distribuidores, produtos, e entrega.

## Processo de Integração

### Passos Realizados

1. **Configuração Inicial**
   - O _frontend_ foi configurado para se comunicar com o backend, ajustando a URL no arquivo `hooks/axios.tsx` para apontar para os microsserviços Go no EC2.
   - O _frontend_ foi implantado em um bucket S3, acessível via uma URL pública.

2. **Testes de Integração**
   - Foram realizados testes de integração para garantir a comunicação correta entre o _frontend_ e os microsserviços, utilizando várias rotas.

## Rotas Testadas

### 1. `POST /csv-processor/populate-all`
   - **Descrição:** Rota responsável por fazer o upload de arquivos CSV e popular o banco de dados com novos produtos.
   - **Entrada:** Arquivo CSV contendo informações de produtos, distribuidores, etc.
   - **Saída:** Confirmação do processamento e inclusão dos dados no banco.

### 2. `POST /purchases`
   - **Descrição:** Rota para processar compras de produtos.
   - **Entrada:** Um JSON contendo os IDs dos produtos, a quantidade desejada e o CEP do cliente.
   - **Saída:** Detalhes da compra realizada, como confirmação, valores, e prazos de entrega.

### 3. `POST /cep/calculate-days`
   - **Descrição:** Calcula o prazo estimado de entrega com base no CEP do cliente.
   - **Entrada:** Um JSON com o CEP do cliente.
   - **Saída:** O tempo estimado em dias para entrega.

## Resultados dos Testes

Os testes de integração realizados com as rotas mencionadas confirmam que a comunicação entre o _frontend_ e os microsserviços está funcionando corretamente. Cada rota foi testada com entradas válidas para garantir que os processos de upload de CSV, realização de compras e cálculo de prazo de entrega estão operando conforme o esperado.

## Vídeo de Demonstração

Um vídeo foi gravado para demonstrar o processo de validação da integração. O vídeo inclui:

- Visão geral dos microsserviços e sua configuração no console da AWS.
- Testes de integração com os microsserviços Go.
- A alteração da URL no _frontend_ para testar a comunicação com o backend.

### [Assistir ao Vídeo de Testes de Integração](https://youtu.be/BvoYwjOHIdk?si=JGWMSTOUxmxNtDb0)

## Conclusão

&emsp;A integração entre o _frontend_ e os microsserviços _backend_ foi validada com sucesso, demonstrando a eficácia da solução implementada. Esta integração garante uma comunicação fluida entre as interfaces de usuário e a lógica de negócios, atendendo às necessidades da aplicação e proporcionando uma experiência consistente ao usuário.