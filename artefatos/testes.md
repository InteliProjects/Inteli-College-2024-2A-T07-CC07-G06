<div align="justify">

# Relatório de Testes - Projeto Quarkus

## Introdução

&emsp;Este relatório apresenta uma visão abrangente dos testes realizados no projeto de inventário distribuído, abrangendo os testes unitários realizados. Os testes foram realizados com o objetivo de validar a funcionalidade e a integridade do sistema, garantindo que os componentes atendam aos requisitos definidos. O foco foi assegurar que a solução suportasse a sincronização de estoque em tempo real, atendendo ao objetivo de melhorar a visibilidade para o time de logística e reduzir o custo de processamento da plataforma de e-commerce.

---

## Testes - Quarkus

### 1. Testes Unitários

#### Classe: `ProductRequestDTOTest`

&emsp;A manipulação correta de dados de produtos é essencial para garantir que o inventário distribuído funcione corretamente e sincronize o estoque de forma precisa entre lojas e centros de distribuição. Os dados de produtos, como `productId`, `quantity` e `total`, são críticos para as operações de validação de estoque e conclusão de vendas, sendo o núcleo do processamento de inventário.

- **Teste 1: Verificação de Inicialização da Classe `ProductRequestDTO`**
  - **Descrição**: Este teste assegura que uma instância da classe `ProductRequestDTO` é criada com sucesso e não é nula após a inicialização. Isso garante que o sistema pode criar objetos de requisição de produto, essenciais para manipular dados durante a jornada do cliente.
  - **Etapas**:
    1. Criação de uma instância do `ProductRequestDTO`.
    2. Verificação se a instância não é nula utilizando `assertNotNull`.
  - **Resultado**: O teste foi bem-sucedido, confirmando que a inicialização da classe está correta e que objetos de produtos podem ser manipulados com sucesso.

- **Teste 2: Manipulação do Campo `productId`**
  - **Descrição**: Este teste valida a funcionalidade dos métodos de acesso (`setProductId` e `getProductId`) para o campo `productId`, que identifica os produtos no inventário.
  - **Etapas**:
    1. Criação de uma nova instância do `ProductRequestDTO`.
    2. Definição de um valor para `productId` e uso do método `setProductId`.
    3. Recuperação do valor usando `getProductId`.
    4. Comparação do valor definido e recuperado utilizando `assertEquals`.
  - **Resultado**: O teste passou, evidenciando que a manipulação do `productId` funciona como esperado. Isso garante que o sistema poderá identificar corretamente os produtos no estoque durante a sincronização.

- **Teste 3: Manipulação do Campo `quantity`**
  - **Descrição**: Este teste verifica a funcionalidade dos métodos de acesso para o campo `quantity`, que define a quantidade de produtos disponíveis no inventário.
  - **Etapas**:
    1. Instanciação do `ProductRequestDTO`.
    2. Definição de um valor para `quantity` usando `setQuantity`.
    3. Recuperação do valor com `getQuantity`.
    4. Comparação dos valores usando `assertEquals`.
  - **Resultado**: O teste foi concluído com sucesso, confirmando que a quantidade de produtos pode ser gerenciada corretamente no sistema.

- **Teste 4: Manipulação do Campo `total`**
  - **Descrição**: Verifica a funcionalidade dos métodos de acesso para o campo `total`, que representa o valor total associado ao pedido ou ao produto.
  - **Etapas**:
    1. Instanciação do `ProductRequestDTO`.
    2. Definição de um valor para `total`.
    3. Recuperação do valor e comparação.
  - **Resultado**: O teste foi bem-sucedido. Isso assegura que o sistema pode calcular corretamente o valor total dos produtos no estoque, essencial para a sincronização correta dos dados de vendas.

- **Teste 5: Verificação do Método `toString`**
  - **Descrição**: Valida a implementação do método `toString`, que converte os dados de um produto para uma string legível. Isso é importante para a criação de logs e para a depuração durante o processo de sincronização.
  - **Etapas**:
    1. Criação de uma instância do `ProductRequestDTO`.
    2. Chamada do método `toString`.
    3. Verificação se o resultado não é nulo.
  - **Resultado**: O teste passou, confirmando que o método `toString` está implementado corretamente, permitindo a geração de logs legíveis de produtos.

