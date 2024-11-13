<div align="justify">

# Vídeos Demonstrativos dos *Back-Ends*

## Quarkus (Java)

- Link para o vídeo: [Vídeo Demonstrativo do Back-End Quarkus](https://www.youtube.com/watch?v=lTgnk1SttJU)

&emsp;Este vídeo demonstra o *back-end* em Quarkus (*framework* de Java) da solução desenvolvida, abordando como a implementação atende aos requisitos delimitados, às boas práticas estabelecidas e aos critérios de entrega dos artefatos. Nele, é explicado como foi aplicada a modularização do código através da generalização de seus principais componentes, assim como também é mostrado como o código foi documentando seguindo o padrão [Javadoc](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html) e como o mesmo foi organizado a nível estrutural e de arquitetura.

&emsp;No vídeo, é mostrado como o código é dividido em *resources* (responsáveis pela comunicação direta por meio de requisições HTTP), *services* (cuidam de lógicas complexas de processamento de dados) e *entities* (modelos de dados que representam as informações armazenadas no banco de dados da aplicação), além dos módulos de tratamento de erros e verificação da saúde da aplicação. Ademais, é explicado como funciona a integração entre *front-end* e *back-end*, que foi feita iterativamente por meio da metodologia de integração contínua, e os recursos criados para popular a base de dados a partir de arquivos CSV e cálculo de dias de entrega de acordo com código de endereçamento postal, funcionalidades significativas da aplicação. Por fim, há um exemplo no vídeo de como é processada internamente uma compra simulada na plataforma de *e-commerce*.

&emsp;Esta documentação audiovisual comprova, portanto, que o *back-end* atende não só aos critérios mapeados para entrega de artefatos como também aos requisitos elicitados anteriormente pelo grupo desenvolvedor da solução. Múltiplas funcionalidades do *front-end* estão integradas a ele, o código é modular e bem documentado, capaz de simular com precisão um ambiente de uma plataforma *e-commerce*, possibilitando a realização de testes de muito valor para a entrega final do projeto.

## Go

- Link para o vídeo: [Vídeo Demonstrativo do Back-End Go](https://www.youtube.com/watch?v=e4rbCK2dD10)

&emsp;Este vídeo demonstra o *back-end* em Go da solução desenvolvida, abordando, assim como o vídeo demonstrativo do Quarkus, como a implementação atende aos requisitos delimitados, às boas práticas estabelecidas e aos critérios de entrega dos artefatos. Neste vídeo, é explicado como foi organizada a estrutura modular do código e como seus componentes interagem entre si, assim como também é mostrado como o código foi documentando seguindo o padrão [Godoc](https://go.dev/blog/godoc).

&emsp;No vídeo, é mostrado como o código é dividido em *handlers* (responsáveis pela comunicação direta por meio de requisições HTTP), *services* (cuidam de lógicas complexas de processamento de dados), *entities* (modelos de dados que representam as informações armazenadas no banco de dados da aplicação) e *payloads* (estruturas de dados para comunicação via requisições HTTP), além dos pacotes de utilidades da aplicação, como o de tratamento de erros e o de geolocalização. Ademais, é explicado como funciona a integração entre *front-end* e *back-end*, que foi feita iterativamente por meio da metodologia de integração contínua, e os recursos criados para popular a base de dados a partir de arquivos CSV e calcular os prazos de entrega de acordo com código de endereçamento postal (CEP).

</div>