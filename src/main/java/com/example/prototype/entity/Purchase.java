package com.example.prototype.entity;

import javafx.beans.property.SimpleStringProperty;

public class Purchase {
    private SimpleStringProperty purchaseId;
    private SimpleStringProperty date;
    private SimpleStringProperty totalPurchase;

    public Purchase(String purchaseId, String date, String totalPurchase) {
        this.purchaseId = new SimpleStringProperty(purchaseId);
        this.date = new SimpleStringProperty(date);
        this.totalPurchase = new SimpleStringProperty(totalPurchase);
    }

    public String getPurchaseId() {
        return purchaseId.get();
    }

    public SimpleStringProperty purchaseIdProperty() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId.set(purchaseId);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTotalPurchase() {
        return totalPurchase.get();
    }

    public SimpleStringProperty totalPurchaseProperty() {
        return totalPurchase;
    }

    public void setTotalPurchase(String totalPurchase) {
        this.totalPurchase.set(totalPurchase);
    }
}