---

#### Classe: `DistanceServiceTest`

&emsp;A entrega rápida e eficiente depende da capacidade do sistema de calcular corretamente a distância entre lojas e centros de distribuição, permitindo que o produto seja enviado da localização mais próxima ao cliente. Esse cálculo afeta diretamente a experiência do cliente e otimiza o processo de sincronização de inventário e entrega.

- **Teste 1: Cálculo da Distância Entre Cidades**
  - **Descrição**: Este teste verifica o cálculo de distância entre duas cidades, garantindo que o sistema escolha corretamente a loja ou centro de distribuição mais próximo para processar o pedido e entregar o produto.
  - **Etapas**:
    1. Definição das coordenadas de Nova York e Los Angeles.
    2. Chamada do método responsável pelo cálculo da distância.
    3. Comparação do resultado esperado (aproximadamente 3935,74 km).
  - **Resultado**: O teste foi bem-sucedido, validando o cálculo correto da distância. Isso é crucial para o sistema de entrega, permitindo uma otimização na seleção da melhor loja para processar o pedido, e garantindo uma sincronização eficiente do inventário.

---

#### Classe: `GeoResponseTest`

&emsp;O sistema de inventário distribuído depende da localização precisa de lojas e centros de distribuição para tomar decisões corretas sobre a entrega e a sincronização de estoque. A manipulação correta dos dados geográficos garante que as informações sejam processadas corretamente.

- **Teste 1: Manipulação do Campo `latitude`**
  - **Descrição**: Verifica o funcionamento dos métodos `setLatitude` e `getLatitude`, essenciais para armazenar e recuperar as coordenadas geográficas das lojas e centros de distribuição.
  - **Etapas**:
    1. Criação de uma instância de `GeoResponse`.
    2. Definição de um valor para `latitude`.
    3. Recuperação e verificação do valor definido.
  - **Resultado**: O teste passou, confirmando que a latitude é armazenada e recuperada corretamente, garantindo que o sistema possa determinar a localização das lojas e centros de distribuição com precisão.

- **Teste 2: Manipulação do Campo `longitude`**
  - **Descrição**: Verifica o funcionamento dos métodos `setLongitude` e `getLongitude`, que definem a longitude de uma loja ou centro de distribuição.
  - **Etapas**:
    1. Instanciação de `GeoResponse`.
    2. Definição e recuperação do valor de `longitude`.
    3. Comparação dos valores.
  - **Resultado**: O teste foi concluído com sucesso, garantindo que o sistema armazene corretamente a localização das lojas e centros de distribuição. Isso é essencial para o funcionamento correto do cálculo de distância e a otimização da entrega.

---

### Conclusão

&emsp;Os testes realizados asseguram a integridade das principais funcionalidades da solução de inventário distribuído, garantindo que os dados críticos, como produto, quantidade, localização e cálculos de distância, sejam manipulados e processados corretamente. Com isso, a sincronização de estoque em tempo real pode ser feita de forma precisa e eficiente, suportando grandes volumes de transações e melhorando a visibilidade para o time de logística.

