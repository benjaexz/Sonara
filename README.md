# Sonara — Plataforma de música Full-Stack

> Plataforma web de streaming e gerenciamento musical inspirado no design minimalista de interfaces modernas (Apple Music glassmorphism), construída com arquitetura desacoplada utilizando **Angular 19+** e **Spring Boot 3 (Java 21)**.

?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

Sonara — Backend APIBackend RESTful para a plataforma de streaming e gerenciamento musical Sonara, arquitetado com foco em segurança, persistência relacional, desacoplamento em camadas e cobertura abrangente de testes automatizados.📑 SumárioVisão GeralStack TecnológicaArquitetura do ProjetoEndpoints PrincipaisDocumentação Interativa (Swagger)Suíte de TestesConfiguração de AmbienteComo ExecutarCom Docker ComposeLocalmente com MavenLicença🎯 Visão GeralA API do Sonara fornece a infraestrutura de serviços para reprodução e organização do ecossistema sonoro da plataforma, contemplando:Autenticação e autorização stateless via JWT (JSON Web Tokens).Gerenciamento de biblioteca pessoal: Playlists, Favoritos e Avaliações (Ratings 1-5).Catálogo musical contendo Artistas, Álbuns, Faixas e metadados.Tratamento global padronizado de exceções (GlobalExceptionHandler).Política de CORS parametrizada para ambientes de desenvolvimento e produção (Vercel).🛠️ Stack TecnológicaComponenteTecnologiaVersão / DetalhesLinguagemJava21 (LTS)FrameworkSpring Boot3.5.14SegurançaSpring Security + JJWTAutenticação Stateless via Bearer TokensPersistênciaSpring Data JPA / HibernateORM com mapeamentos relacionaisBanco de DadosPostgreSQLDialeto relacional robustoDocumentaçãoSpringDoc OpenAPIOpenAPI 3.1 & Swagger UI 2.8.5TestesJUnit 5 + Mockito + MockMvcSuíte unitária, web e de segurançaDevOpsDocker & Docker ComposeImagens multi-stage para deploy enxuto🏛️ Arquitetura do ProjetoA organização de pacotes segue o padrão em camadas desacopladas:Plaintextio.sonara/
├── config/             # Configurações globais (Segurança, CORS, OpenAPI)
├── controller/         # Camada Web / REST Controllers
├── dto/                # Data Transfer Objects (Requests/Responses)
├── exception/          # Handlers globais e exceções de domínio
├── model/              # Entidades JPA (User, Track, Playlist, Favorite, etc.)
├── repository/         # Interfaces Spring Data JPA
├── security/           # Filtro JWT, UserDetailsService e validação de tokens
└── service/            # Regras de negócio, transações e validações de ownership
📡 Endpoints PrincipaisMóduloMétodoEndpointProtegido?DescriçãoAuthPOST/auth/login❌Gera o token JWT para o usuárioAuthPOST/auth/register❌Cadastro de novas contasPlaylistsGET/playlists✅Lista playlists públicas/pessoaisPlaylistsPOST/playlists✅Cria uma nova playlistPlaylistsPOST/playlists/{id}/tracks✅Adiciona uma faixa à playlistPlaylistsDELETE/playlists/{id}✅Remove playlist (validação de dono)FavoritosGET/favorites✅Retorna músicas favoritadas pelo usuárioFavoritosPOST/favorites/{trackId}✅Adiciona faixa aos favoritosFavoritosDELETE/favorites/{trackId}✅Remove faixa dos favoritosAvaliaçõesPOST/ratings✅Avalia faixa com nota (1 a 5)FaixasGET/faixas/{id}✅Detalhes e streaming de metadados📖 Documentação Interativa (Swagger)A API disponibiliza documentação interativa através do Swagger UI com suporte total a autenticação Bearer Token:Swagger UI: http://localhost:8080/swagger-ui/index.html (ou na URL pública da sua API)OpenAPI Spec (JSON): http://localhost:8080/v3/api-docsComo testar endpoints protegidos no Swagger:Realize a chamada de autenticação em POST /auth/login e copie o token gerado.Clique no botão verde Authorize no topo direito da tela.No campo de valor, cole o token JWT e confirme. As requisições passarão a incluir o header Authorization: Bearer <token>.🧪 Suíte de TestesA integridade do código é assegurada por 33 testes automatizados distribuídos entre as camadas críticas da aplicação:Regras de Negócio (io.sonara.service): Testes unitários com JUnit 5 e Mockito, validando ordenação sequencial de faixas, integridade referencial, bloqueio de notas fora da faixa 1-5 e ownership de playlists.Camada Web (io.sonara.controller): Testes de integração leves com @WebMvcTest e MockMvc, garantindo status HTTP (200, 201, 204, 404, 409), parsing correto de payloads e tratamento via GlobalExceptionHandler.Filtros de Segurança (io.sonara.security): Validação de bloqueio de acessos não autorizados em rotas privadas (401/403) e garantia de rotas públicas liberadas.Para executar todos os testes localmente:Bash./mvnw test
⚙️ Configuração de AmbienteCrie um arquivo .env na raiz do projeto (ou configure as variáveis no seu ambiente de nuvem/Docker):Snippet de código# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sonara_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JPA / Hibernate
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# Segurança / JWT
JWT_SECRET=sua_chave_secreta_jwt_longa_e_aleatoria_com_mais_de_256_bits
JWT_EXPIRATION=86400000

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200,https://sonara-amber.vercel.app
🚀 Como Executar1. Via Docker Compose (Recomendado)Sobe o banco PostgreSQL e a aplicação com uma única instrução:Bash# Constrói e inicializa os containers em segundo plano
docker compose up -d --build

# Acompanhar logs da API
docker compose logs -f backend

# Encerrar os serviços
docker compose down
2. Execução Local com MavenCertifique-se de ter o PostgreSQL rodando localmente na porta configurada:Bash# Compilar e rodar a aplicação
./mvnw spring-boot:run
A API estará disponível em http://localhost:8080.📄 LicençaEste projeto está sob a licença MIT. Desenvolvido por Jacó Lima.
