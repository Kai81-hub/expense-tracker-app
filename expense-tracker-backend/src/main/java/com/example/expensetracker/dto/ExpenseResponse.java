package com.example.expensetracker.dto;

import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private String title;
    private Double amount;
    private String category;
    private LocalDate date;
    private String description;
    private String paymentMethod;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long id, String title, Double amount, String category, LocalDate date, String description, String paymentMethod) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
