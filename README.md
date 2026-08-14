# spring-boot3-multitenancy

[![Java CI with Maven](https://github.com/hendisantika/spring-boot3-multitenancy/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot3-multitenancy/actions/workflows/maven.yml)

A small blog API that demonstrates **database-per-tenant multi-tenancy** on Spring Boot with
Spring Data JPA, Flyway and MySQL. Every tenant gets its own MySQL instance and its own schema;
a routing `DataSource` picks the right one at query time, and Flyway migrates all of them at
startup.

## Stack

| Component      | Version                                   |
|----------------|-------------------------------------------|
| Java           | 25                                        |
| Spring Boot    | 4.1.0                                     |
| Spring Data JPA / Hibernate | via Spring Boot BOM          |
| Flyway         | via Spring Boot BOM (`flyway-mysql`)      |
| MySQL          | 9.1.0 (Docker)                            |
| springdoc-openapi | 3.1.0                                  |
| ModelMapper    | 3.2.6                                     |
| Lombok         | via Spring Boot BOM                       |

## How the multi-tenancy works

```
HTTP request
     │
     ▼
Controller ──► Service ──► Repository ──► CustomRoutingDataSource
                  │                              │
                  │ sets the routing key         │ determineCurrentLookupKey()
                  │ ("en" / "fr")                ▼
                  └──────────────────►  db.configurations.en  → mysql  :13306  blog
                                        db.configurations.fr  → mysql2 :23306  blog_fr
```

* `DbConfigProperty` binds every entry under `db.configurations.*` in `application.yml` into its
  own `DataSource`.
* `CustomRoutingDataSource` extends `AbstractRoutingDataSource` and resolves the active tenant
  from the `lang` key held in `SpringBoot3MultitenancyApplication.defaultProperties`, falling back
  to `en`.
* `DatabaseMigration` listens for `ContextRefreshedEvent` and runs Flyway against **each**
  configured tenant, so `src/main/resources/db/migration` is applied to every database.

In this demo the services themselves choose the tenant so the routing is easy to observe:
`AuthorService.getAllAuthors()` switches to `en` and `TagService.getAllTags()` switches to `fr`.
The switch is process-wide, which is fine for a demo — a real application would derive the key
per request (subdomain, JWT claim, header) and keep it in a `ThreadLocal`/`RequestScope` holder.

## Requirements

* JDK 25
* Docker (for the two MySQL instances)

## Running it

```bash
# 1. start both tenant databases
docker compose up -d

# 2. run the application
./mvnw spring-boot:run
```

The app listens on **http://localhost:8081** and every endpoint is served under the
`/api` servlet path (`spring.mvc.servlet.path`). Flyway creates the schema and seeds demo rows in
both `blog` and `blog_fr` on the first start.

Build a jar instead:

```bash
./mvnw clean package
java -jar target/multitenancy-0.0.1-SNAPSHOT.jar
```

Run the tests (both databases must be up):

```bash
./mvnw test
```

## Handy URLs

| What            | URL                                              |
|-----------------|--------------------------------------------------|
| Swagger UI      | http://localhost:8081/api/swagger-ui/index.html  |
| OpenAPI JSON    | http://localhost:8081/api/v3/api-docs            |
| Actuator health | http://localhost:8081/api/actuator/health        |

`http://localhost:8081/api/` redirects to the Swagger UI.

## API

All paths below are relative to `http://localhost:8081/api`.

### Authors — tenant `en`

| Method   | Path              | Notes                                        |
|----------|-------------------|----------------------------------------------|
| `GET`    | `/v1/authors`     | Lists authors, switches the tenant to `en`   |
| `GET`    | `/v1/authors/{id}`| `404` when the author does not exist         |
| `POST`   | `/v1/authors`     | Creates when `id` is omitted, updates when it is present |
| `DELETE` | `/v1/authors/{id}`| `400` with a localized message on a bad id   |

### Tags — tenant `fr`

| Method   | Path            | Notes                                       |
|----------|-----------------|---------------------------------------------|
| `GET`    | `/v1/tags`      | Lists tags, switches the tenant to `fr`     |
| `GET`    | `/v1/tags/{id}` | |
| `POST`   | `/v1/tags`      | Creates when `id` is omitted                |
| `DELETE` | `/v1/tags/{id}` | |

### Posts

| Method   | Path             | Notes                                                        |
|----------|------------------|--------------------------------------------------------------|
| `GET`    | `/v1/posts`      | Paged; supports `page` (1-indexed), `size`, `sort` and `title` search |
| `GET`    | `/v1/posts/{id}` | |
| `POST`   | `/v1/posts`      | `title` and `body` are required; `authorId` links the author |
| `DELETE` | `/v1/posts/{id}` | |

### Post ↔ tag

| Method   | Path                          | Notes                                              |
|----------|-------------------------------|----------------------------------------------------|
| `GET`    | `/v1/posts/{id}/tags`         | |
| `POST`   | `/v1/posts/{id}/tags`         | Send `{"id": 1}` to attach an existing tag, or `{"name": "..."}` to create and attach a new one. Re-attaching returns `400` |
| `DELETE` | `/v1/posts/{id}/tags/{tagId}` | |

### Examples

```bash
# read from the "en" tenant
curl http://localhost:8081/api/v1/authors

# read from the "fr" tenant
curl http://localhost:8081/api/v1/tags

# create — no id needed
curl -X POST http://localhost:8081/api/v1/authors \
  -H 'Content-Type: application/json' \
  -d '{"name":"Sakura"}'

curl -X POST http://localhost:8081/api/v1/posts \
  -H 'Content-Type: application/json' \
  -d '{"title":"Hello Multitenancy","body":"routed to the active tenant","authorId":1}'

# paging and search
curl 'http://localhost:8081/api/v1/posts?page=1&size=5'
curl 'http://localhost:8081/api/v1/posts?title=opening'
```

## Error responses

`BaseControllerAdvice` renders every failure with the same shape:

```json
{
  "code": "404",
  "message": "Author id 9999 not found",
  "timestamp": "2026-08-14T23:35:23.775Z"
}
```

| Status | Raised by                                                       |
|--------|-----------------------------------------------------------------|
| `400`  | `BadRequestException`, `DuplicateException`, bean validation     |
| `401`  | `UnauthorizedException`                                          |
| `403`  | `ForbiddenException`                                             |
| `404`  | `DataNotFoundException`, unknown URLs                            |
| `405`  | Unsupported HTTP method                                          |
| `429`  | `TooManyRequestsException`                                       |
| `500`  | Anything else                                                    |

Messages resolved through `Translator` are localized from
`src/main/resources/messages/messages*.properties` using the `Accept-Language` header
(`en` and `id` are shipped):

```bash
curl -X DELETE http://localhost:8081/api/v1/authors/9999 -H 'Accept-Language: en'
# {"code":"400","message":"Delete failed. Please check the given id.", ...}

curl -X DELETE http://localhost:8081/api/v1/authors/9999 -H 'Accept-Language: id'
# {"code":"400","message":"Gagal menghapus data. Silakan periksa kembali id yang dikirim.", ...}
```

## Tenant configuration

Tenants live under the `db.configurations` prefix — add a key and you have a new tenant, no code
change required:

```yaml
db:
  configurations:
    en:
      url: jdbc:mysql://localhost:13306/blog?...
      driver: com.mysql.cj.jdbc.Driver
      username: yuji
      password: 53cret
    fr:
      url: jdbc:mysql://localhost:23306/blog_fr?...
      driver: com.mysql.cj.jdbc.Driver
      username: yuji
      password: 53cret
```

`docker-compose.yml` maps the matching containers:

| Service | Container | Port    | Database  |
|---------|-----------|---------|-----------|
| `db`    | `mysql`   | `13306` | `blog`    |
| `db2`   | `mysql2`  | `23306` | `blog_fr` |

Both use `yuji` / `53cret` (root password `53cret`).

## Project layout

```
src/main/java/com/hendisantika/multitenancy
├── config
│   ├── db                     # multi-tenant datasource wiring + Flyway per tenant
│   ├── CustomLocaleResolver   # Accept-Language handling and message source
│   ├── PageableConfig         # 1-indexed paging
│   └── SwaggerConfig          # OpenAPI metadata and the /api → Swagger redirect
├── controller                 # REST endpoints
├── entity                     # Author, Post, Tag
├── exception                  # typed exceptions + @RestControllerAdvice
├── model                      # PostDTO, ErrorResponse
├── repository                 # Spring Data repositories
├── service                    # business logic, picks the active tenant
└── util                       # PageUtils, Translator
src/main/resources
├── application.yml
├── db/migration               # Flyway scripts applied to every tenant
└── messages                   # i18n bundles (en, id)
```

## Author

Hendi Santika — <hendisantika@gmail.com> · Telegram [@hendisantika34](https://t.me/hendisantika34)
