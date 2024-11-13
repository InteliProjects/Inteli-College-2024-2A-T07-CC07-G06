<div align=justify>

# 1. Requisitos

&emsp;Os requisitos de um sistema são divididos em funcionais e não funcionais. Os requisitos funcionais referem-se às funcionalidades que o usuário consegue perceber ao utilizar o sistema, como as operações e o comportamento esperado. Já os requisitos não funcionais dizem respeito às especificidades técnicas do sistema, relacionadas a aspectos de qualidade, como desempenho, usabilidade, confiabilidade, segurança e disponibilidade.


## 1.1. Requisitos Funcionais

| Identificador  | RF 1 
|----------|----------|
| Nome | Visualização de Produtos   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve apresentar todos os produtos disponíveis quando o usuário acessar a página principal   |  

| Identificador  | RF 2 
|----------|----------|
| Nome | Indicação de Produtos Indisponíveis   |
| Prioridade   | Baixa  |
| Descrição   | Quando o usuário buscar um produto, o sistema deve sinalizar claramente quando ele não está disponível    | 

| Identificador  | RF 3 
|----------|----------|
| Nome | Exibição de Informações do Produto   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve permitir que o usuário visualize as informações do produto: nome, preço, descrição, SKU (*stock keeping unit*, ou unidade de manutenção de estoque, é o identificador único do produto) e imagem   | 

| Identificador  | RF 4 
|----------|----------|
| Nome | Consultar Informações Internas do Produto   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de consultar as informações do produto: nome, preço, descrição, sku e imagem   | 

| Identificador  | RF 5 
|----------|----------|
| Nome | Cálculo da Loja ou Centro de Distribuição Mais Próximo   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de identificar e retornar os três centros de distribuição mais próximos de um CEP fornecido, desde que possuam o produto em estoque   | 

| Identificador  | RF 7 
|----------|----------|
| Nome | Consulta de Estoque de produto   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de consultar o estoque de um determinado produto   |

| Identificador  | RF 8 
|----------|----------|
| Nome | Consulta de Estoque de loja ou centro de distribuição   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de consultar os produtos e seus respectivos estoques de uma determinada loja   |

| Identificador  | RF 9 
|----------|----------|
| Nome | Estimativa de Prazo de Entrega   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve calcular um prazo estimado de entrega com base na localização do usuário e na disponibilidade do produto   |

| Identificador  | RF 10 
|----------|----------|
| Nome | Função de Carrinho de Compras   |
| Prioridade   | Média  |
| Descrição   | O sistema deve contemplar a função de carrinho de compras, permitindo ao usuário adicionar, remover e atualizar a quantidade de produtos, e calcular o frete.  |

| Identificador  | RF 11 
|----------|----------|
| Nome | Função de Pagamento   |
| Prioridade   | Baixa  |
| Descrição   | O sistema contempla a função de simulação de pagamento ao confirmar seu carrinho de compras  |
|Observação|A simulação de pagamento será uma página. Ela reserva o produto no banco de dados|

| Identificador  | RF 12 
|----------|----------|
| Nome | Relatórios de Performance do Sistema   |
| Prioridade   | Média  |
| Descrição   | O sistema deve gerar relatórios de performance abrangentes, incluindo métricas como tempo de resposta, taxa de sucesso nas sincronizações de estoque, tempo médio de entrega, e taxa de erros, permitindo o monitoramento e otimização contínua da solução  |

| Identificador  | RF 13 
|----------|----------|
| Nome | Relatórios de Performance do Sistema   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de se integrar de forma transparente com os sistemas legados da Vivo, utilizando APIs específicas para troca de informações e sincronização de dados.  |

## 1.2. Requisitos não funcionais

| Identificador  | RNF 1 
|----------|----------|
| Nome | Carga da aplicação   |
| Categoria | Performance   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve suportar 30 mil requisições por segundo nas consultas ao back-end   |  
| Plano de teste   | Criação e realização de testes envolvendo tempo de resposta da aplicação através do K6, avaliando os resultados obtidos para garantir que o requisito seja cumprido   |  

| Identificador  | RNF 2 
|----------|----------|
| Nome | Tempo de resposta   |
| Categoria | Performance   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ter um tempo máximo de resposta de até 1 segundo em suas consultas ao banco de dado   |  
| Plano de teste   | Criação e realização de testes envolvendo tempo de resposta da aplicação através do K6, avaliando os resultados obtidos para garantir que o requisito seja cumprido   |  

| Identificador  | RNF 3 
|----------|----------|
| Nome | Alta disponibilidade   |
| Categoria | Confiabilidade   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser projetado com redundâncias, garantindo alta disponibilidade e confiabilidade com um índice de disponibilidade de 99,99%.   |  
| Plano de teste   | Simulação de casos de perda de dados e arquivos corrompidos para testar backups e agilidade na restauração dos dados   | 

| Identificador  | RNF 4 
|----------|----------|
| Nome | Sistema intuitivo   |
| Categoria | Usabilidade   |
| Prioridade   | Média  |
| Descrição   | O sistema deve ser intuitivo em sua navegação. Mais de 80% das pessoas devem conseguir fazer todas as atividades propostas pelo a aplicação  |  
| Plano de teste   | Realização de testes de usabilidade com usuários de diferentes áreas. Analisando os seus feedbacks e melhorando a aplicação a partir deles   | 

| Identificador  | RNF 5 
|----------|----------|
| Nome | Escalabilidade do sistema   |
| Categoria | Flexibilidade   |
| Prioridade   | Alta  |
| Descrição   | O sistema deve ser capaz de escalar horizontalmente e verticalmente para lidar com um aumento de até 100% da carga de trabalho sem degradação perceptível   |  
| Plano de teste   |  Simular um aumento súbito de 50%, 75%, e 100% na carga de trabalho em intervalos de 5 minutos, e monitorar as métricas de tempo de resposta, uso de CPU, e taxa de erros. Os teste será repetido 2 vezes para cada cenário, utilizando K6.  | 

<div>