&emsp;Também é possível visualizar outros testes a partir da seguinte documentação: [Testes Quarkus: Resumo](https://github.com/Inteli-College/2024-2A-T07-CC07-G06/blob/main/docs/testes-quarkus.md)

### Testes de microserviço

&emsp;Esta documentação foca nos testes dos microsserviços desenvolvidos em Java para operações de compras, consultas de CEP e processamento de arquivos `CSVs`. O objetivo é validar o comportamento e a integridade dos serviços, garantindo que as funcionalidades, como cálculo de dias de entrega e processamento de pedidos, sejam executadas corretamente em diferentes cenários. 

#### 📁 Collection: products-service

---

**End-point: products**  
Serviço referente à consulta dos produtos.

**Method: GET**  
>```
>http://3.221.150.22:30005/products
>```

**Resultado:**
```json
{
  [
      {
          "id": 1,
          "description": "Display supervívido e brilhante",
          "linkImage": "https://m.media-amazon.com/images/I/517RnD277kL.__AC_SX300_SY300_QL70_ML2_.jpg",
          "name": "Samsung, Smartphone Samsung Galaxy A05",
          "price": 679.0,
          "sku": "sku123"
      }
  ]
}
```

---

**End-point: products/{id}**  
Serviço referente à consulta de um produto específico.

**Method: GET**  
>```
>http://3.221.150.22:30005/products/1
>```

**Resultado:**
```json
{
    "id": 1,
    "description": "Display supervívido e brilhante",
    "linkImage": "https://m.media-amazon.com/images/I/517RnD277kL.__AC_SX300_SY300_QL70_ML2_.jpg",
    "name": "Samsung, Smartphone Samsung Galaxy A05",
    "price": 679.0,
    "sku": "sku123"
}
```

#### 📁 Collection: purchases-service

---

**End-point: purchases**  
Serviço referente à rota de compra. Responsável por retirar os itens do estoque e realizar efetivamente a compra.

**Method: POST**  
>```
>http://3.221.150.22:30005/purchases
>```

**Body (raw)**
```json
{
  "cep": "05510-021",
  "products": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Resultado:**
Status Code 200 OK

---

#### 📁 Collection: csv-processor-service

---

**End-point: upload-csv**  
O microserviço `CsvProcessorResource` é responsável por processar múltiplos arquivos CSV e popular o banco de dados com as entidades correspondentes (e.g., Distribuidores, Produtos, Sales Orders).

**Method: POST**  
>```
>http://3.221.150.22:30005/csv-processor/populate-all
>```

**Body (form-data)**  
- **files[]**: (Vários arquivos CSV a serem enviados simultaneamente, cada arquivo correspondendo a uma entidade específica)

**Resultado:**
```json
{
  "message": "All CSVs processed successfully.",
}
```

**Resultado:**
Status Code 202 Accepted

---

#### 📁 Collection: delivery-service

---

**End-point: calculate-days**  
Serviço responsável por calcular o prazo de entrega baseado no CEP do cliente e na disponibilidade do produto nos distribuidores mais próximos.

**Method: POST**  
>```
>http://3.221.150.22:30005/delivery/calculate-days
>```

**Body (raw)**
```json
{
    "cep": "05510-021",
    "productId": 1
}
```

**Resultado:**
```json
{
  "firstDeliveryDate": "01-10-2024",
  "lastDeliveryDate": "05-10-2024"
}
```

---
  
# Relatório de Testes - Projeto GO
## Introdução
&emsp;Os testes desempenham um papel fundamental no desenvolvimento de software, garantindo que cada parte do sistema funcione conforme esperado. Para garantir a qualidade do código, uma abordagem estruturada e eficiente é necessária, o que inclui a realização de testes unitários, de integração e de microserviços. Cada um desses tipos de testes foca em um aspecto específico do sistema, desde a validação de funcionalidades isoladas até a verificação de como diferentes partes interagem entre si. No contexto de um sistema baseado em microserviços, essa estratégia de testes é essencial para garantir que cada serviço funcione corretamente de forma independente e também em conjunto, respeitando os contratos de comunicação entre os serviços.

## Testes - GO
### Testes unitários

#### Testes de Produto

**Teste 1:** Criação de um produto

**Descrição:** Está sendo testado se a criação de uma instância da entidade *Product* funciona corretamente.

**Preparativos:** 

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Definir constantes de teste representando um produto de exemplo com valores específicos para `Id`, `SKU`, `Name`, `Price`, `Description`, e `LinkImage`.

**Passo a passo:**

- Testes automatizado
    - Criar uma instância de `Product` com valores constantes para `Id`, `SKU`, `Name`, `Price`, `Description`, e `LinkImage`.
    - Verificar se todos os atributos da instância criada correspondem exatamente aos valores constantes definidos no início do teste

**Resultado esperado:** A instância de `Product` criada tenha os valores de seus atributos iguais às constantes `testProductID`, `testProductSKU`, `testProductName`, `testProductPrice`, `testProductDescription`, e `testProductLinkImage`.

**Resultado obtido:** Teste bem sucedido

```json
{
    "testProductID": 1,
    "testProductSKU": "SKU123",
    "testProductName": "Alive's t-shirt",
    "testProductPrice": 1299.99,
    "testProductDescription": "I doubt they'll give us an alive's t-shirt",
    "testProductLinkImage": "http://alives-t-shirt.com/alives-t-shirt.jpg"
}
```

**Teste 2:** Criação de um produto

**Descrição:** Está sendo testado se a criação de uma instância da entidade *Product* funciona corretamente.

**Preparativos:** 

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Ter criado um produto no banco de dados.

**Passo a passo:**

- Testes automatizado
    - Configurar um mock do banco de dados usando `sqlmock`.
    - Definir um mock de resultado de consulta SQL com uma linha contendo os valores de produto (`Id`, `SKU`, `Name`, `Price`, `Description`, `LinkImage`).
    - Executar a consulta SQL simulada.
    - Utilizar o método `MapSQLRowToEntity` para mapear a linha de resultado para uma instância da entidade `Product`.
    - Verificar se os valores mapeados da entidade correspondem aos valores do mock de SQL.

**Resultado esperado:**

- A consulta SQL simulada retorne uma linha de resultado com valores de produto.
- O método `MapSQLRowToEntity` converta a linha de resultado corretamente em uma instância da entidade `Product`, com os valores mapeados correspondendo exatamente às constantes definidas para o produto.

**Resultado obtido:** Teste bem sucedido

```json
{
    "testProductID": 1,
    "testProductSKU": "SKU123",
    "testProductName": "Alive's t-shirt",
    "testProductPrice": 1299.99,
    "testProductDescription": "I doubt they'll give us an alive's t-shirt",
    "testProductLinkImage": "http://alives-t-shirt.com/alives-t-shirt.jpg"
}
```

#### Testes de Distribuidores

**Teste 3:** Criação de um distribuidor

**Descrição**: Este teste verifica se a criação de uma instância da entidade *Distributor* funciona corretamente.

**Preparativos**:

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Definir constantes de teste representando um distribuidor de exemplo com valores específicos para `Id`, `Name`, e `Cep`.

**Passo a passo**:

- Teste automatizado:
    - Criar uma instância de `Distributor` com os valores constantes para `Id`, `Name`, e `Cep`.
    - Verificar se todos os atributos da instância criada correspondem exatamente aos valores constantes definidos no início do teste.

**Resultado esperado**: A instância de `Distributor` criada deve ter os valores de seus atributos iguais às constantes `testDistributorID`, `testDistributorName`, e `testDistributorCep`.

**Resultado obtido**: Teste bem-sucedido.

```json
{
    "testDistributorID": 1,
    "testDistributorName": "Distributor A",
    "testDistributorCep": "05510-021"
}

```

---

**Teste 4:** Mapeamento de linha SQL para entidade `Distributor`

**Descrição**: Este teste verifica se o método `MapSQLRowToEntity` mapeia corretamente uma linha de resultado SQL para uma instância da entidade *Distributor*.

**Preparativos**:

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Definir um mock de resultado de consulta SQL com uma linha contendo os valores do distribuidor (`Id`, `Name`, `Cep`).

**Passo a passo**:

- Teste automatizado:
    - Configurar um mock do banco de dados usando `sqlmock`.
    - Definir um mock de resultado de consulta SQL com uma linha contendo os valores de distribuidor (`Id`, `Name`, `Cep`).
    - Executar a consulta SQL simulada.
    - Utilizar o método `MapSQLRowToEntity` para mapear a linha de resultado para uma instância da entidade `Distributor`.
    - Verificar se os valores mapeados da entidade correspondem aos valores do mock de SQL.

**Resultado esperado**:

- A consulta SQL simulada deve retornar uma linha de resultado com valores do distribuidor.
- O método `MapSQLRowToEntity` deve converter corretamente a linha de resultado em uma instância da entidade *Distributor*, com os valores mapeados correspondendo exatamente às constantes definidas.

**Resultado obtido**: Teste bem-sucedido.

```json
{
    "testDistributorID": 1,
    "testDistributorName": "Distributor A",
    "testDistributorCep": "05510-021"
}

```

#### Testes de Produtos-Distribuidores

**Teste 5:** Criação de uma entidade *ProductDistributor*

**Descrição**: Este teste verifica se a criação de uma instância da entidade *ProductDistributor* funciona corretamente, garantindo que todos os atributos sejam atribuídos corretamente com base nos valores fornecidos.

**Preparativos**:

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Definir constantes de teste representando um distribuidor-produto com valores específicos para `DistributorId`, `ProductId`, `AmountAvailable`, e `AmountReserved`.

**Passo a passo**:

- Teste automatizado:
    - Criar uma instância de `ProductDistributor` com os valores constantes para `DistributorId`, `ProductId`, `AmountAvailable`, e `AmountReserved`.
    - Verificar se todos os atributos da instância criada correspondem exatamente aos valores constantes definidos no início do teste.

**Resultado esperado**: A instância de `ProductDistributor` criada deve ter os valores de seus atributos iguais às constantes `testDistributorId`, `testProductId`, `testAmountAvailable`, e `testAmountReserved`.

**Resultado obtido**: Teste bem-sucedido.

```json
{
    "testDistributorId": 1,
    "testProductId": 1,
    "testAmountAvailable": 100,
    "testAmountReserved": 50
}

```

---

**Teste 6:** Mapeamento de linha SQL para entidade *ProductDistributor*

**Descrição**: Este teste verifica se o método `MapSQLRowToEntity` mapeia corretamente uma linha de resultado SQL para uma instância da entidade *ProductDistributor*.

**Preparativos**:

- Configurar o ambiente de testes com as dependências necessárias:
    - Usar o pacote `testify` para facilitar as asserções (`assert` e `require`).
    - Usar o pacote `sqlmock` para simular o comportamento do banco de dados.
- Definir um mock de resultado de consulta SQL com uma linha contendo os valores de distribuidor-produto (`Id`, `DistributorId`, `ProductId`, `AmountAvailable`, `AmountReserved`).

**Passo a passo**:

- Teste automatizado:
    - Configurar um mock do banco de dados usando `sqlmock`.
    - Definir um mock de resultado de consulta SQL com uma linha contendo os valores de distribuidor-produto (`Id`, `DistributorId`, `ProductId`, `AmountAvailable`, `AmountReserved`).
    - Executar a consulta SQL simulada.
    - Utilizar o método `MapSQLRowToEntity` para mapear a linha de resultado para uma instância da entidade *ProductDistributor*.
    - Verificar se os valores mapeados da entidade correspondem aos valores do mock de SQL.

**Resultado esperado**:

- A consulta SQL simulada deve retornar uma linha de resultado com valores de distribuidor-produto.
- O método `MapSQLRowToEntity` deve converter corretamente a linha de resultado em uma instância da entidade *ProductDistributor*, com os valores mapeados correspondendo exatamente às constantes definidas.

**Resultado obtido**: Teste bem-sucedido.

```json
{
    "testProductDistributorID": 1,
    "testDistributorId": 1,
    "testProductId": 1,
    "testAmountAvailable": 100,
    "testAmountReserved": 50
}

```

Link para todos os testes unitários do GO: [Testes unitários Go](https://github.com/Inteli-College/2024-2A-T07-CC07-G06/tree/develop/src/backend/go/tests)

### Testes de Integração

&emsp;Os **testes de integração** têm como objetivo garantir que os diferentes módulos de um sistema interajam corretamente, validando o comportamento do sistema como um todo em condições reais. Esse tipo de teste é essencial para identificar problemas na comunicação entre serviços, APIs e outras dependências do sistema, especialmente em arquiteturas de microserviços, onde os componentes interagem por meio de chamadas de rede e troca de dados.

&emsp;Por outro lado, os **testes de carga** são cruciais para medir o desempenho de um sistema sob condições de uso intensivo. Eles simulam uma quantidade significativa de tráfego para verificar a capacidade do sistema de lidar com grandes volumes de solicitações simultâneas, medindo parâmetros como latência, throughput e taxa de falhas. Esses testes são vitais para identificar gargalos, testar a escalabilidade e garantir que o sistema mantenha um nível de desempenho aceitável mesmo sob alta demanda.

&emsp;Abaixo, são apresentados os resultados dos testes de carga realizados em um microserviço desenvolvido em Go (denominado **MICROGO**), hospedado em uma instância T3 Medium.

#### 2. **MICROGO - Microserviço em Go (T3 Medium)**

&emsp;Neste teste, o microserviço foi submetido a uma carga de 2000 usuários virtuais (VUs) em uma instância T3 Medium. O objetivo era avaliar o comportamento do microserviço sob carga pesada, observando sua latência, throughput e taxa de falhas.

&emsp;Os resultados revelaram uma latência média de 102,26ms e um throughput de 564,7 requisições por segundo (req/s). Embora o sistema tenha mantido uma taxa de sucesso de 99,70%, foi observada uma taxa de falhas de 0,20%, indicando que há espaço para otimizações, principalmente para lidar com picos de carga. A análise detalhada dos parâmetros é apresentada a seguir:

| **Parâmetro** | **MICROGO - T3 Medium** |
| --- | --- |
| **Total de Requisições** | 474.932 (564,7 req/s) |
| **Usuários Virtuais (VUs)** | 2.000 max |
| **Duração Total** | 14m 30s |
| **Latência Média (http_req_duration)** | 102,26ms |
| **Percentil 95 da Latência** | 404,83ms |
| **Taxa de Sucesso (%)** | 99,70% |
| **Requisições Falhadas (%)** | 0,20% |
| **Respondeu em <1,5s (%)** | 99% |
| **Throughput** | 564,7 req/s |

#### Análise dos Resultados

1. **Latência**: A latência média de 102,26ms indica que, em geral, o serviço consegue responder rapidamente às solicitações. Entretanto, o percentil 95 da latência (404,83ms) mostra que uma pequena porcentagem das requisições sofreu atrasos mais significativos, sugerindo que o desempenho sob carga pesada pode variar.
2. **Throughput**: O throughput de 564,7 req/s demonstra que o microserviço conseguiu lidar com um volume considerável de requisições em um período contínuo, o que é um indicativo positivo da sua capacidade de atender altas demandas.
3. **Taxa de Falhas**: Embora a taxa de sucesso tenha sido alta (99,70%), a pequena porcentagem de requisições falhadas (0,20%) reforça a necessidade de ajustes e otimizações, especialmente em cenários de pico de uso.
4. **Oportunidades de Melhoria**: A taxa de falhas de 0,20% sugere que o serviço pode beneficiar de ajustes de desempenho, como otimizações no código Go ou melhorias na configuração da infraestrutura para lidar melhor com cenários de uso extremo.

&emsp;Os testes de carga no **MICROGO** revelaram que, apesar de manter um desempenho sólido, há potencial para melhorias em sua capacidade de lidar com cargas pesadas. O sistema apresentou um throughput aceitável e uma alta taxa de sucesso, mas a latência mais alta em alguns momentos e a pequena taxa de falhas indicam que otimizações no serviço ou na infraestrutura poderiam beneficiar sua resiliência e escalabilidade.

### Testes de microserviço

&emsp;O teste de microserviços é fundamental para garantir o correto funcionamento e a integração dos diferentes serviços que compõem uma arquitetura distribuída. Esses testes permitem verificar não apenas as rotas e as funcionalidades individuais de cada microserviço, mas também como eles se comunicam e se integram uns com os outros, simulando cenários reais de uso. Ao realizar os testes em um ambiente similar ao de produção, é possível assegurar a disponibilidade dos serviços e antecipar potenciais falhas de comunicação ou desempenho. Além disso, os testes de microserviços ajudam a validar a resiliência do sistema, verificando como ele lida com falhas em microserviços dependentes e garantindo a robustez da aplicação.
#### 📁 Collection: products-service 


**End-point: products**  
Serviço referente à consulta dos produtos

**Method: GET**
>```
>http://35.170.198.190:30000/products
>```

**Resultado:**
```json
{
  [
    {
      "id": 1,
      "sku": "sku-123",
      "name": "Galaxy S15",
      "price": 1000.00,
      "description": "Celular Galaxy S15",
      "link_image": "https://cdn.dummyjson.com/products/images/smartphones/iPhone%205s/1.png",
    },
    {
      "id": 2,
      "sku": "sku-456",
      "name": "iPhone 6",
      "price": 299.99,
      "description": "The iPhone 6 is a stylish and capable smartphone with a larger display and improved performance. It introduced new features and design elements, making it a popular choice in its time.",
      "link_image": "https://cdn.dummyjson.com/products/images/smartphones/iPhone%206/1.png",
    }
  ]

} 
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: products by id**  
Serviço referente à consulta de um produto específico

**Method: GET**
>```
>http://35.170.198.190:30000/products/1
>```

**Resultado:**
```json
{
      "id": 1,
      "sku": "sku-123",
      "name": "Galaxy S15",
      "price": 1000.00,
      "description": "Celular Galaxy S15",
      "link_image": "https://cdn.dummyjson.com/products/images/smartphones/iPhone%205s/1.png",
    }
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: products by ids**  
Serviço referente a criação de produtos

**Method: POST**
>```
>http://35.170.198.190:30000/products/ids
>```
**Body (raw)**

```json
{
    "productsIds": [
        1,
        2,
        3
    ]
}
```
**Resultado:**
Status Code 200 OK

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: distributors-service 


**End-point: distributors**  
Serviço referente a consulta de todos os distribuidores.

**Method: GET**
>```
>http://35.170.198.190:30000/distributors
>```

**Resultado:**
```json
{
  [
    {
      "id": 1,
      "name": "Distribuidora MobileXpress",
      "cep": "01001-000"
    },
    {
      "id": 2,
      "name": "CelularTech Distribuidora",
      "cep": "01310-001"
    }
  ]

} 
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: get-by-id**  
Serviço referente a consulta de um distribuidor específico.

**Method: GET**
>```
>http://35.170.198.190:30000/distributors/1
>```

**Resultado:**
```json
{
  "id": 1,
  "name": "Distribuidora MobileXpress",
  "cep": "01001-000"
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: products-distributors-service 


**End-point: products-distributors**  
Serviço referente a consulta do inventário (produtos-distribuidores).

**Method: GET**
>```
>http://35.170.198.190:30000/products-distributors
>```

**Resultado:**
```json
{
  [
    {
      "id": 1,
      "distributorId": 1,
      "productId": 1,
      "amountAvailable": 100,
      "amountReserved": 40
    },
    {
      "id": 2,
      "distributorId": 1,
      "productId": 2,
      "amountAvailable": 70,
      "amountReserved": 70
    }
  ]
} 
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: products-distributors by id**
Serviço referente a consulta de um produto espefíco de um distribuidor específico do inventário (produtos-distribuidores).

**Method: GET**
>```
>http://35.170.198.190:30000/products-distributors/1
>```

**Resultado:**
```json
{
  "id": 1,
  "distributorId": 1,
  "productId": 1,
  "amountAvailable": 100,
  "amountReserved": 40
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: products-distributors update**
Serviço referente a atualização de um produto espefíco de um distribuidor específico do inventário (produtos-distribuidores).

**Method: PUT**
>```
>http://35.170.198.190:30000/products-distributors
>```
**Body (raw)**

```json
{
    "id": 1,
    "distributorId": 1,
    "productId": 1,
    "amountAvailable": 86,
    "amountReserved": 10
}
```

**Resultado:**
Status Code 200 OK

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: sales-orders-service 


**End-point: sales-orders**
Serviço referente a consulta de todas as ordens de venda.  
**Method: GET**
>```
>http://35.170.198.190:30000/sales-orders
>```



**Resultado:**
```json
{
  [
    {
      "id": 1, 
      "cep": "04384032",
      "total": 1000.00,
      "saleDate": "29-09-2024",
      "firstDeliveryDate": "01-10-2024",
      "lastDeliveryDate": "05-10-2024",
    },
    {
      "id": 2, 
      "cep": "04384030",
      "total": 299.99,
      "saleDate": "29-09-2024",
      "firstDeliveryDate": "02-10-2024",
      "lastDeliveryDate": "06-10-2024",
    }
  ]
} 
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: sales-orders by id**
Serviço referente a consulta de uma ordem de venda específica.

**Method: GET**
>```
>http://35.170.198.190:30000/sales-orders/1
>```

**Resultado:**
```json
{
  "id": 1, 
  "cep": "04384032",
  "total": 1000.00,
  "saleDate": "29-09-2024",
  "firstDeliveryDate": "01-10-2024",
  "lastDeliveryDate": "05-10-2024",
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: sales-orders post**  
Serviço referente a criação de uma ordem de venda.

**Method: POST**
>```
>http://35.170.198.190:30000/sales-orders
>```
**Body (raw)**

```json
{
    "cep": "05510021",
    "total": 1299.99,
    "saleDate": "2024-08-01T10:15:30Z",
    "firstDeliveryDate": "2024-08-05T00:00:00Z",
    "lastDeliveryDate": "2024-08-08T00:00:00Z"
}
```

**Resultado:**
Status Code 200 OK

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: sales-orders-products-service 


**End-point: sales-orders-products**
Serviço de consulta de todas as orders de venda-produtos.

**Method: GET**
>```
>http://35.170.198.190:30000/sales-orders-products
>```

**Resultado:**
```json
{
  [
    {
      "id": 1,
      "saleOrderId": 1,
      "productId": 1,
      "distributorId": 1,
      "quantity": 1,
      "unitPrice": 1000.00
    },
    {
      "id": 2, 
      "saleOrderId": 1,
      "productId": 2,
      "distributorId": 2,
      "quantity": 1,
      "unitPrice": 299.99,
    }
  ]
} 
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: sales-orders-products by id**
Serviço de consulta de uma ordem de venda-produto específica.

**Method: GET**
>```
>http://35.170.198.190:30000/sales-orders-products/1
>```

**Resultado:**
```json
{
  "id": 1,
  "saleOrderId": 1,
  "productId": 1,
  "distributorId": 1,
  "quantity": 1,
  "unitPrice": 1000.00
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: sales-orders-products post**
Serviço de criação de uma ordem de venda-produto.
**Method: POST**
>```
>http://35.170.198.190:30000/sales-orders-products
>```
**Body (raw)**

```json
{
  "saleOrderId": 3,
  "productId": 1,
  "distributorId": 1,
  "quantity": 1,
  "unitPrice": 1000.00
}
```

**Resultado:**
Status Code 200 OK


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: purchases-service 


**End-point: purchases**
Serviço referente a rota de compra. Responsável por retirar os itens do estoque e realizar efetivamente a compra.

**Method: POST**
>```
>http://35.170.198.190:30000/purchases
>```
**Body (raw)**

```json
{
    "products": [
        {
            "productId": 1,
            "quantity": 1
        }
    ],
    "cep": "05510-021"
}
```
**Resultado:**
Status Code 200 OK


⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃
#### 📁 Collection: delivery-service 


**End-point: calculate-days**  
Serviço responsável por calcular o prazo de entrega baseado no CEP do cliente e na disponibilidade do produto nos distribuidores mais próximos.

**Method: POST**
>```
>http://35.170.198.190:30000/cep/calculate-days
>```
**Body (raw)**

```json
{
    "cep": "05510-021",
    "productId": 1
}
```

**Resultado:**
```json
{
  "firstDeliveryDate": "01-10-2024",
  "lastDeliveryDate": "05-10-2024",
}
```

⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃ ⁃

**End-point: delivery-details**
Serviço responsável por fazer o calculo do prazo de entrega durante o processo de compra.

**Method: POST**
>```
>http://35.170.198.190:30000/delivery-details
>```
**Body (raw)**

```json
{
    "products": [
        {
            "productId": 1,
            "quantity": 1
        }
    ],
    "cep": "05510-021"
}
```

**Resultado:**
```json
{
  "firstDeliveryDate": "01-10-2024",
  "lastDeliveryDate": "05-10-2024",
  "distributorId": 1
}
```

## Conclusão
&emsp;A implementação dos testes unitários, de integração e de microserviços é uma prática crucial para aumentar a confiabilidade e a manutenção do sistema. Os testes unitários garantem que cada componente funcione de forma correta isoladamente, enquanto os testes de integração validam a interação entre diferentes módulos ou serviços. Por fim, os testes de microserviços asseguram que o sistema distribuído funcione como um todo, garantindo a consistência e a comunicação adequada entre os serviços. Com esses testes bem estruturados, é possível mitigar riscos de falhas e garantir que as funcionalidades continuem funcionando corretamente, mesmo após modificações ou novas implementações no sistema.

</div>