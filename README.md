# Expense Tracker — Full Project

A complete expense tracking application:

- **Backend:** Java + Spring Boot, exposing a REST API
- **Frontend:** Android app in Kotlin, talking to the backend via Retrofit

```
┌──────────────────────┐        HTTP / JSON        ┌───────────────────────────┐
│   Android App         │  ───────────────────────▶ │   Spring Boot Backend      │
│   (Kotlin, MVVM)       │  ◀─────────────────────── │   (Java, REST API)         │
│   ExpenseTrackerApp/  │                            │   expense-tracker-backend/ │
└──────────────────────┘                            └──────────────┬────────────┘
                                                                      │
                                                                      ▼
                                                              ┌───────────────┐
                                                              │  H2 Database   │
                                                              │  (in-memory)   │
                                                              └───────────────┘
```

---

## 1. What's in each folder

| Folder | What it is | Language |
|---|---|---|
| `expense-tracker-backend/` | REST API — handles storage, business logic, validation | Java (Spring Boot) |
| `ExpenseTrackerApp/` | Mobile app — list, add, edit, delete expenses | Kotlin (Android) |

You run **both**. The app is just a client — without the backend running, it has nothing to talk to.

---

## 2. Prerequisites

| Tool | Needed for | Notes |
|---|---|---|
| JDK 17+ | Backend | `java -version` to check |
| Maven | Backend | Project includes standard `pom.xml`; use your IDE's built-in Maven or install the Maven CLI |
| Android Studio (recent, e.g. Iguana+) | App | Comes with the Android SDK + Gradle |
| Android Emulator or physical Android phone | Running the app | Emulator is simplest to start with |

No database installation is required to start — the backend uses an **in-memory H2 database** out of the box (data resets on every restart). MySQL instructions are below if you want persistence.

---

## 3. Run the backend first

```bash
cd expense-tracker-backend
./mvnw spring-boot:run
```

Or open the folder in IntelliJ/Eclipse/Android Studio (with a Java plugin) and run `ExpenseTrackerApplication.java` directly.

**Confirm it's up:** open `http://localhost:8080/api/expenses` in a browser — you should see `[]` (an empty list).

- H2 database console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:expensedb`
  - User: `sa`, Password: *(blank)*

### Switching to MySQL (optional, for persistent storage)
1. In `pom.xml`, uncomment the `mysql-connector-j` dependency.
2. In `src/main/resources/application.properties`, comment out the H2 block and uncomment the MySQL block. Set your DB username/password.
3. Create the database (or let `createDatabaseIfNotExist=true` do it for you) and restart.

---

## 4. Run the Android app

1. Open Android Studio → **Open** → select the `ExpenseTrackerApp` folder → wait for Gradle sync.
2. Make sure the **backend is already running** (step 3).
3. Press **Run** with an emulator selected.

The app is pre-configured to reach your machine's backend at `http://10.0.2.2:8080/api/` — this is a special address that only works from the **Android emulator** (it means "the host machine's localhost").

### Using a real phone instead of an emulator
1. Find your computer's LAN IP (e.g. `192.168.1.5`) — both the phone and computer must be on the same Wi-Fi.
2. Open `ExpenseTrackerApp/app/src/main/java/com/example/expensetracker/data/api/RetrofitClient.kt` and change:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:8080/api/"
   ```
   to
   ```kotlin
   private const val BASE_URL = "http://192.168.1.5:8080/api/"
   ```
3. Re-run the app.

---

## 5. REST API reference

Base URL: `http://localhost:8080/api/expenses`

| Method | Endpoint | Description | Body |
|---|---|---|---|
| GET | `/` | List every expense | — |
| GET | `/{id}` | Get one expense by ID | — |
| POST | `/` | Create a new expense | see below |
| PUT | `/{id}` | Update an existing expense | see below |
| DELETE | `/{id}` | Delete an expense | — |
| GET | `/category/{category}` | Filter by category (e.g. `Food`) | — |
| GET | `/range?start=2026-09-01&end=2026-09-30` | Filter by date range | — |
| GET | `/search?keyword=coffee` | Search by title | — |
| GET | `/summary` | Total spend + per-category totals | — |

### Request body (POST / PUT)
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
`title`, `amount`, `category`, and `date` are required. `description` and `paymentMethod` are optional.

### Response shape
```json
{
  "id": 1,
  "title": "Groceries",
  "amount": 1250.5,
  "category": "Food",
  "date": "2026-09-27",
  "description": "Weekly shopping",
  "paymentMethod": "UPI"
}
```

### Error responses
- **400 Bad Request** — validation failed. Includes a `fieldErrors` map naming which field(s) failed and why.
- **404 Not Found** — no expense with that ID.
- **500 Internal Server Error** — unexpected server error.

---

## 6. Project structure

### Backend (`expense-tracker-backend/`)
```
src/main/java/com/example/expensetracker/
├── ExpenseTrackerApplication.java   # entry point
├── model/Expense.java               # JPA entity
├── dto/                             # request/response shapes
├── repository/ExpenseRepository.java# Spring Data JPA queries
├── service/ExpenseService.java      # business logic
├── controller/ExpenseController.java# REST endpoints
├── exception/                       # custom exceptions + global handler
└── config/CorsConfig.java           # CORS setup
src/main/resources/application.properties
```

### Android app (`ExpenseTrackerApp/`)
```
app/src/main/java/com/example/expensetracker/
├── data/
│   ├── model/Expense.kt             # data classes matching backend DTOs
│   ├── api/ExpenseApiService.kt     # Retrofit endpoint definitions
│   ├── api/RetrofitClient.kt        # Retrofit singleton — BASE_URL lives here
│   └── repository/ExpenseRepository.kt
└── ui/
    ├── main/MainActivity.kt         # expense list, total, pull-to-refresh
    ├── main/ExpenseViewModel.kt
    ├── addedit/AddEditExpenseActivity.kt
    └── adapter/ExpenseAdapter.kt
```

---

## 7. Features

- Add, view, edit, and delete expenses
- Category tagging (Food, Transport, Shopping, Bills, Entertainment, Health, Other)
- Running total shown at the top of the list
- Pull-to-refresh
- Search and date-range filtering (backend supports it; wire up UI for it whenever you want)
- Category-wise spending summary via `/summary` (ready for a pie/bar chart on the app side)

---

## 8. Troubleshooting

| Symptom | Likely cause | Fix |
|---|---|---|
| App shows a network error toast | Backend not running | Start the backend before the app |
| App can't connect from a real phone | Wrong `BASE_URL` | Use your computer's LAN IP, not `10.0.2.2` (see step 4) |
| `403`/CORS errors in browser testing | Shouldn't happen — CORS is open by default | Check `CorsConfig.java` wasn't edited |
| Data disappears after restarting backend | Using default H2 in-memory DB | Expected — switch to MySQL for persistence (step 3) |
| Backend won't start — port in use | Something else is on 8080 | Change `server.port` in `application.properties` |

---

## 9. Possible next steps

- Room database in the app for offline caching
- Charts (e.g. MPAndroidChart) for the `/summary` endpoint
- User accounts + JWT authentication for multi-user support
- Recurring expenses / budgets / monthly limits
- Export to CSV/PDF
