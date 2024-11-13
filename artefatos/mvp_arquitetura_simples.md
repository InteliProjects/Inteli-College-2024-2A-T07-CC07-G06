<div align="justify">

# MVP Arquitetura Simples

&emsp;Um Mínimo Produto Viável (MVP) é uma versão simplificada do produto em desenvolvimento, que inclui as funcionalidades essenciais para resolver os principais problemas dos usuários. O objetivo de um MVP é validar a viabilidade do produto no mercado de forma rápida e econômica, permitindo que você colete *feedback* real dos usuários, teste hipóteses de valor, minimize riscos e otimize o uso de recursos antes de investir em um desenvolvimento completo.


## 1. Contexto do projeto

&emsp;O projeto desenvolvido em parceria com a Vivo consiste em solucionar um desafio de sincronização de estoques. Atualmente, a sincronização do estoque tem custo elevado para a plataforma de _e-commerce_ pois necessita de uma série de processamentos integrados com o sistema legado da empresa para validar se o estoque é confiável ou não, isso impacta diretamente no tempo de processamento das requisições e retorno dos dados necessários, fazendo com que ocorram incoerências entre o que o cliente solicita e acredita estar disponível e o que de fato existe nos estoques, assim afetando negativamente na jornada do cliente que utiliza dos serviços do _Consumer Eletronics_. Resolver esse desafio é de suma importância para melhorar a experiência, não só do cliente final que receberá seu produto com garantia de que ele está disponível, mas também para o setor logístico da empresa que contará com uma plataforma altamente escalável, disponível e confiável para gerenciar seus negócios.


## 2. _Front-end_

&emsp;O _front-end_ de uma aplicação é a parte com a qual os usuários interagem diretamente, englobando todos os elementos visuais como botões, formulários, menus, animações e o _layout_ da página, tais partes sendo conhecidas como a interface do usuário (UI). Ademais, o _front_ da aplicação também é majoritariamente responsável pela experiência do usuário, que se refere à usabilidade e acessibilidade da aplicação.


## 3. _Back-end_

&emsp;O _back-end_ de uma aplicação é a parte que opera "nos bastidores" através do gerenciamento da lógica de negócios, armazenamento e manipulação de dados, comunicação com outras partes do sistema, processamento de requisições do usuário, execução de _scripts_, interação com o banco de dados e integração com _front-end_, assim garantindo o funcionamento e a lógica da aplicação.


### 3.1. Escolha de Tecnologias

&emsp;Existem diversas opções de tecnologias para o desenvolvimento *back-end* no mercado, diferentes opções de linguagens e _frameworks_ que podem adequar-se melhor para diferentes cenários e situações. O projeto em questão depende de maior performance visto que a velocidade de sincronização dos dados é um desafio para o parceiro, além disso, uma estrutura robusta faz-se vital para garantir não só a velocidade do traânsito de informações, como também a confiabilidade desses dados. Com isso, dentre as opções levantadas, foi decidido usar a linguagem Java para o desenvolvimento devido à sua maturidade, robustez, ampla adoção no mercado e grande quantidade de bibliotecas e _frameworks_ disponíveis que facilitarão o desenvolvimento.

&emsp;Baseado na escolha da linguagem, existem diversas opções de ferramentas de desenvolvimento, sendo o _Spring Boot_ uma das opções mais famosas e consolidadas do mercado. O _Spring Boot_ é conhecido por sua facilidade de configuração, extensa documentação, e grande comunidade de suporte. Ele permite a criação rápida de aplicações robustas e escaláveis. Entretanto, o mesmo não é tão otimizado para arquiteturas de microsserviços e de _cloud_, técnicas que se popularizaram no desenvolvimento web atual. Para atender aos desafios do ambiente em nuvem, optamos pelo _Quarkus_, que tem a missão de entregar aplicações com o "Java subatômico e subsônico", o que significa que o _framework_ é nativo e otimizado para nuvem, tem um tempo de inicialização extremamente rápido e um consumo de memória reduzido, além de ser compatível com a maioria das melhores bibliotecas e extensões do ecossistema Java, sendo essa uma e escolha ideal para o desenvolvimento de um sistema distribuído.


### 3.2. Estrutura do _MVP_

