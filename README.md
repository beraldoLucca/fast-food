# Fast food

O projeto gerencia o fluxo de pedidos de uma lanchonete.

## Sumário

- [Descrição](#descrição)
- [Instalação](#instalação)
- [Uso](#uso)
- [Endpoints de Testes](#endpoints-de-testes)
- [Infra](#infra)
- [Novos requisitos de negócio](#novos-requisitos-de-negócio)
- [Desenho dos requisitos de infraestrutura](#desenho-dos-requisitos-de-infraestrutura)
- [Autores e Reconhecimentos](#autores-e-reconhecimentos)

## Descrição

O projeto tem por objetivo ajudar uma lanchonete em processo de expansão, 
provendo um sistema de autoatendimento de fast-food.

O projeto consegue cadastrar novos clientes(a partir do seu CPF, nome e email),
e identificar um cliente a partir do seu CPF.
Consegue também criar, editar e remover produtos, além de buscar um produto
atraveś de uma categoria específica.
O projeto possui um endpoint para criar um novo pedido e o fake checkout,
um endpoint que finaliza os pedidos.
É possível também listar todos os pedidos.

## Instalação e Uso

Instruções para instalação do projeto.

```bash
# Clone o repositório
git clone https://github.com/beraldoLucca/fast-food.git

# Navegue até o diretório do projeto
cd fast-food

# Empacote o projeto, usando o comando abaixo, para que seja gerado a versão:
mvn package

# Execute o comando abaixo para rodar a aplicação:
docker-compose up --build

# Depois da aplicação estar rodando, acesse a URL do Swagger:
http://localhost:8080/api

# Para ver o documento de open-api, acesse a URL:
http://localhost:8080/api-docs
```

## Endpoints de Testes

### Introdução
Aqui estão os endpoints da API que você pode usar para testar as funcionalidades.


### Endpoints principais:

-   ### Cadastrar um novo cliente
    - #### URL: /api/v1/customer
    - #### Método: `POST`
    - #### Cabeçalho:
    {"cpf": "12345678910",
      "name": Fiap da Silva,
      "email": fiap@gmail.com}
    - #### Resposta:
            Cliente cadastrado com sucesso!

-   ### Identificação do Cliente via CPF
    - #### URL: /api/v1/customer/{cpf}
      - exemplo: api/v1/customer/12345678910
    - #### Método: `GET`
    - #### Resposta: 
          Cliente validado com sucesso!

-   ### Cadastrar um novo produto
    - #### URL: /api/v1/product
    - #### Método: `POST`
    - #### Cabeçalho:
    {"name": "Coxinha",
    "categoryId": 1,
    "price": 5.90,
    "description": "Salgado frito recheado com frango",
    "image": "imageCoxinha.png"}
    - #### Resposta:
            Produto cadastrado com sucesso!

-   ### Editar um produto
    - #### URL: /api/v1/product/{id}
        - exemplo: api/v1/product/1
    - #### Método: `PUT`
    - #### Cabeçalho:
    {"name": "Coxinha",
    "categoryId": 1,
    "price": 5.90,
    "description": "Salgado frito recheado com frango",
    "image": "imageCoxinha.png"}
    - #### Resposta:
            Produto atualizado com sucesso!

-   ### Remover um produto
    - #### URL: /api/v1/product/{id}
        - exemplo: api/v1/product/1
    - #### Método: `DELETE`
    - #### Resposta:
            Produto excluido com sucesso!

-   ### Buscar produtos por categoria
    - #### URL: /api/v1/product/category/{id}
        - exemplo: api/v1/product/category/1
    - #### Método: `GET`
    - #### Resposta:
    {"id": 1,
    "name": "Coxinha",
    "category": "LANCHE",
    "price": 5.90,
    "description": "Salgado frito recheado com frango",
    "image": "example.png"}

-   ### Solicitar um novo pedido
    - #### URL: /api/v1/demand
    - #### Método: `POST`
    - #### Cabeçalho(no primeiro exemplo o Cliente se identificou através do CPF e no segundo não):
- 1° exemplo -
  {"customer":  {
  "cpf":"12345678910",
  "name": "Fiap da silva",
  "email":"fiap@gmail.com"
  }, {"productsId": [1,2]},
- 2° exemplo -
    {"productsId": [3,4]}
    - #### Resposta:
            Pedido solicitado com sucesso!

-   ### Finalizar um produto/Fake checkout
    - #### URL: /api/v1/demand/{id}
        - exemplo: api/v1/demand/1
    - #### Método: `PUT`
    - #### Resposta:
            Pedido: 1 finalizado com sucesso!

-   ### Listar todos os pedidos
    - #### URL: /api/v1/demands
    - #### Método: `GET`
    - #### Resposta:
- 
  ["customer": {
  "cpf": {
  "cpf": "456.789.098.93"
  },
  "name": "Lucca",
  "email": "lucca@gmail.com"
  },
  "products": [
  {
  "name": "Refrigerante",
  "price": 6.00,
  "description": "Refrigerante gelado"
  },
  {
  "name": "Coxinha",
  "price": 5.90,
  "description": "Salgado frito recheado com frango"
  }
  ],
  "time": 15.0,
  "status": "RECEBIDO"
  }]

-   ### Consultar status de pagamento de um pedido
    - #### URL: /api/v1/demand/{id}/payment-status
        - exemplo: api/v1/demand/1/payment-status
    - #### Método: `GET`
    - #### Resposta:
            Pedido: 5, status de pagamento: EM_ANDAMENTO

-   ### Gerar assinatura para pagamento webhook
    - #### URL: /api/v1/demand/{id}/generate-signature
        - exemplo: api/v1/demand/1/generate-signature
    - #### Método: `GET`
    - #### Resposta:
            Assinatura gerada: JF2OYRyhq3plXxFG9Xlmk+HETDcycrSBpy/7nSAu0Ws=

  -   ### Webhook para confirmar pagamento do pedido
      - #### URL: /api/v1/demand/webhook/payment
      - #### Método: `POST`
      No headers, no campo Authorization, é necessário colocar a assinatura gerada no método anterior: JF2OYRyhq3plXxFG9Xlmk+HETDcycrSBpy/7nSAu0Ws=
  - Payload:
      {
         "event": "payment_approved",
         "data": {
            "order_id": 1
         }
      }
    - #### Resposta:
              Pagamento aprovado com sucesso.


## Infra

Na parte de infraestrutura, utilizei o EKS da AWS para gerenciamento dos containers.
Com a ultilização do helm, usei o deployment para o deploy da imagem da aplicação, que
está hospedada no docker hub, e conexão com o banco através das variáveis de URL, user e senha.
Utilizei o HPA para o autoscaling automático, com o minimo de 1 réplica e o máximo de 10,
com o limite de 30% de uso de uma aplicação para que uma outra seja escalada.
Para conectar na app, estou usando um service.
Na parte de banco, subi um pod da imagem do banco de dados(PostgreSQL), e um service
para acessar a imagem. Usei um arquivo de secret para gerenciar os dados sensíveis
como a senha do banco.
Por fim, usei um configMap para gerenciar a URL e usuário do banco.
Para consumir a app na AWS, o usuário bate em uma ALB que chama o EKS que faz consultas
ou persistências no banco. Isso tudo(desde o ALB até o banco) está dentro de uma VPC.


## Novos requisitos de negócio

#### Foram modificados os seguintes endpoints:
    - Finalização do pedido: @PutMapping("/demand/{id}")
    - Listar todos os pedidos: @GetMapping("/demands")
#### Foram implementados os seguintes endpoints:
    - Consultar status de pagamento: @GetMapping("/demand/{id}/payment-status")
    - Geração do hash da assinatura: @GetMapping("/demand/{id}/generate-signature")
    - Webhook para confirmação de pagamento: @PostMapping("/demand/webhook/payment")

## Desenho dos requisitos de infraestrutura
[architecture design.pdf](..%2F..%2F..%2FDownloads%2Farchitecture%20design.pdf)

## Autores e Reconhecimentos

Lucca Beraldo

