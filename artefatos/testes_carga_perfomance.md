<div align="justify">

# Desenvolvimento e Definição dos Testes de Performance e Carga

## Introdução

&emsp;Os testes foram projetados e implementados utilizando o K6, e serão executados dentro do ambiente Amazon Web Services para validar como o sistema se comportará sob altas cargas de processamento. Cada tipo de teste foca em aspectos específicos dos requisitos não funcionais (RNFs), garantindo que o sistema atenda às expectativas de desempenho e estabilidade.

## Testes de Performance

### Objetivo

&emsp;Os testes de performance têm como objetivo avaliar a resposta do sistema sob carga moderada, medindo o tempo de resposta (latência) e a taxa de sucesso das requisições. Estes testes verificam se o sistema é capaz de lidar com um número comum de usuários simultâneos sem comprometer a experiência do usuário.

**Virtual Users (Usuários Virtuais) e Cálculo de Requisições por Minuto**

&emsp;No contexto de testes de performance com K6, Virtual Users (VUs) representam usuários simulados que fazem requisições ao sistema. Cada VU age como um usuário real, executando ações como acessar páginas ou fazer requisições a *Endpoints* de APIs. No script de teste, cada VU segue um fluxo de ações definido, que inclui um intervalo de espera (*sleep*) entre as requisições. Neste caso, há um intervalo de 1 segundo entre cada requisição. Isso significa que cada VU fará exatamente uma requisição a cada segundo.

&emsp;Para calcular o número de requisições por minuto (RPM), é multiplicado o número de VUs ativos pelo número de segundos em um minuto. Por exemplo, com 5.000 VUs: 
- Requisições por minuto (RPM) = 5.000 VUs * 60 segundos = 300.000 requisições por minuto.

&emsp;Se o número de VUs aumentar para 10.000, a taxa de requisições por minuto também dobrará, resultando em 600.000 RPM, e assim por diante. Este conceito de VUs e o intervalo de espera são fundamentais para simular diferentes cenários de carga e entender como o sistema responde a volumes crescentes de tráfego.

### Configuração

- **Cenário 1: *Endpoint* `/cep/calculate-days`**
  - **Ramp-up**: 200 usuários em 1 minuto.
  - **Sustentação**: 200 usuários por 1 minuto.
  - **Ramp-down**: Redução para 0 usuários em 1 minuto.
  
  **Thresholds**:
  - 95% das requisições devem ter uma duração inferior a 1 segundo.
  - Menos de 1% das requisições podem falhar.
  
- **Cenário 2: *Endpoint* `/products`**
  - **Ramp-up**: 200 usuários em 1 minuto.
  - **Sustentação**: 200 usuários por 1 minuto.
  - **Ramp-down**: Redução para 0 usuários em 1 minuto.
  
  **Thresholds**:
  - 95% das requisições devem ter uma duração inferior a 1 segundo.
  - Menos de 1% das requisições podem falhar.

### Métricas

- **Duração da Requisição**: Mede o tempo que o servidor leva para responder à requisição.
- **Taxa de Falha**: Percentual de requisições que não foram bem-sucedidas.
- **Taxa de Sucesso**: Percentual de requisições bem-sucedidas.

### Resultado Esperado

&emsp;O sistema deve responder 95% das requisições em menos de 1 segundo, com uma taxa de falha inferior a 1%.

## Testes de Carga

### Objetivo

&emsp;Os testes de carga avaliam a capacidade do sistema de escalar e lidar com um grande número de usuários simultâneos, simulando condições de uso para verificar se o sistema mantém a funcionalidade e o desempenho esperado com o aumento de usuários.

### Configuração

- **Cenário 1: *Endpoint* `/cep/calculate-days`**
  - **Ramp-up**: Gradual aumento de 0 para 500 usuários em 1 minuto.
  - **Ramp-up**: Gradual aumento de 100 para 1000 usuários em 2 minutos.
  - **Ramp-up**: Gradual aumento de 100 para 2000 usuários em 1 minuto.
  
  **Thresholds**:
  - 95% das requisições devem ter uma duração inferior a 1,5 segundos.
  - Menos de 2% das requisições podem falhar.
  
