# Sonara — Plataforma de música Full-Stack

> Plataforma web de streaming e gerenciamento musical inspirado no design minimalista de interfaces modernas (Apple Music glassmorphism), construída com arquitetura desacoplada utilizando **Angular 19+** e **Spring Boot 3 (Java 21)**.

[![Demonstração ao vivo](https://img.shields.io/badge/Demo-Vercel-black?style=for-the-badge&logo=vercel)](https://sonara-amber.vercel.app)
[![Backend](https://img.shields.io/badge/API-Render-46E3B7?style=for-the-badge&logo=render)](https://sonara-backend-kh00.onrender.com)
[![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Bota de mola](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular_19-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

---

## 🚀 Demonstração ao vivo e acesso rápido

- **Frontend (SPA):** [https://sonara-amber.vercel.app](https://sonara-amber.vercel.app)
- **API Swagger / OpenAPI:** [https://sonara-backend-kh00.onrender.com/swagger-ui/index.html](https://sonara-backend-kh00.onrender.com/swagger-ui/index.html)

> 💡 **Uso de Teste (Recrutadores / Demonstração):**
> Se preferir não cadastrar um novo usuário, utilize como credenciais de homologação:
> - **E-mail:** `demo@sonara.io`
> - **Senha:** `Sonara@123`

---

## 🏛️ Visão Geral da Arquitetura

O Sonara adota o padrão de **arquitetura em camadas desacopladas**, dividida em um cliente Single Page Application (SPA), uma API RESTful stateless e um banco de dados relacional.

```texto
               Navegador / Cliente
                       │
             HTTPS (Bearer JWT)
                       ▼
          ┌─────────────────────────┐
          │   Angular Frontend      │  (Vercel SPA)
          │  Zone.js + HttpGuard    │
          └────────────┬────────────┘
                       │ REST / JSON
                       ▼
          ┌─────────────────────────┐
          │   Spring Boot 3 API     │  (Render Container)
          │  Security + JWT Filter  │
          └────────────┬────────────┘
                       │ Spring Data JPA / JDBC
                       ▼
          ┌─────────────────────────┐
          │   PostgreSQL Database   │  (Supabase / Cloud SQL)
          └─────────────────────────┘

```

---

## ⚙️ Decisões de Engenharia & Destaques Técnicos

1. **Autenticação Stateless com JWT & BCrypt:**
* Senhas criptografadas com `BCryptPasswordEncoder` no cadastro.
* Emissão e validação de tokens JWT (`io.jsonwebtoken`) no fluxo de login.
* Camada de segurança personalizada com `JwtAuthenticationFilter` estendendo `OncePerRequestFilter` e populando o `SecurityContextHolder`.


2. **Frontend Interceptors & Proteção de Rotas:**
* **`authInterceptor`**: Intercepta dinamicamente requisições HTTP e injeta o header `Authorization: Bearer <TOKEN>` para rotas autenticadas.
* **`authGuard` (`CanActivateFn`)**: Impede o carregamento de views protegidas (`/`, `/favorites`, `/playlists`, `/history`), redirecionando acessos não autorizados para `/login`.


3. **Ciclo de Estabilidade Angular & Zone.js:**
* Configuração de polyfill `zone.js` no bootstrap da aplicação para evitar discrepâncias em loops de renderização assíncronos (`NG0908`).
* Gerenciamento reativo de estado de autenticação via `BehaviorSubject` do RxJS, garantindo sincronia imediata entre o header, sidebar e player.


4. **Tratamento de Lazy Loading e DTOs:**
* Implementação de DTOs (`TrackResponseDTO`, `AlbumResponseDTO`, etc.) desacoplados das entidades JPA para evitar `LazyInitializationException` e loops infinitos de serialização bidirecional Jackson.
* `GlobalExceptionHandler` centralizado (`@RestControllerAdvice`) tratando `ResourceNotFoundException`, `DuplicateResourceException` e violações de Bean Validation com respostas padronizadas (`ErrorResponse`).


5. **Infraestrutura & CORS Multi-Ambiente:**
* Configuração dinâmica de políticas de CORS (`CorsConfigurationSource`) permitindo requisições controladas tanto de ambientes locais (`http://localhost:4200`) quanto do domínio de produção (`https://sonara-amber.vercel.app`).



---

## 📦 Modelo de Domínio

O ecossistema modela o catálogo e recursos exclusivos de usuário:

* **Catálogo:** `Artist` ➔ `Album` ➔ `Track` ➔ `Genre`
* **Usuário:** `User` com senhas em hash e username único
* **Músicas & Playlists:** `Playlist` ➔ `PlaylistTrack` (com posição indexada)
* **Engajamento:** `Favorite` (com restrição única por usuário/faixa), `ListeningHistory`, `TrackRating` (1 a 5) e `TrackComment`.

---

## 🛠️ Stack Tecnológica

| Camada | Tecnologias |
| --- | --- |
| **Frontend** | Angular 19, TypeScript, RxJS, Zone.js, HTML5 / CSS3 Moderno |
| **Backend** | Java 21, Spring Boot 3, Spring Security, Spring Data JPA / Hibernate |
| **Banco de Dados** | PostgreSQL |
| **Segurança** | JWT (JSON Web Tokens), BCrypt |
| **Documentação** | Swagger / OpenAPI 3 |
| **DevOps & Deploy** | Docker, Docker Compose, Git, Vercel, Render |

---

## 💻 Executando Localmente

### Pré-requisitos

* [Docker & Docker Compose](https://www.docker.com/) instalados **OU**
* Java 21 SDK + Node.js 20+ + PostgreSQL local.

### 1. Clonar o Repositório

```bash
git clone [https://github.com/benjaexz/Sonara.git](https://github.com/benjaexz/Sonara.git)
cd Sonara

```

### 2. Rodando via Docker Compose (Recomendado)

```bash
docker-compose up -d --build

```

* Frontend acessível em: `http://localhost:4200`
* Backend API em: `http://localhost:8080`

### 3. Rodando Manualmente

#### Backend:

```bash
cd backend
# Configure suas variáveis no application.properties ou passe por ambiente:
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sonara
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export JWT_SECRET=seuSecretSuperSeguroComMaisDe256BitsAqui12345

./mvnw spring-boot:run

```

#### Frontend:

```bash
cd frontend
npm install
npm start
# Aplicação disponível em http://localhost:4200

```

---

## 🗺️ Roadmap de Evolução

* [x] Autenticação Stateless via JWT + BCrypt
* [x] Catálogo Musical completo com CRUDs de Artistas, Álbuns e Faixas
* [x] Interceptor Angular & AuthGuard de proteção de rotas
* [x] Player persistente integrado ao catálogo
* [x] Gestão de Favoritos e Playlists
* [x] Deploy Contínuo (Vercel + Render)
* [ ] Interface visual para avaliações (1 a 5 estrelas) e comentários de faixas
* [ ] Fila de reprodução avançada com Shuffle / Repeat
* [ ] Pipeline de CI automatizada com GitHub Actions

---

## 👤 Autor

Desenvolvido por **Jaco Lima**

* **GitHub:** [@benjaexz](https://www.google.com/url?sa=E&source=gmail&q=https://github.com/benjaexz)
