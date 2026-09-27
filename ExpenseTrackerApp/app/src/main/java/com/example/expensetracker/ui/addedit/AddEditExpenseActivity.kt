package com.example.expensetracker.ui.addedit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.data.repository.ApiResult
import com.example.expensetracker.data.repository.ExpenseRepository
import com.example.expensetracker.databinding.ActivityAddEditExpenseBinding
import kotlinx.coroutines.launch
import java.util.Calendar

class AddEditExpenseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EXPENSE_ID = "extra_expense_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_DESCRIPTION = "extra_description"

        val CATEGORIES = arrayOf("Food", "Transport", "Shopping", "Bills", "Entertainment", "Health", "Other")
    }

    private lateinit var binding: ActivityAddEditExpenseBinding
    private val repository = ExpenseRepository()

    private var expenseId: Long? = null
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategoryDropdown()
        setupDatePicker()
        prefillIfEditing()

        binding.btnSave.setOnClickListener { saveExpense() }
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, CATEGORIES)
        binding.spinnerCategory.setAdapter(adapter)
        if (binding.spinnerCategory.text.isNullOrEmpty()) {
            binding.spinnerCategory.setText(CATEGORIES[0], false)
        }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        binding.etDate.setText(selectedDate)

        binding.etDate.setOnClickListener {
            DatePickerDialog(this, { _, year, month, day ->
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                binding.etDate.setText(selectedDate)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun prefillIfEditing() {
        expenseId = intent.getLongExtra(EXTRA_EXPENSE_ID, -1L).takeIf { it != -1L }
        if (expenseId != null) {
            title = "Edit Expense"
            binding.etTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.etAmount.setText(intent.getDoubleExtra(EXTRA_AMOUNT, 0.0).toString())
            binding.spinnerCategory.setText(intent.getStringExtra(EXTRA_CATEGORY), false)
            intent.getStringExtra(EXTRA_DATE)?.let {
                selectedDate = it
                binding.etDate.setText(it)
            }
            binding.etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            binding.btnSave.text = "Update Expense"
        }
    }

    private fun saveExpense() {
        val title = binding.etTitle.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()
        val category = binding.spinnerCategory.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (title.isEmpty()) { binding.etTitle.error = "Required"; return }
        if (amountText.isEmpty()) { binding.etAmount.error = "Required"; return }
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) { binding.etAmount.error = "Enter a valid amount"; return }
        if (category.isEmpty()) { Toast.makeText(this, "Pick a category", Toast.LENGTH_SHORT).show(); return }

        val expense = Expense(
            id = expenseId,
            title = title,
            amount = amount,
            category = category,
            date = selectedDate,
            description = description.ifEmpty { null }
        )

        binding.btnSave.isEnabled = false
        lifecycleScope.launch {
            val result = if (expenseId != null) {
                repository.updateExpense(expenseId!!, expense)
            } else {
                repository.createExpense(expense)
            }
            binding.btnSave.isEnabled = true
            when (result) {
                is ApiResult.Success -> {
                    Toast.makeText(this@AddEditExpenseActivity, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ApiResult.Error -> {
                    Toast.makeText(this@AddEditExpenseActivity, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
