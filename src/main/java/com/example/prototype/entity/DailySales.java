package com.example.prototype.entity;

import javafx.beans.property.SimpleStringProperty;

public class DailySales {
    private SimpleStringProperty itemId;
    private SimpleStringProperty itemName;
    private SimpleStringProperty totalAmount;
    private SimpleStringProperty itemPrice;
    private SimpleStringProperty totalPrice;
    public DailySales(String itemId, String itemName, String totalAmount, String itemPrice, String totalPrice) {
        this.itemId = new SimpleStringProperty(itemId);
        this.itemName = new SimpleStringProperty(itemName);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.itemPrice = new SimpleStringProperty(itemPrice);
        this.totalPrice = new SimpleStringProperty(totalPrice);
    }

    public String getItemPrice() {
        return itemPrice.get();
    }

    public SimpleStringProperty itemPriceProperty() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice.set(itemPrice);
    }

    public String getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleStringProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getItemId() {
        return itemId.get();
    }

    public SimpleStringProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId.set(itemId);
    }

    public String getItemName() {
        return itemName.get();
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }
}
