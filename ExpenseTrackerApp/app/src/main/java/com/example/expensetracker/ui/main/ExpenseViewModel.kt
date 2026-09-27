package com.example.expensetracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.repository.ApiResult
import com.example.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val repository = ExpenseRepository()

    private val _expenses = MutableLiveData<List<Expense>>(emptyList())
    val expenses: LiveData<List<Expense>> = _expenses

    private val _totalAmount = MutableLiveData<Double>(0.0)
    val totalAmount: LiveData<Double> = _totalAmount

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getAllExpenses()) {
                is ApiResult.Success -> {
                    _expenses.value = result.data.sortedByDescending { it.date }
                    _totalAmount.value = result.data.sumOf { it.amount }
                }
                is ApiResult.Error -> _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun deleteExpense(id: Long) {
        viewModelScope.launch {
            when (val result = repository.deleteExpense(id)) {
                is ApiResult.Success -> loadExpenses()
                is ApiResult.Error -> _errorMessage.value = result.message
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
