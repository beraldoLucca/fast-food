# Fast food

O projeto gerencia o fluxo de pedidos de uma lanchonete.

## Sumário

- [Descrição](#descrição)
- [Instalação](#instalação)
- [Uso](#uso)
- [Endpoints de Testes](#endpoints-de-testes)
- [Autores e Reconhecimentos](#autores-e-reconhecimentos)

## Descrição

O projeto consegue cadastrar novos clientes(a partir do seu CPF, nome e email),
e identificar um cliente a partir do seu CPF.
Consegue também criar, editar e remover produtos, além de buscar um produto
atraveś de uma categoria específica.
O projeto possui o fake checkout, um endpoint que finaliza os pedidos.
É possível também listar todos os pedidos.

## Instalação e Uso

Instruções para instalação do projeto.

```bash
# Clone o repositório
git clone https://github.com/beraldoLucca/fast-food.git

# Navegue até o diretório do projeto
cd fast-food

# Suba o arquivo docker-compose para rodar a aplicação
docker-compose up --build

# Depois da aplicação estar rodando, acesse a URL do Swagger
http://localhost:8080/api
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
    "image": "imageCoxinha.html"}
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
    "image": "imageCoxinha.html"}
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
    [{"id": 1,
    "name": "Coxinha",
    "category": "LANCHE",
    "price": 5.90,
    "description": "Salgado frito recheado com frango",
    "image": "example.html"}]

-   ### Solicitar um novo pedido
    - #### URL: /api/v1/demand
    - #### Método: `POST`
    - #### Cabeçalho(no primeiro exemplo o Cliente se identificou através do CPF e no segundo não):
- 
  {"customer":  {
  "cpf":"12345678910",
  "name": "Fiap da silva",
  "email":"fiap@gmail.com"
  },
- 
    {"productsId": [1,2],
    "time": 15},
    {"productsId": [3,4],
    "time": 13}
    - #### Resposta:
            Pedido solicitado com sucesso!

-   ### Finalizar um produto/checkout
    - #### URL: /api/v1/demand/{id}
        - exemplo: api/v1/demand/1
    - #### Método: `PUT`
    - #### Resposta:
            Pedido finalizado com sucesso!

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


## Autores e Reconhecimentos

Lucca Beraldo

