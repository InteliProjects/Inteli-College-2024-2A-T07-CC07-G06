<div align="justify">

<table>
<tr>
<td>
<a href= "https://www.vivo.com.br/"> <img src="./artefatos/img/vivo-logo.png" alt="vivo.com.br" border="0" width="20%"></a>
</td>
<td><a href= "https://www.inteli.edu.br/"><img src="inteli-logo.png" alt="Inteli - Instituto de Tecnologia e Liderança" border="0" width="50%"></a>
</td>
</tr>
</table>

# Introdução

Este é um dos repositórios do projeto de alunos do Inteli em parceria com a Vivo no 2º semestre de 2024. Este projeto está sendo desenvolvido por alunos do Módulo 7 do curso de Ciência da Computação.

# Projeto: Aplicação escalável em sistemas distribuídos

# Grupo: Nsync (G6)

# 👨‍🎓 Integrantes:

* [Ana Luisa Goes Barbosa](https://www.linkedin.com/in/ana-luisa-goes-barbosa/)
* [Gabriel Coletto Silva](https://www.linkedin.com/in/gabrielcolettosilva/)
* [Isabelle Santos](https://www.linkedin.com/in/isabelle-santos-507067204/)
* [Lucas Nogueira Nunes](https://www.linkedin.com/in/lucas-nogueira-nunes/)
* [Mauro das Chagas Junior](https://www.linkedin.com/in/mauro-das-chagas-junior/)
* [Vitto Mazeto](https://www.linkedin.com/in/vitto-mazeto/)

# 👩‍🏫 Professores:

## Orientador
* [🧭 Laíza Ribeiro Silva](https://www.linkedin.com/in/laizaribeiro/)

## Instrutores
* [📈 Lisane Valdo - Professora de Business](https://www.linkedin.com/in/lisane-valdo/)
* [🎨 Guilherme Cestari - Professor de Design UX/UI](https://www.linkedin.com/in/gui-cestari/)
* [👑 Mônica Anastassiu - Professor de Liderança](https://www.linkedin.com/in/monica-anastassiu-d-sc-2568522/)
* [👨‍💻 André Godoi, PhD - Professor de Computação](https://www.linkedin.com/in/profandregodoi/)
* [👨‍💻 Rodolfo Goya - Professor de Computação](https://www.linkedin.com/in/rodolfo-goya-6ab187/)
* [👨‍💻 Tomaz Mikio Sasaki - Professor de Computação](https://www.linkedin.com/in/tmsasaki/)

# 💡 Descrição

Este projeto aborda o desafio de utilizar o estoque distribuído de múltiplas lojas como centros de distribuição para o e-commerce da Vivo. A solução desenvolvida passou por várias abordagens para o _back-end_, incluindo arquiteturas monolíticas (Java Quarkus e Go), microservices (Kubernetes com Quarkus e Go), e _serverless_ (AWS Lambda em Python e Go). Após uma série de testes de carga e desempenho, o monolito em Go apresentou a melhor performance, oferecendo uma solução escalável e eficiente. Ademais, foi desenvolvido um front-end focado na simulação da plataforma de _e-commerce_ do parceiro, permitindo maior compreensão sobre os testes realizados. A aplicação também foi integrada ao Grafana para monitoramento e submetida a rigorosos testes de carga e validação.

# ⚙️ Execução da Aplicação

## _Front-end_

## Requisitos de Instalação

- **Node.js v20**  
  O Node.js é necessário para executar e gerenciar o ambiente de desenvolvimento do projeto.  
  **Como baixar:**  
  1. Acesse o [site oficial do Node.js](https://nodejs.org/).
  2. Baixe a versão LTS v20 para o seu sistema operacional.
  3. Siga as instruções de instalação fornecidas no site.

- **Visual Studio Code (VS Code) [Recomendado]**  
  Uma IDE (Ambiente de Desenvolvimento Integrado) facilita o desenvolvimento e a execução do projeto.  
  **Como baixar:**  
  1. Acesse o [site oficial do VS Code](https://code.visualstudio.com/).
  2. Baixe a versão para o seu sistema operacional.
  3. Instale o VS Code e configure conforme necessário.
  4. É recomendado instalar as extensões relevantes para desenvolvimento com Next.js, como `ESLint`, `Prettier`, e `JavaScript (ES6) code snippets`.

### Configuração do Projeto

1. **Instalação das Dependências:**
   - Navegue até o diretório do projeto e instale as dependências necessárias usando o comando:
     ```bash
     npm install
     ```

2. **Executar o Projeto Localmente:**
   - Para iniciar o servidor de desenvolvimento e visualizar o projeto localmente, execute:
     ```bash
     npm run dev
     ```
   - Ou, se preferir usar Yarn, execute:
     ```bash
     yarn dev
     ```

3. **Visualizar o Projeto:**
   - Abra o navegador e acesse [http://localhost:3000](http://localhost:3000) para ver o resultado.

### Exportação Estática para Hospedagem no S3

Este projeto está configurado para usar o Next.js em modo estático, com a configuração de saída ajustada para permitir a hospedagem no Amazon S3. Esta abordagem foi adotada para fins acadêmicos, permitindo a publicação do site estático na AWS.

**Como gerar a saída estática para o S3:**

1. **Construa o Projeto:**
   - Execute o comando para gerar a build do projeto:
     ```bash
     npm run build
     ```
   - Isso criará a pasta `out`, que contém todos os arquivos estáticos prontos para serem hospedados.
   - Essa pasta foi feita automaticamente por causa da configuração de *static exports*, explicada no site oficial do Nextjs [Explicação](https://nextjs.org/docs/pages/building-your-application/deploying/static-exports) 

3. **Hospede no S3:**
   - Faça o upload dos arquivos da pasta `out` diretamente para o bucket S3 configurado para hospedagem de site estático.

### Mais Informações

Para aprender mais sobre o Next.js, consulte os seguintes recursos:

- [Documentação do Next.js](https://nextjs.org/docs) - descubra as funcionalidades e a API do Next.js.
- [Aprender Next.js](https://nextjs.org/learn) - um tutorial interativo sobre Next.js.

## _Back-end_

### Requisitos de Instalação

- **JDK v17**  
  O _Java Development Kit_ (JDK) v17 é necessário para compilar e executar a aplicação.  
  **Como baixar:**  
  1. Acesse o site oficial da [Oracle](https://www.oracle.com/java/technologies/downloads/) ou [OpenJDK](https://openjdk.org/projects/jdk/17/).
  2. Baixe a versão apropriada para o seu sistema operacional.
  3. Siga as instruções de instalação.

- **IntelliJ IDEA ou Visual Studio Code (VS Code) [Recomendado]**  
  Uma IDE (Ambiente de Desenvolvimento Integrado) facilita o desenvolvimento e a execução do projeto.  
  **Como baixar:**  
  1. Para IntelliJ IDEA, acesse o [site oficial da _JetBrains_](https://www.jetbrains.com/pt-br/idea/download/?section=windows), escolha a versão _Community_ (gratuita) ou _Ultimate_ (paga).
  2. Para _Visual Studio Code_, acesse o [site oficial da _Microsoft_](https://code.visualstudio.com/) e baixe a versão para o seu sistema operacional.
  3. Instale a _IDE_ e configure conforme necessário.
  4. Para o _VS Code_, é recomendado instalar o _Extension Pack for Java_ que fornece uma série de ferramentas que facilitam o desenvolvimento com Java.

- **Maven**  
  Maven é uma ferramenta de automação de compilação que também gerencia dependências do projeto.  
  **Como baixar:**  
  1. Acesse o [site oficial do Apache Maven](https://maven.apache.org/).
  2. Baixe o arquivo binário (.zip ou .tar.gz) para o seu sistema operacional.
  3. Extraia o arquivo e adicione o caminho do binário à variável de ambiente PATH.

### Configuração dos Ambientes de Execução

* **No IntelliJ IDEA:**
  1. Abra o IntelliJ IDEA e vá para "Run" -> "Edit Configurations".
  2. Clique no botão de adicionar uma nova configuração, simbolizado pelo símbolo "+".
  3. Em "Build and Run", clique em "Modify Options".
  4. Selecione "VM options" e insira a seguinte opção: `-Dquarkus.profile=dev` para o ambiente de desenvolvimento (dev).
  5. Repita o mesmo processo para configurar os ambientes de UAT (`-Dquarkus.profile=uat`) e de produção (`-Dquarkus.profile=prod`).

* **No Terminal:**
  1. Navegue até o diretório raiz do seu projeto.
  2. Execute o seguinte comando para iniciar o ambiente de desenvolvimento:  
     ```bash
     ./mvnw compile quarkus:dev -Dquarkus.profile=dev
     ```
  3. Para os ambientes de UAT e produção, execute os comandos abaixo, respectivamente:  
     ```bash
     ./mvnw compile quarkus:dev -Dquarkus.profile=uat
     ```  
     ```bash
     ./mvnw compile quarkus:dev -Dquarkus.profile=prod
     ```

### 🐋 Execução via Docker

- **Docker**  
  O Docker é necessário para construir e executar a aplicação em um container.  
  **Como baixar:**  
  1. Acesse o site oficial do [Docker](https://www.docker.com/get-started).
  2. Baixe e instale a versão apropriada para o seu sistema operacional.

- Para construir a imagem Docker do _back-end_, execute o seguinte comando na raiz do projeto:
  ```bash
  docker build -f src/main/docker/Dockerfile.jvm -t quarkus/app-jvm .
  ```

- Após a imagem ser construída com sucesso, execute o seguinte comando para iniciar o container:
  ```bash
  docker run -i --rm -p 8080:8080 -e QUARKUS_PROFILE=dev quarkus/app-jvm
  ```

- Acesse o Swagger através do endpoint: http://localhost:8080/swagger-ui/

# Releases

* **SPRINT 1**:
  - MVP: Deploy básico da aplicação;
  - Entendimento de negócio;
  - Requisitos funcionais e não funcionais;
  - Entendimento do usuário.

* **SPRINT 2**:
  - Artigo (Versão 01);
  - Back-end;
  - Arquitetura corporativa;
  - Infraestrutura;
  - Front-end.

* **SPRINT 3**:
  - Modelagem;
  - Relatório técnico;
  - Artigo (Versão 02);
  - Integração entre back-end e front-end.

* **SPRINT 4**:
  - Testes do sistema;
  - Definição da aplicação;
  - Artigo (Versão 03).

* **SPRINT 5**:
  - Aprimoramento dos testes;
  - Artigo completo;
  - Organização do repositório do Github. 

## 📋 Licença/License

<p xmlns:cc="http://creativecommons.org/ns#" xmlns:dct="http://purl.org/dc/terms/"><a property="dct:title" rel="cc:attributionURL" href="https://github.com/Inteli-College/2024-1B-T07-CC06-G04">This Work</a> by <a rel="cc:attributionURL dct:creator" property="cc:attributionName" href="https://www.inteli.edu.br/">Inteli, Ana Luisa Goes Barbosa, Gabriel Coletto Silva, Isabelle Santos, Lucas Nogueira Nunes, Mauro das Chagas Junior, Vitto Mazeto</a> is licensed under <a href="https://creativecommons.org/licenses/by/4.0/?ref=chooser-v1" target="_blank" rel="license noopener noreferrer" style="display:inline-block;">CC BY 4.0<img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/cc.svg?ref=chooser-v1" alt=""><img style="height:22px!important;margin-left:3px;vertical-align:text-bottom;" src="https://mirrors.creativecommons.org/presskit/icons/by.svg?ref=chooser-v1" alt=""></a></p>

</div>