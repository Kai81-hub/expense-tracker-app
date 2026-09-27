# Expense Tracker — Backend (Spring Boot + Java)

REST API for an expense tracker app. Built with Spring Boot 3, Spring Data JPA, and an in-memory H2 database (swap to MySQL in 2 minutes — see application.properties).

## Run it

```
cd expense-tracker-backend
./mvnw spring-boot:run
```
(or open in IntelliJ/Eclipse and run `ExpenseTrackerApplication.java`)

Server starts on **http://localhost:8080**
H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:expensedb`, user `sa`, no password)

## Endpoints (base path `/api/expenses`)

| Method | Path                     | Description                          |
|--------|--------------------------|---------------------------------------|
| GET    | `/`                      | List all expenses                     |
| GET    | `/{id}`                  | Get one expense                       |
| POST   | `/`                      | Create expense                        |
| PUT    | `/{id}`                  | Update expense                        |
| DELETE | `/{id}`                  | Delete expense                        |
| GET    | `/category/{category}`  | Filter by category                    |
| GET    | `/range?start=&end=`     | Filter by date range (yyyy-MM-dd)     |
| GET    | `/search?keyword=`       | Search by title                       |
| GET    | `/summary`               | Total spend + category-wise breakdown |

### Sample request body (POST/PUT)
```json
{
  "title": "Groceries",
  "amount": 1250.50,
  "category": "Food",
  "date": "2026-09-27",
  "description": "Weekly shopping",
  "paymentMethod": "UPI"
}
```

## Switching to MySQL
1. In `pom.xml`, uncomment the `mysql-connector-j` dependency.
2. In `application.properties`, comment the H2 block and uncomment the MySQL block; set your DB password.

## Notes
- CORS is open (`*`) for easy testing from Postman/emulator — tighten `CorsConfig.java` before shipping.
- Validation errors return 400 with a `fieldErrors` map; a missing resource returns 404.
