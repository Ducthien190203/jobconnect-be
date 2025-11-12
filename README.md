# JobConnect (Backend)
RESTful API connecting **employers** and **freelancers**, built with modern Java tech stack.

![Build Status](https://github.com/ducthien190203/jobconnect-be/actions/workflows/ci.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## 🚀 Features
- ✅ **JWT Authentication** – login/register/refresh token
- 👥 **Role-based Authorization** – Admin / Employer / Freelancer
- 🗾 **Job CRUD API** – pagination, sorting, and filter
- 🧠 **Global Exception Handling** – ProblemDetail (RFC 7807)
- 🧰 **Dockerized PostgreSQL Database**
- 📝 **Swagger UI** for interactive API testing
- 🔁 **Flyway Migration** for versioned schema changes
- ⚙️ **CI/CD** with GitHub Actions

---

## 🧱 Tech Stack
Spring Boot 3 · Java 21 · JPA · PostgreSQL · Flyway · JWT · Docker · Swagger · CI/CD (GitHub Actions)

---

## ⚙️ Setup & Run Locally
```bash
# 1. Start PostgreSQL container
docker compose up -d db

# 2. Set JWT secret
export JWT_SECRET="$(openssl rand -base64 64)"

# 3. Run the app
./gradlew bootRun
```

> ✅ App will be available at: http://localhost:8080/swagger-ui/index.html

---

## 🤉 Swagger UI
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Swagger includes:
- `/api/v1/auth/register` – Register new user  
- `/api/v1/auth/login` – Get JWT token  
- `/api/v1/jobs` – CRUD job listings  
- `/api/v1/users/me` – Get logged-in user info  

Use **Authorize → Bearer JWT** to test secured endpoints.

---

## 🧠 Project Structure
```
src/main/java/com/jobconnect
 ├── auth/        # Đăng ký, đăng nhập, refresh token
 ├── job/         # Entity, service, controller cho Job
 ├── user/        # User entity, RoleName, repository
 ├── security/    # JWT filter, config, user-details service
 ├── config/      # OpenAPI, Clock bean, global beans
 ├── common/      # Exception handler, util
 └── Application.java
```

---

## 🧪 Example API Request
### Register User
**POST** `/api/v1/auth/register`
```json
{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe"
}
```

**Response**
```json
{
  "id": "uuid",
  "email": "user@example.com"
}
```

---

## 🧰 Development Tips
- Check DB connection:
  ```bash
  docker exec -it jobconnect-db psql -U jobconnect -d jobconnect
  ```
- Recreate clean DB:
  ```bash
  docker compose down -v && docker compose up -d db
  ```
- Build & test:
  ```bash
  ./gradlew clean build
  ```

---

## 👨‍💻 Author
**Trương Đức Thiện**  
📧 [ducthien190203@gmail.com](mailto:ducthien190203@gmail.com)  
💼 [GitHub Profile](https://github.com/ducthien190203)

---

## 🧾 License
This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.