- **Cenário 2: *Endpoint* `/products`**
  - **Ramp-up**: Gradual aumento de 0 para 500 usuários em 1 minuto.
  - **Ramp-up**: Gradual aumento de 100 para 1000 usuários em 2 minutos.
  - **Ramp-up**: Gradual aumento de 100 para 2000 usuários em 1 minuto.

- **Cenário 3: *Endpoint* `/products/{id}`**
  - **Ramp-up**: Gradual aumento de 0 para 500 usuários em 1 minuto.
  - **Ramp-up**: Gradual aumento de 100 para 1000 usuários em 2 minutos.
  - **Ramp-up**: Gradual aumento de 100 para 2000 usuários em 1 minuto.
  - **Objetivo**: Verificar a capacidade do sistema de lidar com requisições de um produto único, simulando um lançamento de produto ou promoção.
  
  **Thresholds**:
  - 95% das requisições devem ter uma duração inferior a 2 segundos.
  - Menos de 2% das requisições podem falhar.

### Métricas

- **Duração da Requisição**: Verifica a latência sob alta carga.
- **Taxa de Falha**: Avalia a robustez do sistema frente a uma carga significativa.
- **Taxa de Sucesso**: Mede a capacidade do sistema de manter a funcionalidade sob carga extrema.

## Conclusão

&emsp;Os testes são projetados para validar diferentes aspectos do sistema, desde a capacidade de lidar com cargas moderadas até sua resiliência sob condições variadas de teste. A análise dos resultados desses testes permitirá identificar gargalos de desempenho, pontos de falha e áreas que necessitam de otimização, garantindo que o sistema esteja preparado para operar em ambiente de produção com alta demanda.

# Resultados dos Testes de Carga e Performance

&emsp;O objetivo desta seção é apresentar os resultados dos testes de carga e performance realizados no sistema, com base nos cenários e configurações definidos anteriormente. Os testes foram executados utilizando a ferramenta K6 e os resultados foram analisados para avaliar o desempenho do sistema sob diferentes condições de carga e de infraestrutura.

&emsp;Essa análise é fundamental para identificar possíveis gargalos de desempenho, pontos de falha e áreas que necessitam de otimização, além de permitir comparação entre as infraestuturas que podem ser implementadas na *AWS* e permitir identificar a mais ideal para atender os requisitos não funcionais do projeto.

## Teste de Carga

### Resultados e Análise Comparativa da Infraestrutura no Teste de Carga para o *Endpoint* `calculate-cep`

#### Tabela de Resultados

| Infraestrutura                | *Success Rate* | *Avg Req Duration* (ms) | *Max Req Duration* (ms) | *Iterations/sec* | *VUs Max* | Erros (%) | *Checks* (% Pass) |
|-------------------------------|--------------|-----------------------|-----------------------|----------------|---------|-----------|-----------------|
| T3 Medium                      | 100%         | 5.21                  | 273.31                | 807.61         | 2000    | 0.00%     | 100.00%         |
| Load Balancer (10 x T2 Micro)  | 97.22%       | 285.36                | 10,036.86             | 587.45         | 2000    | 2.77%     | 97.22%          |
| T3 Medium com Kubernetes       | 100%         | 3.13                  | 209.74                | 809.23         | 2000    | 0.00%     | 100.00%         |

#### Análise das Diferenças Entre as Infraestruturas

1. **Taxa de Sucesso (*Success Rate*)**:
   - A infraestrutura utilizando T3 Medium, tanto em ambiente tradicional quanto com Kubernetes, apresentou uma taxa de sucesso de 100%, indicando que todas as requisições foram processadas corretamente.
   - Já a infraestrutura com Load Balancer utilizando 10 instâncias T2 Micro teve uma taxa de sucesso de 97.22%, com uma porcentagem de erros considerável (2.77%).

2. **Duração Média das Requisições (*Avg Req Duration*)**:
   - A configuração com Kubernetes no T3 Medium mostrou o melhor desempenho com uma duração média de requisição de 3.13ms, seguida pela T3 Medium em ambiente tradicional com 5.21ms.
   - A infraestrutura com Load Balancer (10 x T2 Micro) apresentou uma média significativamente mais alta, de 285.36ms, indicando que essa configuração pode estar sobrecarregada ou ineficiente para lidar com a carga.

