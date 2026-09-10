# URL Shortener

Fresher-level Java Spring Boot backend project: submit a long URL, get a short code back, use custom aliases, set an expiry, and track clicks.

## Tech Stack
Java 17, Spring Boot 3.5.16, Spring Data JPA, MySQL, Maven.

## Prerequisites
- JDK 17
- Maven
- MySQL 8 running locally

## Setup
Open `src/main/resources/application.properties` and set your MySQL username/password:

```properties
spring.datasource.username=root
spring.datasource.password=yourpassword
```

The database `url_shortener_db` is created automatically on first run (`createDatabaseIfNotExist=true`), and the `urls` table is created/updated automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

Run the app:

```bash
mvn clean install
mvn spring-boot:run
```

Runs on `http://localhost:8080`.

## Testing
Import `url-shortener.postman_collection.json` into Postman. It contains the full flow in order: create a short URL with an auto-generated code → fetch it → create a custom alias with an expiry → try the same alias again (expect 409) → get one / get all → update the destination → hit the redirect (expect 302) → confirm the click count went up → delete it → fetch the deleted code (expect 404) → submit an empty `originalUrl` (expect 400).

Request 1 auto-saves the generated `shortCode` into a collection variable, so request 2 runs top to bottom without manual copy-pasting.

If you want to inspect the raw 302 response and `Location` header yourself in Postman rather than following it, turn off "Automatically follow redirects" in settings first.

## API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/urls` | Create a short URL (custom alias + expiry optional) |
| GET | `/api/urls` | Get all short URLs |
| GET | `/api/urls/{shortCode}` | Get details of one short URL |
| PUT | `/api/urls/{shortCode}` | Update `originalUrl` and/or `expiryAt` only |
| DELETE | `/api/urls/{shortCode}` | Delete a short URL |
| GET | `/{shortCode}` | Redirect (302) to the original URL and increment `clickCount` |

All endpoints are public — this project has no authentication.
