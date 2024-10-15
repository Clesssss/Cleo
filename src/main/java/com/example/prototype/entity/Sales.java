package com.example.prototype.entity;

import javafx.beans.property.SimpleStringProperty;

public class Sales {
    private SimpleStringProperty salesId;
    private SimpleStringProperty storeName;
    private SimpleStringProperty area;
    private SimpleStringProperty date;
    private SimpleStringProperty discountAmount;
    private SimpleStringProperty isBill;

    public Sales(){
        this.salesId = new SimpleStringProperty();
        this.storeName = new SimpleStringProperty();
        this.area = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.discountAmount = new SimpleStringProperty();
        this.isBill = new SimpleStringProperty();
    }
    public String getSalesId() {
        return salesId.get();
    }

    public SimpleStringProperty salesIdProperty() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId.set(salesId);
    }

    public String getStoreName() {
        return storeName.get();
    }

    public SimpleStringProperty storeNameProperty() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName.set(storeName);
    }

    public String getArea() {
        return area.get();
    }

    public SimpleStringProperty areaProperty() {
        return area;
    }

    public void setArea(String area) {
        this.area.set(area);
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

    public String getIsBill() {
        return isBill.get();
    }

    public SimpleStringProperty isBillProperty() {
        return isBill;
    }

    public void setIsBill(String isBill) {
        this.isBill.set(isBill);
    }

    public String getDiscountAmount() {
        return discountAmount.get();
    }

    public SimpleStringProperty discountAmountProperty() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount.set(discountAmount);
    }
}
