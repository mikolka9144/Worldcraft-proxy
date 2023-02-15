package com.sender.demo;


import java.math.BigDecimal;

public class Product {

    private String name;
    private Long amount;
    private BigDecimal price;
    private String description;
    private String notes;
    private String deliveryAnnotation;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDeliveryAnnotation() {
        return deliveryAnnotation;
    }

    public void setDeliveryAnnotation(String deliveryAnnotation) {
        this.deliveryAnnotation = deliveryAnnotation;
    }
}
