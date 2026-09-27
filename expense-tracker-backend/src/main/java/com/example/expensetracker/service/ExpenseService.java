package com.example.expensetracker.service;

import com.example.expensetracker.dto.*;
import com.example.expensetracker.exception.ResourceNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = findEntityById(id);
        return toResponse(expense);
    }

    public ExpenseResponse createExpense(ExpenseRequest request) {
        Expense expense = new Expense(
                request.getTitle(),
                request.getAmount(),
                request.getCategory(),
                request.getDate(),
                request.getDescription(),
                request.getPaymentMethod()
        );
        return toResponse(expenseRepository.save(expense));
    }

    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense expense = findEntityById(id);
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setPaymentMethod(request.getPaymentMethod());
        return toResponse(expenseRepository.save(expense));
    }

    public void deleteExpense(Long id) {
        Expense expense = findEntityById(id);
        expenseRepository.delete(expense);
    }

    public List<ExpenseResponse> getExpensesByCategory(String category) {
        return expenseRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByDateRange(LocalDate start, LocalDate end) {
        return expenseRepository.findByDateBetween(start, end)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> searchByTitle(String keyword) {
        return expenseRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExpenseSummaryResponse getSummary() {
        Double total = expenseRepository.getTotalExpenses();
        List<CategorySummary> breakdown = expenseRepository.getCategoryWiseTotals()
                .stream()
                .map(row -> new CategorySummary((String) row[0], (Double) row[1]))
                .collect(Collectors.toList());
        return new ExpenseSummaryResponse(total, breakdown);
    }

    private Expense findEntityById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
    }

    private ExpenseResponse toResponse(Expense e) {
        return new ExpenseResponse(
                e.getId(), e.getTitle(), e.getAmount(), e.getCategory(),
                e.getDate(), e.getDescription(), e.getPaymentMethod()
        );
    }
}
