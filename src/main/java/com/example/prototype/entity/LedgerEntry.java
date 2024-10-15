package com.example.prototype.entity;

import javafx.beans.property.SimpleStringProperty;

public class LedgerEntry {
    private SimpleStringProperty ledgerEntryId;
    private SimpleStringProperty accountName;
    private SimpleStringProperty amount;
    private SimpleStringProperty description;
    private SimpleStringProperty entryDate;
    private SimpleStringProperty isSales;

    public LedgerEntry(String ledgerEntryId, String accountName, String amount, String description, String isSales) {
        this.ledgerEntryId = new SimpleStringProperty(ledgerEntryId);
        this.accountName = new SimpleStringProperty(accountName);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
        this.isSales = new SimpleStringProperty(isSales);
    }
    public LedgerEntry(String ledgerEntryId, String amount, String description, String entryDate) {
        this.ledgerEntryId = new SimpleStringProperty(ledgerEntryId);
        this.amount = new SimpleStringProperty(amount);
        this.description = new SimpleStringProperty(description);
        this.entryDate = new SimpleStringProperty(entryDate);
    }

    public String getLedgerEntryId() {
        return ledgerEntryId.get();
    }

    public SimpleStringProperty ledgerEntryIdProperty() {
        return ledgerEntryId;
    }

    public void setLedgerEntryId(String ledgerEntryId) {
        this.ledgerEntryId.set(ledgerEntryId);
    }

    public String getAccountName() {
        return accountName.get();
    }

    public SimpleStringProperty accountNameProperty() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName.set(accountName);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
    public String getEntryDate() {
        return entryDate.get();
    }

    public SimpleStringProperty entryDateProperty() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate.set(entryDate);
    }

    public String getIsSales() {
        return isSales.get();
    }

    public SimpleStringProperty isSalesProperty() {
        return isSales;
    }

    public void setIsSales(String isSales) {
        this.isSales.set(isSales);
    }


}
