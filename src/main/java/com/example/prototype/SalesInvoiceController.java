package com.example.prototype;

import com.example.prototype.dao.CustomerDAO;
import com.example.prototype.dao.LedgerEntryDAO;
import com.example.prototype.dao.SalesInvoiceDAO;
import com.example.prototype.entity.Sales;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SalesInvoiceController {
    @FXML
    private TextField nameTextField;
    @FXML
    private ListView<String> nameListView;
    @FXML
    private TextField areaTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField jumlahTextField1;
    @FXML
    private TextField jumlahTextField10;
    @FXML
    private TextField jumlahTextField2;
    @FXML
    private TextField jumlahTextField3;
    @FXML
    private TextField jumlahTextField4;
    @FXML
    private TextField jumlahTextField5;
    @FXML
    private TextField jumlahTextField6;
    @FXML
    private TextField jumlahTextField7;
    @FXML
    private TextField jumlahTextField8;
    @FXML
    private TextField jumlahTextField9;
    @FXML
    private Label priceLabel1;
    @FXML
    private Label priceLabel2;
    @FXML
    private Label priceLabel3;
    @FXML
    private Label priceLabel4;
    @FXML
    private Label priceLabel5;
    @FXML
    private Label priceLabel6;
    @FXML
    private Label priceLabel7;
    @FXML
    private Label priceLabel8;
    @FXML
    private Label priceLabel9;
    @FXML
    private Label priceLabel10;

    private Map<Integer, Label> priceLabelMap;
    @FXML
    private Label totalLabel1;
    @FXML
    private Label totalLabel2;
    @FXML
    private Label totalLabel3;
    @FXML
    private Label totalLabel4;
    @FXML
    private Label totalLabel5;
    @FXML
    private Label totalLabel6;
    @FXML
    private Label totalLabel7;
    @FXML
    private Label totalLabel8;
    @FXML
    private Label totalLabel9;
    @FXML
    private Label totalLabel10;

    private Map<Integer, Label> totalLabelMap;
    @FXML
    private Label finalTotalLabel;
    @FXML
    private TextField discountTextField;
    @FXML
    private Label totalAfterDiscLabel;
    @FXML
    private CheckBox tagihanCheckBox;
    private Scene scene;
    private LocalDate date;
    private int salesId;
    private boolean isEdit = false;
    private TextField[] textFields = new TextField[10];
    private DecimalFormat numberFormatter = new DecimalFormat("#,###");
    private static final SalesInvoiceDAO salesInvoiceDAO = new SalesInvoiceDAO();
    private static final LedgerEntryDAO ledgerEntryDAO = new LedgerEntryDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();
    

    private boolean isValid(){
        if(nameTextField.getText().isBlank() || nameTextField.getText().isEmpty()
                || datePicker.getValue() == null
                || areaTextField.getText().isBlank() || areaTextField.getText().isEmpty()) {
            return false;
        }
        return true;
    }
    @FXML
    void initialize(){

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBlank()){
                nameListView.setVisible(false);
            } else {
                ObservableList<String> results = customerDAO.searchCustomerNames(newValue);

                if (results.isEmpty()) {
                    nameListView.setVisible(false);
                } else {
                    nameListView.setItems(results);
                    nameListView.setVisible(true);
                }

                int maxHeight = 100; // Maximum height of ListView in pixels
                int itemHeight = 24; // Approximate height of each item in pixels
                int height;

                if (results.size() == 0) {
                    height = itemHeight; // Show only the "No results" item
                } else {
                    height = Math.min(results.size() * itemHeight, maxHeight);
                }

                nameListView.setPrefHeight(height);
            }

        });
        nameTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                nameListView.setVisible(false); // Hide ListView if TextField loses focus
            } else {
                // Show ListView if TextField gains focus and has text
                if (!nameTextField.getText().trim().isEmpty()) {
                    nameListView.setVisible(true);
                }
            }
        });
        nameListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !nameListView.getSelectionModel().isEmpty()) {
                String selectedCustomer = nameListView.getSelectionModel().getSelectedItem();
                nameTextField.setText(selectedCustomer);

                String area = customerDAO.getCustomerArea(selectedCustomer);
                areaTextField.setText(area);

                nameListView.setVisible(false); // Hide the ListView
            }
        });

        discountTextField.setStyle("-fx-alignment: CENTER-RIGHT;");
        priceLabelMap = new HashMap<>();
        priceLabelMap.put(1, priceLabel1);
        priceLabelMap.put(2, priceLabel2);
        priceLabelMap.put(3, priceLabel3);
        priceLabelMap.put(4, priceLabel4);
        priceLabelMap.put(5, priceLabel5);
        priceLabelMap.put(6, priceLabel6);
        priceLabelMap.put(7, priceLabel7);
        priceLabelMap.put(8, priceLabel8);
        priceLabelMap.put(9, priceLabel9);
        priceLabelMap.put(10, priceLabel10);
        salesInvoiceDAO.setPriceLabel(priceLabelMap);

        totalLabelMap = new HashMap<>();
        totalLabelMap.put(1, totalLabel1);
        totalLabelMap.put(2, totalLabel2);
        totalLabelMap.put(3, totalLabel3);
        totalLabelMap.put(4, totalLabel4);
        totalLabelMap.put(5, totalLabel5);
        totalLabelMap.put(6, totalLabel6);
        totalLabelMap.put(7, totalLabel7);
        totalLabelMap.put(8, totalLabel8);
        totalLabelMap.put(9, totalLabel9);
        totalLabelMap.put(10, totalLabel10);

        textFields[0] = jumlahTextField1;
        textFields[1] = jumlahTextField2;
        textFields[2] = jumlahTextField3;
        textFields[3] = jumlahTextField4;
        textFields[4] = jumlahTextField5;
        textFields[5] = jumlahTextField6;
        textFields[6] = jumlahTextField7;
        textFields[7] = jumlahTextField8;
        textFields[8] = jumlahTextField9;
        textFields[9] = jumlahTextField10;

        Map<Integer, Integer> previousValues = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            Label totalLabel = totalLabelMap.get(i);
            int finalI = i;

            // Initialize previousValues map
            previousValues.put(finalI, 0);

            textFields[i - 1].textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int newQty = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                    int price = Integer.parseInt(priceLabelMap.get(finalI).getText().replace(",", ""));
                    int newTotal = price * newQty;
                    int previousTotal = price * previousValues.get(finalI);

                    // Update the individual total label
                    totalLabel.setText(numberFormatter.format(newTotal));

                    // Update the overall total
                    int pastTotal = Integer.parseInt(finalTotalLabel.getText().replace(",", ""));
                    finalTotalLabel.setText(numberFormatter.format(pastTotal - previousTotal + newTotal));
                    int discount = (discountTextField.getText().isEmpty())
                            ? 0
                            : Integer.parseInt(discountTextField.getText().replace(",", ""));
                    totalAfterDiscLabel.setText(numberFormatter.format(pastTotal - previousTotal + newTotal - discount));
                    // Update previousValues map
                    previousValues.put(finalI, newQty);
                } catch (NumberFormatException e) {
                    // Handle the case where newValue is not a valid integer
                    // You might want to reset the totalLabel or do some error handling here
                    totalLabel.setText("0");
                }
            });
            discountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    // Parse the finalTotalLabel value and the discountTextField value
                    int finalTotal = Integer.parseInt(finalTotalLabel.getText().replace(",", ""));
                    int discount = newValue.isEmpty() ? 0 : Integer.parseInt(newValue.replace(",", ""));

                    // Calculate the new total after discount
                    int totalAfterDiscount = finalTotal - discount;

                    // Update the totalAfterDiscLabel
                    totalAfterDiscLabel.setText(numberFormatter.format(totalAfterDiscount));
                } catch (NumberFormatException e) {
                    // Handle the case where parsing fails
                    totalAfterDiscLabel.setText("Invalid Input");
                }
            });
        }
    }

    @FXML
    void onCancel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
            scene.setRoot(fxmlLoader.load());
            DailySalesController cont = fxmlLoader.getController();
            cont.setDate(this.date);
            cont.setScene(scene);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSave(ActionEvent event) throws SQLException, IOException {

        if (isValid()) {
            if (!isEdit) {

                int itemType = 0;

                for(int i = 0; i < textFields.length; i++){
                    if(!textFields[i].getText().isBlank()){
                        itemType++;
                    }
                }
                int[] itemIds = new int[itemType];
                int[] amounts = new int[itemType];
                int counter = 0;
                for(int j = 0; j < textFields.length; j++){
                    if(!textFields[j].getText().isBlank()){
                        itemIds[counter] = j;
                        amounts[counter] = Integer.parseInt(textFields[j].getText());
                        counter++;
                    }
                }

                boolean isBill = tagihanCheckBox.isSelected();
                int discount = (discountTextField.getText() == null || discountTextField.getText().isEmpty()) ? 0 : Integer.parseInt(discountTextField.getText());
                int salesId = salesInvoiceDAO.addSalesInvoice(
                        nameTextField.getText(),
                        areaTextField.getText(),
                        datePicker.getValue().toString(),
                        itemType,
                        itemIds,
                        amounts,
                        discount,
                        isBill
                );


                int cogs = 0;
                for (int i = 0; i < 10; i++){
                    if(!textFields[i].getText().isBlank()){
                        cogs += Integer.parseInt(textFields[i].getText()) * salesInvoiceDAO.getBuyPrice(i + 1);
                    }
                }

                boolean hasDiscount = !discountTextField.getText().isEmpty() && !discountTextField.getText().trim().equals("0");
                String accountReceivableOrCash = tagihanCheckBox.isSelected() ? "Account Receivable" : "Cash";
                String salesOrDiscount = tagihanCheckBox.isSelected() ? "Tagihan" : "Sales";
                String lastParam = tagihanCheckBox.isSelected() ? "1" : "0";
                String finalAmount = finalTotalLabel.getText().replace(",", "");
                String discountAmount = hasDiscount ? discountTextField.getText().replace(",", "") : "0";
                String totalAfterDiscAmount = hasDiscount ? totalAfterDiscLabel.getText().replace(",", "") : finalAmount;

                ledgerEntryDAO.addLedgerEntrySales(salesId, accountReceivableOrCash, totalAfterDiscAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), lastParam);
                if (hasDiscount) {
                    ledgerEntryDAO.addLedgerEntrySales(salesId, "Discount Allowed", discountAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                }
                ledgerEntryDAO.addLedgerEntrySales(salesId, "Sales Revenue", finalAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntrySales(salesId, "COGS", String.valueOf(cogs), "COGS " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntrySales(salesId, "Inventory", String.valueOf(cogs), "COGS " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");

            } else {
                int itemType = 0;

                for(int i = 0; i < textFields.length; i++){
                    if(!textFields[i].getText().trim().isEmpty() && !textFields[i].getText().trim().equals("0")){
                        itemType++;
                    }
                }
                int[] itemIds = new int[itemType];
                int[] amounts = new int[itemType];
                int counter = 0;
                for(int j = 0; j < textFields.length; j++){
                    if (!textFields[j].getText().trim().isEmpty() && !textFields[j].getText().trim().equals("0")) {
                        itemIds[counter] = j;
                        amounts[counter] = Integer.parseInt(textFields[j].getText().trim());
                        counter++;
                    }
                }
                boolean isBill = tagihanCheckBox.isSelected();
                int discount = (discountTextField.getText() == null || discountTextField.getText().isEmpty()) ? 0 : Integer.parseInt(discountTextField.getText());
                salesInvoiceDAO.updateSalesInvoice(String.valueOf(salesId), nameTextField.getText(), areaTextField.getText(), datePicker.getValue().toString(), itemType, itemIds, amounts, discount, isBill);


                boolean hasDiscount = !discountTextField.getText().isEmpty() && !discountTextField.getText().trim().equals("0");
                String accountReceivableOrCash = tagihanCheckBox.isSelected() ? "Account Receivable" : "Cash";
                String salesOrDiscount = tagihanCheckBox.isSelected() ? "Tagihan" : "Sales";
                String lastParam = tagihanCheckBox.isSelected() ? "1" : "0";
                String finalAmount = finalTotalLabel.getText().replace(",", "");
                String discountAmount = hasDiscount ? discountTextField.getText().replace(",", "") : "0";
                String totalAfterDiscAmount = hasDiscount ? totalAfterDiscLabel.getText().replace(",", "") : finalAmount;
                int cogs = 0;
                for (int i = 0; i < 10; i++){
                    if(!textFields[i].getText().isBlank()){
                        cogs += Integer.parseInt(textFields[i].getText()) * salesInvoiceDAO.getBuyPrice(i + 1);
                    }
                }
                ledgerEntryDAO.deleteLedgerEntriesSales(salesId);
                ledgerEntryDAO.addLedgerEntrySales(this.salesId, accountReceivableOrCash, totalAfterDiscAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), lastParam);
                if (hasDiscount) {
                    ledgerEntryDAO.addLedgerEntrySales(this.salesId, "Discount Allowed", discountAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                }
                ledgerEntryDAO.addLedgerEntrySales(this.salesId, "Sales Revenue", finalAmount, salesOrDiscount + " " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntrySales(this.salesId, "COGS", String.valueOf(cogs), "COGS " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntrySales(this.salesId, "Inventory", String.valueOf(cogs), "COGS " + nameTextField.getText() + " " + areaTextField.getText(), datePicker.getValue().toString(), "0");

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Data berhasil disimpan !");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
                scene.setRoot(loader.load());
                DailySalesController cont = loader.getController();
                cont.setScene(scene);
                cont.setDate(this.date);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Harap Cek Data Kembali !");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        if (datePicker != null){
            datePicker.setValue(this.date);
        }
    }
    public void setSalesId(int salesId){
        this.salesId = salesId;
        Sales sales = salesInvoiceDAO.getSalesById(this.salesId);

        if (sales != null) {
            nameTextField.setText(sales.getStoreName());
            areaTextField.setText(sales.getArea());
            tagihanCheckBox.setSelected("1".equals(sales.getIsBill()));
            if(!(sales.getDiscountAmount().equals("0") || sales.getDiscountAmount() == null)){
                discountTextField.setText(sales.getDiscountAmount());
            }

            datePicker.setValue(date);

            Map<Integer, Integer> salesDetails = salesInvoiceDAO.getSalesDetailsBySalesId(salesId);
            for (Map.Entry<Integer, Integer> entry : salesDetails.entrySet()) {
                int itemId = entry.getKey();
                int amount = entry.getValue();

                switch (itemId) {
                    case 1:
                        jumlahTextField1.setText(String.valueOf(amount));
                        break;
                    case 2:
                        jumlahTextField2.setText(String.valueOf(amount));
                        break;
                    case 3:
                        jumlahTextField3.setText(String.valueOf(amount));
                        break;
                    case 4:
                        jumlahTextField4.setText(String.valueOf(amount));
                        break;
                    case 5:
                        jumlahTextField5.setText(String.valueOf(amount));
                        break;
                    case 6:
                        jumlahTextField6.setText(String.valueOf(amount));
                        break;
                    case 7:
                        jumlahTextField7.setText(String.valueOf(amount));
                        break;
                    case 8:
                        jumlahTextField8.setText(String.valueOf(amount));
                        break;
                    case 9:
                        jumlahTextField9.setText(String.valueOf(amount));
                        break;
                    case 10:
                        jumlahTextField10.setText(String.valueOf(amount));
                        break;
                }
            }
        }
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