3. **Duração Máxima das Requisições (*Max Req Duration*)**:
   - O tempo máximo de requisição foi mais controlado nas infraestruturas T3 Medium e Kubernetes, com 273.31ms e 209.74ms, respectivamente.
   - A configuração com Load Balancer foi drasticamente pior, com uma requisição atingindo até 10.03 segundos, o que pode indicar picos de latência extremos e problemas de escalabilidade.

4. **Velocidade de Iterações (*Iterations/sec*)**:
   - As infraestruturas T3 Medium e Kubernetes mantiveram uma taxa de iteração próxima, ambas em torno de 807 a 809 iterações por segundo.
   - A configuração com Load Balancer (10 x T2 Micro) apresentou uma taxa de iteração menor, 587 iterações por segundo, refletindo sua menor eficiência.

5. **Número Máximo de Usuários Virtuais (*VUs Max*)**:
   - Todas as infraestruturas mantiveram um máximo de 2000 usuários virtuais, demonstrando capacidade similar em termos de simultaneidade.

6. **Erros e Verificações (*Checks*)**:
   - A infraestrutura com T3 Medium, tanto no ambiente tradicional quanto com Kubernetes, passou em todos os *Checks*, indicando alta confiabilidade.
   - No Load Balancer, 2.77% das requisições falharam, o que impactou negativamente os *Checks*, resultando em apenas 97.22% de sucesso.

### Resultados e Análise Comparativa da Infraestrutura no Teste de Carga para o *Endpoint* `get-products`

#### Tabela de Resultados

| Infraestrutura                | *Success Rate* | *Avg Req Duration* (ms) | *Max Req Duration* (ms) | *Iterations/sec* | *VUs Max* | Erros (%) | *Checks* (% Pass) |
|-------------------------------|--------------|-----------------------|-----------------------|----------------|---------|-----------|-----------------|
| T3 Medium                      | 100%         | 1.49s                 | 5.43s                 | 324.54         | 2000    | 0.00%     | 100.00%         |
| Load Balancer (10 x T2 Micro)  | 90.51%       | 2.68s                 | 60.00s                | 200.55         | 2000    | 9.48%     | 90.51%          |
| T3 Medium com Kubernetes       | 99.95%       | 1.30s                 | 7.21s                 | 351.86         | 2000    | 0.04%     | 99.95%          |

#### Análise das Diferenças Entre as Infraestruturas

1. **Taxa de Sucesso (*Success Rate*)**:
   - A infraestrutura utilizando T3 Medium apresentou uma taxa de sucesso de 100%, indicando que todas as requisições foram processadas corretamente.
   - A infraestrutura com Kubernetes no T3 Medium também apresentou um excelente desempenho, com uma taxa de sucesso de 99.95%.
   - A configuração com Load Balancer utilizando 10 instâncias T2 Micro teve a menor taxa de sucesso, 90.51%, com uma porcentagem de erros significativa (9.48%).

2. **Duração Média das Requisições (*Avg Req Duration*)**:
   - A configuração com Kubernetes no T3 Medium mostrou um bom desempenho, com uma duração média de requisição de 1.30s, seguida pela T3 Medium em ambiente tradicional com 1.49s.
   - A infraestrutura com Load Balancer (10 x T2 Micro) apresentou uma média de 2.68s, indicando que essa configuração está sobrecarregada ou ineficiente para lidar com a carga.

3. **Duração Máxima das Requisições (*Max Req Duration*)**:
   - O tempo máximo de requisição foi mais controlado na infraestrutura com Kubernetes, com 7.21s, e na T3 Medium, com 5.43s.
   - A configuração com Load Balancer foi drasticamente pior, com uma requisição atingindo até 60.00s, o que pode indicar picos de latência extremos e problemas de escalabilidade.

4. **Velocidade de Iterações (*Iterations/sec*)**:
   - A infraestrutura com Kubernetes manteve a maior taxa de iteração, com 351.86 iterações por segundo, seguida pela T3 Medium com 324.54 iterações por segundo.
   - A configuração com Load Balancer (10 x T2 Micro) apresentou a menor taxa de iteração, 200.55 iterações por segundo, refletindo sua menor eficiência.

