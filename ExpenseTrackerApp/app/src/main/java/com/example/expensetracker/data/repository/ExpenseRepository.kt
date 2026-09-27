package com.example.expensetracker.data.repository

import com.example.expensetracker.data.api.RetrofitClient
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.model.ExpenseSummary

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

class ExpenseRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAllExpenses(): ApiResult<List<Expense>> = safeCall {
        api.getAllExpenses()
    }

    suspend fun getExpenseById(id: Long): ApiResult<Expense> = safeCall {
        api.getExpenseById(id)
    }

    suspend fun createExpense(expense: Expense): ApiResult<Expense> = safeCall {
        api.createExpense(expense)
    }

    suspend fun updateExpense(id: Long, expense: Expense): ApiResult<Expense> = safeCall {
        api.updateExpense(id, expense)
    }

    suspend fun deleteExpense(id: Long): ApiResult<Unit> = safeCall {
        api.deleteExpense(id)
    }

    suspend fun searchExpenses(keyword: String): ApiResult<List<Expense>> = safeCall {
        api.searchExpenses(keyword)
    }

    suspend fun getSummary(): ApiResult<ExpenseSummary> = safeCall {
        api.getSummary()
    }

    private suspend inline fun <T> safeCall(crossinline call: suspend () -> retrofit2.Response<T>): ApiResult<T> {
        return try {
            @Suppress("UNCHECKED_CAST")
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) ApiResult.Success(body)
                else ApiResult.Success(Unit as T)
            } else {
                ApiResult.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error(e.localizedMessage ?: "Network error. Is the backend running?")
        }
    }
}
