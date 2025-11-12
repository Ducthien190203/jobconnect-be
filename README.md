# JobConnect (Backend)
RESTful API connecting employers and freelancers.

## Tech Stack
Spring Boot 3 · Java 21 · JPA · PostgreSQL · Flyway · JWT · Docker · Swagger · CI/CD (GitHub Actions)

## Setup
1. `docker compose up -d db`
2. `export JWT_SECRET="<base64>"`
3. `./gradlew bootRun`

## Swagger
http://localhost:8080/swagger-ui/index.html

## Project Structure
- `auth`: đăng ký, đăng nhập, refresh token và DTO.
- `job`: entity, service và controller cho tin tuyển dụng.
- `user`: quản lý RoleName, User entity và repository.
- `security`: JWT filter, config, properties và user-details service.
- `config`: các cấu hình chung như OpenAPI, Clock bean.
- `common`: handler trả lỗi ProblemDetail toàn cục.

## License
MIT
