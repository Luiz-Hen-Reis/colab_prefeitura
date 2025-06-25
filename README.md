# API de Ocorrências Urbanas (Colab Prefeitura) - v1.0.0

![Badge](https://img.shields.io/badge/Java-Spring%20Boot%20%7C%20Spring%20Security-success)
![Badge](https://img.shields.io/badge/OAS-3.1-blue)
![Badge](https://img.shields.io/badge/PostgreSQL-Docker%20Compose-informational)
![Badge](https://img.shields.io/badge/RabbitMQ-Message%20Broker-important)

API REST para gerenciamento de ocorrências urbanas, permitindo que cidadãos reportem problemas como buracos, iluminação pública, coleta de lixo e outros serviços urbanos.

## 📌 Visão Geral

- **Tecnologias**: Java Spring Boot, Spring Security, JWT, PostgreSQL, RabbitMQ
- **Documentação**: [OpenAPI 3.1](http://localhost:8080/v3/api-docs) | [Swagger UI](http://localhost:8080/swagger-ui.html)
- **Servidor**: `http://localhost:8080` (Ambiente de desenvolvimento)
- **Testes Unitários**:  API testada com testes unitários usando JUnit e Mockito

## 🚀 Funcionalidades

### 👨‍💻 Administração (ROLE_ADMIN)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| PATCH  | `/admin/occurrences/{occurrenceId}/status` | Atualizar status de uma ocorrência |
| PATCH  | `/admin/occurrences/{occurrenceId}/priority` | Atualizar prioridade de uma ocorrência |
| GET    | `/admin/occurrences` | Listar todas as ocorrências |

### 🏙️ Ocorrências Urbanas (ROLE_USER)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/occurrences` | Criar nova ocorrência (sem imagem) |
| POST   | `/occurrences/{id}/image` | Upload de imagem para ocorrência |
| GET    | `/occurrences/me` | Listar ocorrências do usuário autenticado |

### 🔐 Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/auth/sessions` | Login (JWT) |
| POST   | `/auth/register` | Registrar novo usuário |

## 🔄 Fluxo de Prioridades

RabbitMQ é utilizado para classificar automaticamente a prioridade das ocorrências baseado em heurísticas simples.

## 🛠️ Configuração

### Pré-requisitos
- Docker e Docker Compose
- Java 17+
- Maven

## ▶️ Iniciar Aplicação

1. **Subir containers**:
   ```bash
   docker-compose up -d

2. Execute o seguinte comando para iniciar a aplicação Spring Boot:

  ```bash
  mvn spring-boot:run

