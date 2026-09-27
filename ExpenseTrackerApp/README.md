# Expense Tracker — Android App (Kotlin)

MVVM app that talks to the Spring Boot backend over REST (Retrofit + coroutines).

## Open it
1. Android Studio → **Open** → select the `ExpenseTrackerApp` folder → let Gradle sync.
2. Make sure the **backend is running first** (`http://localhost:8080`).
3. Run on an **emulator** — it's pre-configured to reach your machine via `10.0.2.2` (see `RetrofitClient.kt`).
   - On a **real device**, change `BASE_URL` in `RetrofitClient.kt` to your computer's LAN IP (both must be on the same Wi-Fi), e.g. `http://192.168.1.5:8080/api/`.

## Structure
```
app/src/main/java/com/example/expensetracker/
├── data/
│   ├── model/Expense.kt              # data classes matching backend DTOs
│   ├── api/ExpenseApiService.kt      # Retrofit endpoint interface
│   ├── api/RetrofitClient.kt         # Retrofit singleton (base URL here)
│   └── repository/ExpenseRepository.kt
└── ui/
    ├── main/MainActivity.kt          # expense list + total + pull-to-refresh
    ├── main/ExpenseViewModel.kt
    ├── addedit/AddEditExpenseActivity.kt  # add/edit form
    └── adapter/ExpenseAdapter.kt
```

## Features
- List all expenses with running total (pull to refresh)
- Add / edit / delete expense (tap a card to edit, trash icon to delete)
- Category dropdown, date picker
- Basic client-side validation + error toasts if the backend is unreachable

## Next steps you may want
- Room database for offline caching
- Charts for the `/summary` category breakdown (MPAndroidChart)
- Login/auth (JWT) if this becomes multi-user
