# 🏙️ API de Ocorrências Urbanas – Colab Prefeitura

**Versão:** 1.0.0  
**Documentação OpenAPI:** [Swagger UI](http://localhost:8080/swagger-ui.html) | [Spec JSON](http://localhost:8080/v3/api-docs)

API REST desenvolvida com **Java Spring Boot** para o gerenciamento de ocorrências urbanas, permitindo que cidadãos reportem problemas como buracos nas ruas, falhas na iluminação pública, coleta de lixo e outros serviços municipais.

---

## 🚀 Funcionalidades

- Registro e autenticação de usuários (com JWT)
- Criação e consulta de ocorrências urbanas
- Upload de imagens vinculadas às ocorrências
- Listagem das ocorrências de um usuário autenticado
- Endpoints administrativos para alterar **status** e **prioridade**
- Sistema de classificação de prioridade assíncrono via **RabbitMQ**
- Controles de acesso baseados em **roles** (`USER` e `ADMIN`)
- Testes unitários e ambiente isolado com Docker Compose

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Spring Validation
- PostgreSQL (via Docker)
- RabbitMQ (mensageria assíncrona via Docker)
- JWT para autenticação
- JUnit + Mockito para testes
- OpenAPI 3.1 (Swagger)

---

## 🐳 Docker

### Serviços disponíveis via Docker Compose:

- PostgreSQL (porta `5432`)
- RabbitMQ
  - Porta: `5672`
  - Painel de administração: [http://localhost:15672](http://localhost:15672)
  - Login: `guest` / `guest`

```bash
docker-compose up -d
```