5. **Número Máximo de Usuários Virtuais (*VUs Max*)**:
   - Todas as infraestruturas mantiveram um máximo de 2000 usuários virtuais, demonstrando capacidade similar em termos de simultaneidade.

6. **Erros e Verificações (*Checks*)**:
   - A infraestrutura com T3 Medium passou em todos os *Checks*, indicando alta confiabilidade.
   - A configuração com Kubernetes teve uma pequena taxa de falhas, resultando em 99.95% de *Checks* bem-sucedidos.
   - No Load Balancer, 9.48% das requisições falharam, o que impactou negativamente os *Checks*, resultando em apenas 90.51% de sucesso.

### Resultados e Análise Comparativa da Infraestrutura no Teste de Carga para o *Endpoint* `get-products`

#### Tabela de Resultados

| Infraestrutura                | *Success Rate* | *Avg Req Duration* (ms) | *Max Req Duration* (ms) | *Iterations/sec* | *VUs Max* | Erros (%) | *Checks* (% Pass) |
|-------------------------------|--------------|-----------------------|-----------------------|----------------|---------|-----------|-----------------|
| T3 Medium                      | 100%         | 15.2ms                | 120.34ms              | 798.3          | 2000    | 0.00%     | 100.00%         |
| Load Balancer (10 x T2 Micro)  | 90.51%       | 142.35ms              | 2.50s                 | 715.2          | 2000    | 9.48%     | 90.51%          |
| T3 Medium com Kubernetes       | 100%         | 10.5ms                | 600.79ms              | 850            | 2000    | 0.00%     | 100.00%         |

#### Análise das Diferenças Entre as Infraestruturas

1. **Taxa de Sucesso (*Success Rate*)**:
   - A infraestrutura utilizando T3 Medium e a configuração com Kubernetes no T3 Medium apresentaram uma taxa de sucesso de 100%, processando todas as requisições corretamente.
   - A configuração com Load Balancer utilizando 10 instâncias T2 Micro teve uma taxa de sucesso menor, de 90.51%, com 9.48% das requisições falhando. Isso indica que essa configuração teve dificuldades em lidar com a carga de trabalho.

2. **Duração Média das Requisições (*Avg Req Duration*)**:
   - A configuração com Kubernetes no T3 Medium mostrou o melhor desempenho, com uma duração média de requisição de 10.5ms. Isso reflete uma maior eficiência e rapidez em processar as requisições.
   - A T3 Medium em ambiente tradicional teve uma duração média de 15.2ms, ainda apresentando um bom desempenho, mas um pouco inferior ao ambiente com Kubernetes.
   - A configuração com Load Balancer apresentou uma média de 142.35ms, significativamente mais lenta, o que sugere problemas de desempenho ou sobrecarga.

3. **Duração Máxima das Requisições (*Max Req Duration*)**:
   - O tempo máximo de requisição foi relativamente controlado nas infraestruturas com Kubernetes e T3 Medium, atingindo 600.79ms e 120.34ms, respectivamente.
   - A configuração com Load Balancer foi a mais preocupante, com uma requisição atingindo até 2.50s, indicando picos de latência e problemas de escalabilidade sob alta carga.

4. **Velocidade de Iterações (*Iterations/sec*)**:
   - A infraestrutura com Kubernetes manteve a maior taxa de iteração, com 850 iterações por segundo, demonstrando uma maior capacidade de lidar com múltiplas requisições simultâneas.
   - A T3 Medium seguiu com 798.3 iterações por segundo, o que ainda é uma taxa elevada e eficiente.
   - A configuração com Load Balancer teve a menor taxa de iteração, 715.2 iterações por segundo, o que reforça sua menor eficiência em comparação com as outras infraestruturas.

5. **Número Máximo de Usuários Virtuais (*VUs Max*)**:
   - Todas as infraestruturas suportaram um máximo de 2000 usuários virtuais, mostrando uma capacidade semelhante em termos de suporte a usuários simultâneos.

6. **Erros e Verificações (*Checks*)**:
   - As infraestruturas com T3 Medium e Kubernetes no T3 Medium passaram em todos os *Checks*, com 100% de sucesso, o que indica alta confiabilidade e consistência no processamento das requisições.
   - A configuração com Load Balancer teve 9.48% das requisições falhando, impactando negativamente os *Checks* e resultando em apenas 90.51% de sucesso.


