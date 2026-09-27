package com.example.expensetracker.dto;

import java.util.List;

public class ExpenseSummaryResponse {
    private Double totalExpense;
    private List<CategorySummary> categoryBreakdown;

    public ExpenseSummaryResponse(Double totalExpense, List<CategorySummary> categoryBreakdown) {
        this.totalExpense = totalExpense;
        this.categoryBreakdown = categoryBreakdown;
    }

    public Double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(Double totalExpense) { this.totalExpense = totalExpense; }

    public List<CategorySummary> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(List<CategorySummary> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
}
