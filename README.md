# Sonara 🎵

[![Sonara CI/CD Pipeline](https://github.com/benjaexz/Sonara/actions/workflows/ci.yml/badge.svg)](https://github.com/benjaexz/Sonara/actions/workflows/ci.yml)

O **Sonara** é uma plataforma completa de transmissão e gestão musical, desenvolvida com arquitetura desacoplada e padrões corporativos de engenharia de software: cliente web moderno em **Angular**, API RESTful robusta em **Java 21 / Spring Boot 3**, persistência relacional com **PostgreSQL**, esteira automatizada de **CI/CD via GitHub Actions**, orquestração completa em **Docker** e deploy automatizado na nuvem.

---

## 📸 Screenshots

| Login | Músicas Curtidas |
|---|---|
| ![Login](Screenshots/login.png) | ![Músicas Curtidas](Screenshots/Musicascurtidas.png) |

---

## 📑 Índice

- [Screenshots](#-screenshots)
- [Arquitetura da Solução](#-arquitetura-da-solução)
- [Funcionalidades](#-funcionalidades)
- [Stack Tecnológica Completa](#-stack-tecnológica-completa)
- [Estrutura do Repositório](#-estrutura-do-repositório)
- [Documentação da API (Swagger UI)](#-documentação-da-api-swagger-ui)
- [Testes Automatizados](#-testes-automatizados)
- [Pipeline de CI/CD](#-pipeline-de-cicd)
- [Configuração de Variáveis de Ambiente](#-configuração-de-variáveis-de-ambiente)
- [Como Executar o Projeto](#-como-executar-o-projeto)
  * [1. Execução Completa com Docker Compose (Recomendado)](#1-execução-completa-com-docker-compose-recomendado)
  * [2. Execução Manual (Desenvolvimento Local)](#2-execução-manual-desenvolvimento-local)
- [Implementação e Deploy](#-implementação-e-deploy)
- [Licença](#-licença)

---

## 🏛️ Arquitetura da Solução

O projeto está estruturado em monorepo contendo dois módulos principais e totalmente desacoplados:

- **Frontend (SPA):** Interface reativa em Angular com TypeScript, consumindo a API REST autenticada via cabeçalhos HTTP (`Authorization: Bearer <token>`). Em produção, servida por servidor Nginx otimizado.
- **Backend (API RESTful):** Desenvolvido em Spring Boot com Spring Security, gestão de sessões stateless via JWT, validação rigorosa de dados e arquitetura em camadas (`Controller`, `Service`, `Repository`, `DTO`).
- **Banco de Dados Relacional:** PostgreSQL com integridade referencial, gerido via Spring Data JPA e Hibernate, com mapeamentos N:N e 1:N estruturados (playlists, faixas, avaliações e favoritos).

---

## ✨ Funcionalidades

- **Autenticação & Segurança:** Registo e login de utilizadores com hash BCrypt e emissão de tokens JWT seguros.
- **Catálogo de Áudio:** Exploração e reprodução de faixas, artistas e álbuns.
- **Gestão de Playlists:** Criação, edição, exclusão e reordenação de faixas com validação rigorosa de posse (*ownership*).
- **Favoritos:** Marcação e gestão de faixas prediletas pelo utilizador autenticado.
- **Classificações (Ratings):** Sistema de pontuação e avaliação de faixas de 1 a 5 estrelas.
- **Tratamento de Exceções Centralizado:** Respostas de erro padronizadas (`@RestControllerAdvice`) para status 400, 401, 403, 404 e 409.

---

## 🛠️ Stack Tecnológica Completa

| Camada | Tecnologia | Detalhes |
| --- | --- | --- |
| **Frontend** | Angular + TypeScript | Aplicação Single Page conteinerizada com Nginx |
| **Backend** | Java 21 & Spring Boot 3.5.14 | API RESTful com ciclo de vida gerenciado via Maven Wrapper |
| **Segurança** | Spring Security + JJWT 0.12.6 | Controle de acesso granular e tokens JWT stateless |
| **Base de Dados** | PostgreSQL 16 | Armazenamento relacional e integridade referencial |
| **ORM** | Spring Data JPA / Hibernate | Persistência desacoplada e consultas otimizadas |
| **Documentação** | SpringDoc OpenAPI 2.8.5 | Interface interativa (Swagger UI 3.1) |
| **Qualidade & Testes** | JUnit 5 + Mockito + MockMvc | 33 testes automatizados (unitários, integração web e segurança) |
| **DevOps** | Docker / Docker Compose | Multi-stage builds (Alpine), rede interna e healthcheck |
| **CI/CD** | GitHub Actions | Esteira automatizada de build, testes com service container e deploy contínuo |
| **Hospedagem** | Vercel (Frontend) & Render (Backend) | Deploy automatizado acionado via webhook e integração contínua |

---

## 📁 Estrutura do Repositório

```text
Sonara/
├── backend/                       # API REST em Spring Boot
│   ├── src/
│   │   ├── main/java/io/sonara/
│   │   │   ├── config/            # Configurações de Segurança, CORS e OpenAPI/Swagger
│   │   │   ├── controller/        # Controladores REST da API
│   │   │   ├── dto/               # Data Transfer Objects (DTOs)
│   │   │   ├── exception/         # Tratamento global de exceções
│   │   │   ├── model/             # Entidades relacionais JPA
│   │   │   ├── repository/        # Repositórios Spring Data JPA
│   │   │   ├── security/          # Filtros JWT e UserDetailsService
│   │   │   └── service/           # Regras de negócio e validações
│   │   └── test/                  # Suíte com 33 testes automatizados
│   ├── Dockerfile                 # Multi-stage build (JDK 21 compile -> JRE 21 runtime)
│   └── pom.xml                    # Gerenciador de dependências Maven
│
├── frontend/                      # Aplicação web SPA
│   ├── src/                       # Componentes, serviços, rotas e estilos Angular
│   ├── Dockerfile                 # Multi-stage build (Node 20 compile -> Nginx runtime)
│   ├── nginx.conf                 # Configuração de roteamento SPA e servidor web
│   ├── package.json               # Dependências do ecossistema Node/Angular
│   └── angular.json               # Configurações de compilação da interface
│
├── .github/workflows/
│   └── ci.yml                     # Pipeline automatizada de CI/CD
├── docker-compose.yml             # Orquestração local dos 3 serviços (DB, API, Front)
├── .env.example                   # Modelo de variáveis de ambiente
└── README.md                      # Documentação geral do ecossistema
📖 Documentação da API (Swagger UI)
A API conta com documentação interativa baseada na especificação OpenAPI 3:

Swagger UI (Local): http://localhost:8080/swagger-ui/index.html

Swagger UI (Produção): https://sonara-backend-kh00.onrender.com/swagger-ui/index.html

OpenAPI Spec (JSON): http://localhost:8080/v3/api-docs

Como autenticar no Swagger:

Execute a requisição em POST /auth/login para obter o token JWT.

Clique no botão Authorize no canto superior direito.

Insira o token gerado para liberar a execução dos endpoints protegidos.

🧪 Testes Automatizados
A estabilidade e integridade da aplicação são garantidas por 33 testes automatizados:

Camada de Serviço (Unitários com Mockito): Isolamento total para validação de regras de negócio, permissões de playlist e regras de avaliação de faixas.

Camada Web (Integração com MockMvc): Validação de códigos de retorno HTTP (200, 201, 204, 400, 401, 403, 404, 409), serialização/desserialização JSON e tratamento de exceções.

Camada de Segurança: Testes dos filtros JWT assegurando bloqueio de acessos anônimos a rotas protegidas e liberação das rotas públicas.

Para rodar os testes localmente:

Bash
cd backend
./mvnw clean verify
🔄 Pipeline de CI/CD
O repositório possui uma esteira automatizada no GitHub Actions (.github/workflows/ci.yml):

Continuous Integration (CI):

Provisiona uma máquina virtual Ubuntu limpa.

Sobe um container de serviço com PostgreSQL 16 e aguarda o healthcheck (pg_isready).

Configura o runtime Java 21 (Eclipse Temurin) com cache de dependências Maven.

Compila o código e executa todos os 33 testes automatizados.

Continuous Deployment (CD):

Caso todos os testes sejam concluídos com sucesso na branch main, o job de deploy é acionado.

Executa uma chamada segura ao Deploy Hook do Render via webhook secreto (RENDER_DEPLOY_HOOK), atualizando a API em produção sem intervenção manual.

⚙️ Configuração de Variáveis de Ambiente
Crie um arquivo .env na raiz do projeto para execução com Docker Compose:

Snippet de código
# Banco de Dados
POSTGRES_DB=sonaradb
POSTGRES_USER=sonara_user
POSTGRES_PASSWORD=sua_senha_segura
PORT_DB=5432

# Frontend
PORT_FRONTEND=8080

# Backend / JWT (backend/.env ou variáveis de ambiente)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sonaradb
SPRING_DATASOURCE_USERNAME=sonara_user
SPRING_DATASOURCE_PASSWORD=sua_senha_segura
JWT_SECRET=chave_secreta_jwt_longa_e_aleatoria_com_mais_de_256_bits
JWT_EXPIRATION=86400000
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:8080,[https://sonara-amber.vercel.app](https://sonara-amber.vercel.app)
🚀 Como Executar o Projeto
1. Execução Completa com Docker Compose (Recomendado)
Suba todo o ecossistema (PostgreSQL, Spring Boot API e Angular com Nginx) com um único comando a partir da raiz:

Bash
# Construir as imagens e iniciar os contêineres em segundo plano
docker compose up -d --build

# Inspecionar os logs de todos os serviços em tempo real
docker compose logs -f

# Acompanhar os logs apenas do backend
docker compose logs -f backend

# Parar todos os serviços
docker compose down
A aplicação web estará pronta em http://localhost:8080.

2. Execução Manual (Desenvolvimento Local)
Executar o Backend
Certifique-se de possuir o Java 21 e o PostgreSQL em execução:

Bash
cd backend
./mvnw clean spring-boot:run
A API ficará disponível em http://localhost:8080.

Executar o Frontend
Certifique-se de possuir o Node.js (v18+) instalado:

Bash
cd frontend
npm install
npm start
A interface web de desenvolvimento ficará acessível em http://localhost:4200.

🌐 Implementação e Deploy
Frontend: Hospedado e sincronizado via Vercel: sonara-amber.vercel.app

Backend: Conteinerizado e hospedado no Render com banco PostgreSQL gerenciado: sonara-backend-kh00.onrender.com

⚠️ Aviso sobre o plano gratuito do Render: O backend entra em estado de hibernação após períodos de inatividade. A primeira requisição pode levar cerca de 50 segundos para inicializar a instância.

📄 Licença
Este projeto está sob a licença MIT. Desenvolvido por Jacó Lima.
