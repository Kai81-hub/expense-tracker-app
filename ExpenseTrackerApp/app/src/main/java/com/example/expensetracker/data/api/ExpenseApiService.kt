package com.example.expensetracker.data.api

import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.model.ExpenseSummary
import retrofit2.Response
import retrofit2.http.*

interface ExpenseApiService {

    @GET("expenses")
    suspend fun getAllExpenses(): Response<List<Expense>>

    @GET("expenses/{id}")
    suspend fun getExpenseById(@Path("id") id: Long): Response<Expense>

    @POST("expenses")
    suspend fun createExpense(@Body expense: Expense): Response<Expense>

    @PUT("expenses/{id}")
    suspend fun updateExpense(@Path("id") id: Long, @Body expense: Expense): Response<Expense>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: Long): Response<Unit>

    @GET("expenses/category/{category}")
    suspend fun getExpensesByCategory(@Path("category") category: String): Response<List<Expense>>

    @GET("expenses/search")
    suspend fun searchExpenses(@Query("keyword") keyword: String): Response<List<Expense>>

    @GET("expenses/summary")
    suspend fun getSummary(): Response<ExpenseSummary>
}
