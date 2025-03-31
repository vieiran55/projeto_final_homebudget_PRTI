
# HomeBudget API

Esta é uma aplicação desenvolvida com **Spring Boot 3.4.3**.

## Requisitos

- Java 23
- Docker

## Configuração do Banco de Dados

Antes de iniciar a aplicação, é necessário subir o banco de dados utilizando Docker. Execute o seguinte comando na raiz do projeto para subir o banco de dados:

```bash
docker-compose up --build -d
```

Isso irá inicializar o banco de dados de acordo com a configuração presente no arquivo `docker-compose.yml`.

## Como rodar a aplicação

Após configurar o banco de dados, inicie a aplicação Spring Boot com o comando:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível no endereço `http://localhost:8080`.

## Documentação

- `http://localhost:8080/swagger-ui/index.html`: Endpoint de exemplo para testar a aplicação.

## Observações

- Certifique-se de que o banco de dados esteja rodando corretamente antes de iniciar a aplicação.