Sonara é um projeto de backend para um catálogo de música e sistema de comportamento do usuário, inspirado em plataformas como Apple Music e Spotify.

O objetivo é construir um sistema backend real usando Java, Spring Boot, PostgreSQL, APIs REST, arquitetura em câmeras, validação, tratamento de exceções e modelagem de domínio relacional.

---

# Pilha de tecnologia

- Java 21
- Bota de mola
- Dados de primavera JPA
- Hibernar
- PostgreSQL
- Especialista
- Validação de Bean
- API REST
- UUID

---

# Características Atuais

## Módulo de gênero
- Criar gêneros
- Listar gêneros
- Proteção duplicada
- Validação

## Módulo Artista
- Criar artistas
- Listar artistas
- Proteção duplicada
- Validação

## Módulo Álbum
- Criar álbuns
- Listar álbuns
- Relacionamento com Artista
- Validação

## Tratamento de exceções globais
- Respostas de erro padronizadas
- Manipulação duplicada de recursos
- Tratamento de recursos não encontrados
- Tratamento de erros de validação

---

# Arquitetura

O projeto segue uma arquitetura de backend em camadas:

```texto
Cliente
  ↓
Controlador
  ↓
Serviço
  ↓
Repositório
  ↓
PostgreSQL
```

Main packages:

```txt
controller
service
repository
entity
dto
exception
```

---

# Domain Model - Current Stage

 Validaçãotxt
Artist
  └── Album

Genre
```

Next domain step:

```txt
Artista
 └── Álbum
 └── Trilha

Gênero
 └── Trilha
```

---

# Pontos finais da API

## Gêneros

```http
OBTER /gêneros
POSTAR /gêneros
```

## Artistas

```http
OBTER /artistas
POSTAR/artistas
```

## Pãezinhos

```http
OBTER /álbuns
POSTAR /álbuns
```

---

# Roteiro

- [x] Módulo de gênero
- [x] Módulo artista
- [x] Módulo de Álbum
- [x] Manipulador de exceções globais
- [ ] Módulo de trilha
- [ ] DTOs de resposta
- [ ] Arrogância / OpenAPI
- [ ] Testículos
- [ ] Autenticação JWT
- [ ] Paginação e filtros
- [ ] Docker
- [ ] Implantar

---

# Objetivo do projeto

Sonara não é apenas um projeto CRUD.

É um projeto de engenharia de backend focado em:

- modelagem relacional
- Design de API
- arquitetura em camadas
- validação de dados
- tratamento de erros
- estrutura de backend escalar
- evolução progressiva do sistema
