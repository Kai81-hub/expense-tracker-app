package com.example.expensetracker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.databinding.ActivityMainBinding
import com.example.expensetracker.ui.addedit.AddEditExpenseActivity
import com.example.expensetracker.ui.adapter.ExpenseAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()

        viewModel.loadExpenses()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadExpenses()
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(
            onItemClick = { expense -> openEditScreen(expense) },
            onDeleteClick = { expense -> confirmDelete(expense) }
        )
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExpenses.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.expenses.observe(this) { list ->
            adapter.submitList(list)
            binding.emptyStateText.visibility = if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.totalAmount.observe(this) { total ->
            binding.tvTotalAmount.text = String.format(Locale.getDefault(), "Total: ₹%.2f", total)
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.swipeRefresh.isRefreshing = loading
        }

        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddExpense.setOnClickListener {
            startActivity(Intent(this, AddEditExpenseActivity::class.java))
        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadExpenses()
        }
    }

    private fun openEditScreen(expense: Expense) {
        val intent = Intent(this, AddEditExpenseActivity::class.java).apply {
            putExtra(AddEditExpenseActivity.EXTRA_EXPENSE_ID, expense.id)
            putExtra(AddEditExpenseActivity.EXTRA_TITLE, expense.title)
            putExtra(AddEditExpenseActivity.EXTRA_AMOUNT, expense.amount)
            putExtra(AddEditExpenseActivity.EXTRA_CATEGORY, expense.category)
            putExtra(AddEditExpenseActivity.EXTRA_DATE, expense.date)
            putExtra(AddEditExpenseActivity.EXTRA_DESCRIPTION, expense.description)
        }
        startActivity(intent)
    }

    private fun confirmDelete(expense: Expense) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete expense")
            .setMessage("Delete \"${expense.title}\"?")
            .setPositiveButton("Delete") { _, _ -> expense.id?.let { viewModel.deleteExpense(it) } }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
