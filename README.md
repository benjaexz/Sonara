````md
# Sonara

Sonara is a backend project for a music catalog and user behavior system, inspired by platforms like Apple Music and Spotify.

The goal is to build a real backend system using Java, Spring Boot, PostgreSQL, REST APIs, layered architecture, validation, exception handling and relational domain modeling.

---

# Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Bean Validation
- REST API
- UUID

---

# Current Features

## Genre Module
- Create genres
- List genres
- Duplicate protection
- Validation

## Artist Module
- Create artists
- List artists
- Duplicate protection
- Validation

## Album Module
- Create albums
- List albums
- Relationship with Artist
- Validation

## Global Exception Handling
- Standardized error responses
- Duplicate resource handling
- Resource not found handling
- Validation error handling

---

# Architecture

The project follows a layered backend architecture:

```txt
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
PostgreSQL
````

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

```txt
Artist
  └── Album

Genre
```

Next domain step:

```txt
Artist
  └── Album
        └── Track

Genre
  └── Track
```

---

# API Endpoints

## Genres

```http
GET /genres
POST /genres
```

## Artists

```http
GET /artists
POST /artists
```

## Albums

```http
GET /albums
POST /albums
```

---

# Roadmap

* [x] Genre module
* [x] Artist module
* [x] Album module
* [x] Global exception handler
* [ ] Track module
* [ ] Response DTOs
* [ ] Swagger / OpenAPI
* [ ] Tests
* [ ] JWT authentication
* [ ] Pagination and filters
* [ ] Docker
* [ ] Deploy

---

# Project Goal

Sonara is not just a CRUD project.

It is a backend engineering project focused on:

* relational modeling
* API design
* layered architecture
* data validation
* error handling
* scalable backend structure
* progressive system evolution

```
```

