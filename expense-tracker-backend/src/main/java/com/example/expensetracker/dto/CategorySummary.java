package com.example.expensetracker.dto;

public class CategorySummary {
    private String category;
    private Double total;

    public CategorySummary(String category, Double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