## Teste de Performance

### Resultados e Análise Comparativa da Infraestrutura no Teste de Performance para o *Endpoint* `calculate-cep`

#### Tabela de Resultados

| Infraestrutura                | *Success Rate* | *Avg Req Duration* (ms) | *Max Req Duration* (ms) | *Iterations/sec* | *VUs Max* | Erros (%) | *Checks* (% Pass) |
|-------------------------------|--------------|-----------------------|-----------------------|----------------|---------|-----------|-----------------|
| T3 Medium                      | 100.00%      | 1.1ms                 | 72.71ms               | 133.29         | 200     | 0.00%     | 100.00%         |
| Load Balancer (10 x T2 Micro)  | 97.42%       | 281.18ms              | 60s                   | 94.31          | 200     | 2.60%     | 97.39%          |

#### Análise das Diferenças Entre as Infraestruturas

1. **Taxa de Sucesso (*Success Rate*)**:
   - A infraestrutura T3 Medium atingiu uma taxa de sucesso de 100%, o que demonstra sua capacidade de lidar eficientemente com as requisições.
   - O Load Balancer com 10 instâncias T2 Micro apresentou uma taxa de sucesso de 97.42%, com uma porcentagem de erros de 2.60%, mostrando uma menor confiabilidade em comparação à T3 Medium.

2. **Duração Média das Requisições (*Avg Req Duration*)**:
   - A infraestrutura T3 Medium apresentou uma duração média das requisições de apenas 1.1ms, destacando-se como significativamente mais rápida.
   - Por outro lado, a configuração com Load Balancer apresentou uma duração média muito maior, de 281.18ms, indicando problemas de latência e possivelmente gargalos na infraestrutura.

3. **Duração Máxima das Requisições (*Max Req Duration*)**:
   - A T3 Medium teve uma requisição com a duração máxima de 72.71ms, mantendo a latência sob controle.
   - O Load Balancer teve uma requisição atingindo um tempo máximo de 60 segundos, o que é extremamente elevado e sugere graves problemas de desempenho sob carga.

4. **Velocidade de Iterações (*Iterations/sec*)**:
   - A T3 Medium processou 133.29 iterações por segundo, o que reflete sua eficiência em condições de alta carga.
   - O Load Balancer, com 94.31 iterações por segundo, apresentou uma taxa de iterações inferior, correlacionando com a latência mais alta e taxa de erro maior.

5. **Erros e Verificações (*Checks*)**:
   - A T3 Medium passou em todas as verificações sem erros, com 100% de sucesso nos *Checks*.
   - O Load Balancer falhou em 2.60% das requisições, resultando em um menor percentual de *Checks* bem-sucedidos, 97.39%.


### Resultados e Análise Comparativa da Infraestrutura no Teste de Performance para o *Endpoint* `get-products`

#### Tabela de Resultados

| Infraestrutura                | *Success Rate* | *Avg Req Duration* (ms) | *Max Req Duration* (ms) | *Iterations/sec* | *VUs Max* | Erros (%) | *Checks* (% Pass) |
|-------------------------------|--------------|-----------------------|-----------------------|----------------|---------|-----------|-----------------|
| T3 Medium                      | 100.00%      | 1.2s                  | 5.43s                 | 133.29         | 200     | 0.00%     | 100.00%         |
| Load Balancer (10 x T2 Micro)  | 95.37%       | 1.8s              | 60s                   | 87.24          | 200     | 4.62%     | 95.37%          |

#### Análise das Diferenças Entre as Infraestruturas

1. **Taxa de Sucesso (*Success Rate*)**:
   - A infraestrutura T3 Medium apresentou uma taxa de sucesso de 100%, indicando que todas as requisições foram processadas corretamente.
   - A configuração com Load Balancer e 10 instâncias T2 Micro teve uma taxa de sucesso de 95.37%, com uma porcentagem de erros de 4.62%, refletindo uma menor confiabilidade em comparação à T3 Medium.

