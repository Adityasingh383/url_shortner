# URL Shortener — Spring Boot REST API

A fresher-level backend project: submit a long URL, get a short code back,
and hitting the short code redirects you to the original URL.

## Tech stack
Java 17, Spring Boot 3.5.x, Spring Data JPA, MySQL, Maven.

## 1. Set up MySQL
You don't need to create the database by hand — the connection string uses
`createDatabaseIfNotExist=true`. You only need MySQL running locally.

Open `src/main/resources/application.properties` and set your own MySQL
username/password:

```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

`spring.jpa.hibernate.ddl-auto=update` means Hibernate will create the
`urls` table automatically the first time you run the app, based on the
`Url` entity.

## 2. Run the app
```bash
mvn spring-boot:run
```
The app starts on `http://localhost:8080`.

## 3. Test with Postman

### Create a short URL (auto-generated code)
```
POST http://localhost:8080/api/urls
Content-Type: application/json

{
  "originalUrl": "https://www.google.com/search?q=spring+boot"
}
```
→ 201 Created, response includes a random 6-character `shortCode`.

### Create a short URL with a custom alias + expiry
```
POST http://localhost:8080/api/urls
Content-Type: application/json

{
  "originalUrl": "https://example.com/very/long/url",
  "customCode": "myurl",
  "expiryAt": "2026-12-31T23:59:59"
}
```

### Try the same custom alias again
Same request again → **409 Conflict** ("Custom code already exists: myurl").

### Get details of one short URL
```
GET http://localhost:8080/api/urls/myurl
```

### Get all short URLs
```
GET http://localhost:8080/api/urls
```

### Update a short URL (originalUrl and/or expiryAt only)
```
PUT http://localhost:8080/api/urls/myurl
Content-Type: application/json

{
  "originalUrl": "https://example.com/updated-destination"
}
```

### Delete a short URL
```
DELETE http://localhost:8080/api/urls/myurl
```
→ 204 No Content.

### Use the redirect
```
GET http://localhost:8080/myurl
```
In a browser this redirects (HTTP 302) straight to the original URL. In
Postman, turn OFF "Automatically follow redirects" in settings if you want
to inspect the raw 302 response and the `Location` header yourself. Each
hit increments `clickCount` — check `GET /api/urls/myurl` again to see it
go up.

### Try an expired or missing short code
- A short code that doesn't exist → **404 Not Found**.
- A short code whose `expiryAt` is in the past → **410 Gone**.
- A `POST` with an empty `originalUrl` → **400 Bad Request**.

## Project structure
```
Controller  (UrlController, RedirectController)
    ↓
Service     (UrlService, UrlServiceImpl)
    ↓
Repository  (UrlRepository — extends JpaRepository)
    ↓
Entity      (Url)
    ↓
MySQL       (urls table)
```

## Talking points for interviews
- **Short code generation**: `ShortCodeGenerator` picks 6 random characters
  from a 62-character set (A–Z, a–z, 0–9). The service keeps generating a
  new one in a `while` loop until `existsByShortCode` comes back false, so
  collisions are always avoided.
- **Custom aliases**: if `customCode` is present in the request, it's used
  directly (after checking it isn't taken) instead of calling the generator.
- **Expiry**: `RedirectController` compares `expiryAt` against
  `LocalDateTime.now()` before redirecting, and throws `UrlExpiredException`
  (→ 410) if it's in the past. A `null` `expiryAt` means the link never
  expires.
- **Click tracking**: every successful redirect calls
  `incrementClickCount`, which increments the field and saves the entity —
  a plain read-modify-write, no extra libraries involved.
- **Why JpaRepository**: extending it gives you `save`, `findAll`,
  `findById`, `delete`, etc. for free. `findByShortCode` and
  `existsByShortCode` are derived automatically from their method names —
  Spring Data generates the SQL behind the scenes.
- **Entity ↔ table mapping**: `@Entity` + `@Table(name = "urls")` maps the
  class to the table; `@Id` + `@GeneratedValue(strategy = IDENTITY)` maps
  `id` to an auto-increment primary key; `@Column(unique = true)` on
  `shortCode` enforces uniqueness at the database level too, not just in
  application code.
- **Status codes**: 201 (created), 200 (OK), 204 (deleted), 302 (redirect),
  400 (validation failure), 404 (short code not found), 409 (custom code
  taken), 410 (expired link).

## Note on the Spring Boot version
Spring Boot 3.5.16 is the last release in the 3.x line (Spring Boot moved
to 4.x, built on Spring Framework 7 / Jakarta EE 11, starting in late
2025). This project stays on 3.5.x since that's what you asked for and
it's still what most fresher-level tutorials and interview questions
assume — but it's worth knowing that 4.x exists if you get asked about
"the latest version" in an interview.
