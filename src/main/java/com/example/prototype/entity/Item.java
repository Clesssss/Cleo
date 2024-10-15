package com.example.prototype.entity;

import javafx.beans.property.SimpleStringProperty;

public class Item {
    private SimpleStringProperty itemId;
    private SimpleStringProperty name;
    private SimpleStringProperty buyPrice;
    private SimpleStringProperty sellPrice;

    public Item(String itemId, String name, String buyPrice, String sellPrice) {
        this.itemId = new SimpleStringProperty(itemId);
        this.name = new SimpleStringProperty(name);
        this.buyPrice = new SimpleStringProperty(buyPrice);
        this.sellPrice = new SimpleStringProperty(sellPrice);
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBuyPrice() {
        return buyPrice.get();
    }

    public SimpleStringProperty buyPriceProperty() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice.set(buyPrice);
    }

    public String getSellPrice() {
        return sellPrice.get();
    }

    public SimpleStringProperty sellPriceProperty() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice.set(sellPrice);
    }
}