&emsp;Para a estrutura inicial do MVP, foi construído um _back-end_ simples com as principais e mais recorrentes configurações de um projeto. Inicialmente, foram construídas estruturas genéricas para facilitar o desenvolvimento das operações de CRUD da aplicação, assim permitindo a construção agilizada de entidades e dando maior foco para a estruturação das regras de negócio da aplicação. Em adição, estruturas básicas para um tratamento de erros eficiente e monitoramento da "saúde" da aplicação foram antecipadamente desenvolvidas com intuito de garantir a disponibilidade e monitoramento da aplicação no ambiente em nuvem. Para facilitar o desenvolvimento da aplicação, foram configurados ambientes de _dev_, _uat_ e _prod_ que são, respectivamente, destinados para o desenvolvimento, testes e produção. Nesse momento, a principal diferença entre cada ambiente é seu tipo de base de dados, _dev_ utiliza uma base _H2 in memory_ para testes rápidos das funcionalidades, _uat_ possui uma base _H2 file based_ para testes com persistência dos dados e _prod_ possui uma base conectada ao _Amazon RDS_, ferramenta em nuvem que usaremos para suprir as demandas do parceiro de projeto.


### 3.3. Testes

&emsp;Os testes de requisição são essenciais para garantir que um sistema se comporte corretamente ao receber requisições externas. Esses testes são fundamentais para assegurar que os requisitos propostos sejam cumpridos, garantindo que a aplicação esteja em conformidade com as especificações do projeto.  
&emsp;A seguir, apresenta-se a documentação dos testes que abrangem as rotas da aplicação, acompanhada de suas respectivas descrições. Também é possível visualizar a documentação da API [aqui](https://speeding-space-493045.postman.co/workspace/Gabriel_Inteli~85cd70cb-9bec-48b6-9d27-d74cd74f2c47/collection/26958193-d781a154-6fea-4bfd-beb6-9c934e4fec30?action=share&creator=26958193) através do Postman.


#### 📁 Produtos 


##### End-point: products
Rota de consulta que retorna todos os produtos disponíveis na loja, juntamente com suas respectivas informações: ID, SKU, nome, preço, descrição e link da imagem
##### Method: GET
>
>http://100.29.153.12:8080/products
>

---

##### End-point: products
Rota de criação onde o produto enviado será adicionado ao banco de dados, retornando o próprio produto como confirmação da operação.
##### Method: POST
>
>http://100.29.153.12:8080/products
>
##### Body (*raw*)

```json
{
  "id": 0,
  "sku": "sam-sung",
  "name": "Samsung Galaxy",
  "price": 500.99,
  "description": "Celular para comprar",
  "linkImage": "https://th.bing.com/th/id/OIP.0nCFULjbDkrjfl_imMVd9gHaHa?rs=1&pid=ImgDetMain"
}
```


---

##### End-point: products by id
Rota de consulta que retorna um produto específico com base no ID fornecido. Espera-se que a rota retorne todas as informações relacionadas ao produto correspondente ao ID especificado.
##### Method: GET
>
>http://100.29.153.12:8080/products/151
>

---

##### End-point: products
Rota de atualização de um produto específico. Espera-se que a rota atualize as informações do produto com o ID especificado no banco de dados e retorne o produto atualizado como confirmação.
##### Method: PUT
>
>http://100.29.153.12:8080/products/151
>
##### Body (*raw*)

```json
{
    "description": "Celular para comprar mais barato",
    "price": 400
}
```

---

##### End-point: products
Rota de exclusão de um produto. Espera-se que o produto com o ID fornecido seja removido do banco de dados e que a rota retorne o produto excluído como confirmação.
##### Method: DELETE
>
>http://100.29.153.12:8080/products/151
>

---

#### 📁 Distribuidores 


##### End-point: distributor
Rota de consulta que retorna todos os distribuidores disponíveis, juntamente com suas respectivas informações: ID, nome e CEP.
##### Method: GET
>
>http://100.29.153.12:8080/distributors
>

---

##### End-point: distributor
Rota de criação onde o distribuidor enviado será adicionado ao banco de dados, retornando o próprio distribuidor como confirmação da operação.
##### Method: POST
>
>http://100.29.153.12:8080/distributors
>
##### Body (*raw*)

```json
{
  "id": 0,
  "name": "Loja Vivo - Avenida Brasil",
  "cep": "04384032"
}
```

---

##### End-point: distributor by id
Rota de consulta que retorna um distribuidor específico com base no ID fornecido. Espera-se que a rota retorne todas as informações relacionadas ao distribuidor correspondente ao ID especificado.
##### Method: GET
>
>http://100.29.153.12:8080/distributors/2
>

---

##### End-point: distributor
Rota de atualização de um distribuidor específico. Espera-se que a rota atualize as informações do distribuidor com o ID especificado no banco de dados e retorne o distribuidor atualizado como confirmação.
##### Method: PUT
>
>http://100.29.153.12:8080/distributors/1
>
##### Body (*raw*)

```json
{
    "name": "Loja Vivo - Avenida Brasil, 1078"
}
```

---

##### End-point: distributor
Rota de exclusão de um distribuidor. Espera-se que o distribuidor com o ID fornecido seja removido do banco de dados e que a rota retorne o distribuidor excluído como confirmação.
##### Method: DELETE
>
>http://100.29.153.12:8080/distributors/1
>

---

#### 📁 Ordem-de-venda 

##### End-point: sales-orders
Rota de consulta que retorna todas as ordens de venda disponíveis, juntamente com suas respectivas informações: ID, distribuidor, data da venda, total, status e data de entrega.
##### Method: GET
>
>http://100.29.153.12:8080/sales-orders
>

---

##### End-point: sales-orders
Rota de criação onde a ordem de venda enviada será adicionada ao banco de dados, retornando a própria ordem de venda como confirmação da operação.
##### Method: POST
>
>http://100.29.153.12:8080/sales-orders
>
##### Body (*raw*)

```json
{
  "id": 0,
  "distributor": {
    "id": 2,
    "cep": "04384032",
    "name": "Loja Vivo - Avenida Brasil"
    },
  "saleDate": "2022-03-10T12:15:50",
  "total": 1000,
  "status": "entregue",
  "deliveryDate": "2022-03-10T12:15:50"
}
```

---

##### End-point: sales-orders by id
Rota de consulta que retorna uma ordem de venda específica com base no ID fornecido. Espera-se que a rota retorne todas as informações relacionadas à ordem de venda correspondente ao ID especificado.
##### Method: GET
>
>http://100.29.153.12:8080/sales-orders/1
>

---

##### End-point: sales-orders
Rota de atualização de uma ordem de venda específica. Espera-se que a rota atualize as informações da ordem de venda com o ID especificado no banco de dados e retorne a ordem de venda atualizada como confirmação.
##### Method: PUT
>
>http://100.29.153.12:8080/sales-orders/1
>
##### Body (*raw*)

```json
{
  "total": 1500,
  "deliveryDate": "2022-03-14T12:15:50"
}
```

---

##### End-point: sales-orders
Rota de exclusão de uma ordem de venda. Espera-se que a ordem de venda com o ID fornecido seja removido do banco de dados e que a rota retorne a ordem de venda excluída como confirmação.
##### Method: DELETE
>
>http://100.29.153.12:8080/sales-orders/1
>

---

#### 📁 Venda-produto 


##### End-point: sales-products
Rota de consulta que retorna todas as vendas de produtos, juntamente com suas respectivas informações: ID, ordem de venda, produto, quantidade e preço unitário.
##### Method: GET
>
>http://100.29.153.12:8080/sales-products
>

---

##### End-point: sales-products
Rota de criação onde a venda enviada será adicionada ao banco de dados, retornando a própria venda como confirmação da operação.
##### Method: POST
>
>http://100.29.153.12:8080/sales-products
>
##### Body (*raw*)

```json
{
  "id": 0,
  "order": {
    "id": 1,
    "deliveryDate": "2022-03-14T12:15:50",
    "distributor": {
        "id": 2,
        "cep": "04384032",
        "name": "Loja Vivo - Avenida Brasil"
    },
    "saleDate": "2022-03-10T12:15:50",
    "status": "entregue",
    "total": 1500.0
    },
  "product": {
    "id": 2,
    "description": "Celular com tecnologia de ponta e recursos inovadores ideal para quem busca praticidade e estilo",
    "linkImage": "https://www.limifield.pt/wp-content/uploads/2022/09/Smartphone-Apple-iPhone-14-256Gb-6.1-5G-Branco-Estrela-1.jpg",
    "name": "Smartphone Nokia C01 Plus",
    "price": 3074.8,
    "sku": "MBY-33161"
    },
  "quantity": 2,
  "unitPrice": 3074.8
}
```

---

##### End-point: sales-products by id
Rota de consulta que retorna uma venda específica com base no ID fornecido. Espera-se que a rota retorne todas as informações relacionadas à venda correspondente ao ID especificado.
##### Method: GET
>
>http://100.29.153.12:8080/sales-products/1
>

---

##### End-point: sales-products
Rota de atualização de uma venda específica. Espera-se que a rota atualize as informações da venda com o ID especificado no banco de dados e retorne a de venda atualizada como confirmação.
##### Method: PUT
>
>http://100.29.153.12:8080/sales-products/1
>
##### Body (*raw*)

```json
{
    "id": 1,
    "order": {
        "id": 1,
        "deliveryDate": "2022-03-14T12:15:50",
        "distributor": {
            "id": 2,
            "cep": "04384032",
            "name": "Loja Vivo - Avenida Brasil"
        },
        "saleDate": "2022-03-10T12:15:50",
        "status": "entregue",
        "total": 1500.0
    },
    "product": {
        "id": 2,
        "description": "Celular com tecnologia de ponta e recursos inovadores ideal para quem busca praticidade e estilo",
        "linkImage": "https://www.limifield.pt/wp-content/uploads/2022/09/Smartphone-Apple-iPhone-14-256Gb-6.1-5G-Branco-Estrela-1.jpg",
        "name": "Smartphone Nokia C01 Plus",
        "price": 3074.8,
        "sku": "MBY-33161"
    },
    "quantity": 4,
    "unitPrice": 3074.8
}
```

---

##### End-point: sales-products
Rota de exclusão de uma venda. Espera-se que a venda com o ID fornecido seja removido do banco de dados e que a rota retorne a venda excluída como confirmação.
##### Method: DELETE
>
>http://100.29.153.12:8080/sales-products/1
>

---

#### 📁 Produtos-distribuidores 


##### End-point: products-distributors
Rota de consulta que retorna todos os estoques de produtos por loja, juntamente com suas respectivas informações: ID, distribuidor, produto, quantidade disponível e quantidade reservada.
##### Method: GET
>
>http://100.29.153.12:8080/products-distributors
>

---

##### End-point: products-distributors
Rota de criação onde o estoque de produtos por loja enviado será adicionado ao banco de dados, retornando o próprio estoque de produtos por loja como confirmação da operação.
##### Method: POST
>
>http://100.29.153.12:8080/products-distributors
>
##### Body (*raw*)

```json
{
  "id": 0,
  "distributor": {
    "id": 2,
    "cep": "04384032",
    "name": "Loja Vivo - Avenida Brasil"
    },
  "product": {
    "id": 8,
    "description": "Smartphone versátil com tela de alta definição e desempenho eficiente para todas as suas necessidades",
    "linkImage": "https://d3ddx6b2p2pevg.cloudfront.net/Custom/Content/Products/10/76/1076885_smartphone-apple-iphone-11-preto-128gb_z1_637409470785489369.jpg",
    "name": "Smartphone Infinix Hot",
    "price": 1636.22,
    "sku": "JGU-07513"
    },
  "quantityAvailable": 100,
  "quantityReserved": 30
}
```

---

##### End-point: products-distributors
Rota de consulta que retorna um estoque de produtos por loja específico com base no ID fornecido. Espera-se que a rota retorne todas as informações relacionadas ao estoque de produtos por loja correspondente ao ID especificado.
##### Method: GET
>
>http://100.29.153.12:8080/products-distributors/1
>

---

##### End-point: products-distributors
Rota de atualização de um estoque de produtos por loja específico. Espera-se que a rota atualize as informações do estoque de produtos por loja com o ID especificado no banco de dados e retorne o estoque de produtos por loja atualizado como confirmação.
##### Method: PUT
>
>http://100.29.153.12:8080/products-distributors/1
>
##### Body (*raw*)

```json
{
    "id": 1,
    "distributor": {
        "id": 2,
        "cep": "04384032",
        "name": "Loja Vivo - Avenida Brasil"
    },
    "product": {
        "id": 8,
        "description": "Smartphone versátil com tela de alta definição e desempenho eficiente para todas as suas necessidades",
        "linkImage": "https://d3ddx6b2p2pevg.cloudfront.net/Custom/Content/Products/10/76/1076885_smartphone-apple-iphone-11-preto-128gb_z1_637409470785489369.jpg",
        "name": "Smartphone Infinix Hot",
        "price": 1636.22,
        "sku": "JGU-07513"
    },
    "quantityAvailable": 90,
    "quantityReserved": 30
}
```

---

##### End-point: products-distributors
Rota de exclusão de um estoque de produtos por loja. Espera-se que o estoque de produtos por loja com o ID fornecido seja removido do banco de dados e que a rota retorne o estoque de produtos por loja excluído como confirmação.
##### Method: DELETE
>
>http://100.29.153.12:8080/products-distributors/1
>

---

#### 📁 CEP 


###### End-point: CEP
Rota responsável por calcular o tempo de entrega. Espera-se que a rota retorne o primeiro e o último dia do intervalo de entrega.
##### Method: POST
>
>http://100.29.153.12:8080/cep/calculate-days?cep=04384032
>
##### Query Params

|Param|value|
|---|---|
|cep|04384032|

---

##### End-point: dothello
Rota base que retorna uma mensagem "Hello, Grupo.!" para verificar o funcionamento da aplicação.
##### Method: GET
>
>http://100.29.153.12:8080/dothello
>


## 4. _Database_

&emsp;Um _database_ é um sistema organizado para armazenar, gerenciar e recuperar dados de forma eficiente. Ele permite que aplicações armazenem informações de maneira estruturada, facilitando operações de leitura, escrita, atualização e exclusão de dados. Bancos de dados podem ser relacionais, como MySQL e PostgreSQL, que usam tabelas e SQL para gerenciar dados, ou não relacionais (NoSQL), como MongoDB, que utilizam modelos flexíveis como documentos, chave-valor ou grafos.

&emsp;Este documento descreve o modelo lógico do banco de dados NSync, desenvolvido para gerenciar a distribuição e venda de produtos em múltiplos centros de distribuição e lojas de aparelhos de celular e acessórios. No contexto de um projeto que visa resolver o problema de alta carga de processamento na plataforma de e-commerce durante a sincronização de estoque, que impacta negativamente a jornada do cliente. O projeto NSync tem como objetivo principal desenvolver uma solução de inventário distribuído confiável, que apoie o gerenciamento do estoque em tempo real, proporcionando alta disponibilidade, suporte a grandes volumes de transações e maior visibilidade para o time de Logística sobre as saídas de estoque. Esta documentação detalha a estrutura lógica do banco de dados que suportará esse sistema, garantindo integridade, consistência e eficiência nas operações de gerenciamento de estoque e vendas.


### 4.1. Processo Atual

1. Geração manual do arquivo de dados das Lojas e disponibilização para processamento via Control-M.
2. Carga de arquivo de dados das Lojas realizada pelo time de Integração e disponibilizado via API no Barramento Corporativo.
3. Geração do arquivo de Estoque das Lojas e Centros de Distribuição (CDs) através da Plataforma SAP ECC.
4. Transferência do arquivo de estoque das lojas via Connect Direct do File Gateway.
5. Leitura e Importação do arquivo de carga realizada pela plataforma SAP Commerce Cloud.
6. Durante a jornada do cliente, o CEP para entrega é verificado via API Corporativa para encontrar a loja mais próxima com estoque disponível.
7. Ao finalizar a compra, o sistema acessa via API o SAP ECC para validar a disponibilidade do produto e finaliza a venda.


### 4.2. Objetivos

&emsp;O principal objetivo deste modelo é fornecer uma estrutura de dados eficiente e robusta para gerenciar informações sobre distribuidoras, produtos, ordens de venda e movimentações de estoque. Isso inclui:

- Facilitar a inserção, atualização e consulta de dados.
- Garantir a integridade e consistência dos dados.
- Otimizar o desempenho das operações de banco de dados.


### 4.3. Descrição das Entidades

&emsp;O modelo lógico contém as seguintes entidades (figura 01):

<div align="center">

<sub>Figura 01: Entidades do Modelo Lógico</sub>

![Modelo Lógico](./img/entidades-bd.png)

<sup>Fonte: Material produzido pelos próprios autores (2024).</sup>

</div>

- Distributors: Diz respeito aos diversos locais de vendas, bem como as próprias distribuidoras;
- Products: São os produtos oferecidos pela Vivo no e-commerce
- Products_distributors: Tabela intermediária para produtos e distribuidoras;
- Sales_orders: Informações de vendas do produto;
- Sales_products: Tabela intermediária para vendas e produtos.


### 4.4. Relacionamentos

&emsp;Os principais relacionamentos entre as entidades são:

- Uma **Distribuidora** pode ter vários **Produtos** (Relacionamento: Products_distributors).
- Uma **Ordem de Venda** pode ter vários **Produtos** (Relacionamento: Sales_products).
- As **Movimentações de Estoque** registram as transações de entrada e saída de produtos em uma distribuidora.
  

### 4.5. Campos Comuns

&emsp;Todas as entidades possuem os seguintes campos comuns para controle de auditoria:

- `created_at` (*timestamp*): Data e hora de criação do registro.
- `updated_at` (*timestamp*): Data e hora da última atualização do registro.
- `deleted_at` (*timestamp*): Data e hora da exclusão lógica do registro.


### 4.6. Detalhamento das Entidades


#### 4.6.1. Distributors

| Campo | Tipo    | Descrição                            |
| ----- | ------- | ------------------------------------ |
| id    | PK      | Identificador único da distribuidora |
| name  | Varchar | Nome da distribuidora                |
| cep   | Integer | Código postal                        |


#### 4.6.2. Products

| Campo       | Tipo    | Descrição                      |
| ----------- | ------- | ------------------------------ |
| id          | PK      | Identificador único do produto |
| SKU         | Decimal | Indentificador do produto      |
| name        | Varchar | Nome do produto                |
| price       | Decimal | Preço do produto               |
| description | Varchar | Descrição do produto           |


#### 4.6.3. Products_distributors

| Campo             | Tipo    | Descrição                              |
| ----------------- | ------- | -------------------------------------- |
| id                | PK      | Identificador único do registro        |
| distributor_id    | FK      | Identificador da distribuidora         |
| product_id        | FK      | Identificador do produto               |
| quantity          | Integer | Quantidade do produto na distribuidora |
| quantity_reserved | Integer | Quantidade de produto reservado        |


#### 4.6.4. Sales_orders

| Campo          | Tipo      | Descrição                             |
| -------------- | --------- | ------------------------------------- |
| id             | PK        | Identificador único da ordem de venda |
| distributor_id | FK        | Identificador da distribuidora        |
| sale_date      | Timestamp | Data da venda                         |
| total          | Decimal   | Valor total da venda                  |
| status         | Varchar   | Status da venda                       |
| delivery_date  | Timestamp | data de entrega do produto            |


#### 4.6.5. Order_product

| Campo      | Tipo    | Descrição                       |
| ---------- | ------- | ------------------------------- |
| id         | PK      | Identificador único do registro |
| order_id   | FK      | Identificador da ordem de venda |
| product_id | FK      | Identificador do produto        |
| quantity   | Integer | Quantidade do produto na ordem  |
| unit_price | Decimal | Preço unitário do produto       |


#### 4.6.6. Stock_movements

| Campo          | Tipo      | Descrição                            |
| -------------- | --------- | ------------------------------------ |
| id             | PK        | Identificador único da movimentação  |
| distributor_id | FK        | Identificador da distribuidora       |
| product_id     | FK        | Identificador do produto             |
| movements_type | Varchar   | Tipo de movimentação (entrada/saída) |
| quantity       | Integer   | Quantidade movimentada               |
| movement_date  | Timestamp | Data da movimentação                 |


### 4.7. Considerações Finais

&emsp;Este modelo lógico foi projetado para atender às necessidades de gestão de distribuidoras e vendas de produtos no contexto do projeto descrito, que visa otimizar a sincronização de estoque em tempo real, reduzir a sobrecarga de processamento na plataforma de e-commerce e fornecer maior visibilidade para o time de Logística sobre as saídas de estoque. A estrutura proposta visa garantir a integridade e consistência dos dados, além de proporcionar flexibilidade e eficiência nas operações de banco de dados.


## 5. Vídeos Explicativos

&emsp;Nesta seção, são apresentados dois vídeos demonstrativos que evidenciam o funcionamento de uma aplicação web desenvolvida e implementada utilizando a infraestrutura da Amazon Web Services (AWS). 

&emsp;No primeiro vídeo, disponível [neste *link*](https://www.youtube.com/watch?v=wSGFnBvGdmk), é demonstrada a execução de operações CRUD (Create, Read, Update, Delete) no banco de dados da aplicação, hospedado no AWS RDS (Relational Database Service). A gravação inclui uma explicação do desenvolvimento, apresentando testes que indicam o funcionamento e a integração entre o *back-end* e o banco de dados.

&emsp;No segundo vídeo, disponível [neste *link*](https://www.youtube.com/watch?v=wI1TUg7hRYo), é exibido o *front-end* da aplicação, implementado em uma página estática e hospedado no AWS S3. A gravação mostra a integração entre o *front-end* e a infraestrutura de hospedagem, destacando o trabalho desenvolvido pelo grupo.

&emsp;Esses vídeos têm como objetivo demonstrar o funcionamento completo da aplicação, desde o gerenciamento do banco de dados até a apresentação do *front-end*, utilizando os recursos da AWS.

</div>