2. **Duração Média das Requisições (*Avg Req Duration*)**:
   - A infraestrutura T3 Medium teve uma duração média das requisições de 1.2s, o que é significativamente mais rápida do que a média do Load Balancer.
   - A configuração com Load Balancer apresentou uma duração média muito maior de 1.8s, com uma latência considerável, sugerindo que o sistema pode estar enfrentando problemas de escalabilidade e desempenho sob carga.

3. **Duração Máxima das Requisições (*Max Req Duration*)**:
   - A T3 Medium teve uma duração máxima de requisição de 5.43s, indicando uma variabilidade na latência.
   - O Load Balancer apresentou uma duração máxima de 60 segundos, o que é extremamente alto e sugere picos de latência graves e problemas de performance em condições de alta carga.

4. **Velocidade de Iterações (*Iterations/sec*)**:
   - A T3 Medium processou 133.29 iterações por segundo, mostrando um desempenho eficiente sob carga.
   - O Load Balancer com 10 instâncias T2 Micro teve uma taxa de iterações inferior, com 87.24 iterações por segundo, refletindo uma menor eficiência e capacidade de resposta.

5. **Erros e Verificações (*Checks*)**:
   - A infraestrutura T3 Medium passou em todas as verificações, com 100% de sucesso nos *Checks*.
   - O Load Balancer falhou em 4.62% das requisições, resultando em um percentual de *Checks* bem-sucedidos de 95.37%, indicando uma maior taxa de falhas e menor confiabilidade.

### Resultados de Teste de Carga Adicional

#### Teste com Kubernetes e T3 Medium

| **Métrica**                      | **Valor**              |
|---------------------------------|------------------------|
| **Status de Resposta**           | 100.00% ✓ 863.940    |
| **Conteúdo Correto**             | 100.00% ✓ 863.940    |
| **Dados Recebidos**              | 104 MB (428 kB/s)     |
| **Dados Enviados**               | 117 MB (482 kB/s)     |
| **Duração Média das Requisições**| 479.2 ms              |
| **Duração Máxima das Requisições**| 4.37 s                |
| **Taxa de Falhas**               | 0.00% (Nenhuma falha) |
| **Requisições por Segundo**      | 3.599.75              |
| **Iterações por Segundo**        | 3.599.75              |
| **Tempo Médio de Resposta**      | 479.20 ms             |
| **Tempo 95º Percentil**          | 1.46 s                |
| **Tempo 99º Percentil**          | 2.37 s                |
| **Tempo Máximo de Resposta**     | 4.37 s                |
| **Tempo Médio de Recebimento**   | 1.62 ms               |
| **Tempo Médio de Envio**         | 9.88 ms               |
| **Tempo Médio de Espera**        | 467.68 ms             |
| **Número Máximo de VUs**         | 10.000                |
| **Número de VUs Utilizados**     | 394                   |

#### Descrição do Teste

&emsp;Foi realizado um teste de carga em uma infraestrutura Kubernetes rodando dentro de um T3 Medium, utilizando 100 pods para a rota `calculate-cep`. O teste gerou aproximadamente 860 mil requisições em um período de 4 minutos. Durante o teste, o cálculo real do CEP foi simulado (mockado), o que pode mudar significantemente com o cálculo real do cep. O teste demonstrou excelente performance com uma taxa de sucesso de 100% e uma duração média de requisição de 479.2 ms, indicando alta capacidade de processamento e estabilidade da infraestrutura.

### Conclusão Geral

&emsp;Após a análise dos testes de carga, ficou claro que a solução com Kubernetes e instâncias T3 Medium apresentou o melhor desempenho geral, oferecendo uma taxa de sucesso de 100% e uma baixa duração média das requisições, evidenciando sua capacidade robusta e estabilidade para lidar com grandes volumes de tráfego. A infraestrutura T3 Medium também mostrou um bom desempenho, mas com uma duração de requisição um pouco maior em comparação ao Kubernetes. Em contraste, a configuração com Load Balancer e 10 instâncias T2 Micro apresentou o pior desempenho, com uma taxa de sucesso inferior e uma latência significativamente maior. No entanto, a configuração com Load Balancer oferece vantagens em termos de custo e flexibilidade, adaptando-se melhor às variações na demanda. Apesar de seu desempenho mais baixo, ela pode ser otimizada para melhorar a eficiência e a capacidade de resposta.

</div>