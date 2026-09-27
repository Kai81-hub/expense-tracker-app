package com.example.expensetracker.data.model

data class Expense(
    val id: Long? = null,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,          // ISO format yyyy-MM-dd, matches backend LocalDate
    val description: String? = null,
    val paymentMethod: String? = null
)

data class CategorySummary(
    val category: String,
    val total: Double
)

data class ExpenseSummary(
    val totalExpense: Double,
    val categoryBreakdown: List<CategorySummary>
)
