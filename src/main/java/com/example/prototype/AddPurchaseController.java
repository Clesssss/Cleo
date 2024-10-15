package com.example.prototype;

import com.example.prototype.dao.CustomerDAO;
import com.example.prototype.dao.LedgerEntryDAO;
import com.example.prototype.dao.PurchaseDAO;
import com.example.prototype.dao.SalesInvoiceDAO;
import com.example.prototype.entity.Customer;
import com.example.prototype.entity.Item;
import com.example.prototype.entity.Purchase;
import com.example.prototype.entity.Sales;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPurchaseController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField jumlahTextField1;
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
    private TextField jumlahTextField10;
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
    private Scene scene;
    private Purchase editablePurchase;
    private int purchaseId;
    private boolean isEdit = false;
    private TextField[] textFields = new TextField[10];
    private DecimalFormat numberFormatter = new DecimalFormat("#,###");
    private static final PurchaseDAO purchaseDAO = new PurchaseDAO();
    private static final LedgerEntryDAO ledgerEntryDAO = new LedgerEntryDAO();

    private boolean isValid(){
        if((jumlahTextField1.getText().isBlank() || jumlahTextField1.getText().isEmpty())
                && (jumlahTextField2.getText().isBlank() || jumlahTextField2.getText().isEmpty())
                && (jumlahTextField3.getText().isBlank() || jumlahTextField3.getText().isEmpty())
                && (jumlahTextField4.getText().isBlank() || jumlahTextField4.getText().isEmpty())
                && (jumlahTextField5.getText().isBlank() || jumlahTextField5.getText().isEmpty())
                && (jumlahTextField6.getText().isBlank() || jumlahTextField6.getText().isEmpty())
                && (jumlahTextField7.getText().isBlank() || jumlahTextField7.getText().isEmpty())
                && (jumlahTextField8.getText().isBlank() || jumlahTextField8.getText().isEmpty())
                && (jumlahTextField9.getText().isBlank() || jumlahTextField9.getText().isEmpty())
                && (jumlahTextField10.getText().isBlank() || jumlahTextField10.getText().isEmpty())
                || datePicker.getValue() == null) {
            return false;
        }
        return true;
    }
    @FXML
    void initialize(){

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
        purchaseDAO.setPriceLabel(priceLabelMap);

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchase.fxml"));
            scene.setRoot(fxmlLoader.load());
            PurchaseController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
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
                int[] quantities = new int[itemType];
                int counter = 0;
                for(int j = 0; j < textFields.length; j++){
                    if(!textFields[j].getText().isBlank()){
                        itemIds[counter] = j;
                        quantities[counter] = Integer.parseInt(textFields[j].getText());
                        counter++;
                    }
                }

                int purchaseId = purchaseDAO.addPurchase(datePicker.getValue().toString(), itemType, itemIds, quantities);

                String finalAmount = finalTotalLabel.getText().replace(",", "");

                ledgerEntryDAO.addLedgerEntryPurchase(purchaseId, "Inventory", finalAmount, "Purchase", datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntryPurchase(purchaseId, "Cash", finalAmount, "Purchase" , datePicker.getValue().toString(), "0");

            } else {
                int itemType = 0;

                for(int i = 0; i < textFields.length; i++){
                    if(!textFields[i].getText().trim().isEmpty() && !textFields[i].getText().trim().equals("0")){
                        itemType++;
                    }
                }
                int[] itemIds = new int[itemType];
                int[] quantities = new int[itemType];
                int counter = 0;
                for(int j = 0; j < textFields.length; j++){
                    if (!textFields[j].getText().trim().isEmpty() && !textFields[j].getText().trim().equals("0")) {
                        itemIds[counter] = j;
                        quantities[counter] = Integer.parseInt(textFields[j].getText().trim());
                        counter++;
                    }
                }
                purchaseDAO.updatePurchase(String.valueOf(purchaseId), datePicker.getValue().toString(), itemType, itemIds, quantities);

                String finalAmount = finalTotalLabel.getText().replace(",", "");

                ledgerEntryDAO.deleteLedgerEntriesPurchase(purchaseId);
                ledgerEntryDAO.addLedgerEntryPurchase(purchaseId, "Inventory", finalAmount, "Purchase", datePicker.getValue().toString(), "0");
                ledgerEntryDAO.addLedgerEntryPurchase(purchaseId, "Cash", finalAmount, "Purchase" , datePicker.getValue().toString(), "0");

            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Data berhasil disimpan !");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchase.fxml"));
                scene.setRoot(fxmlLoader.load());
                PurchaseController cont = fxmlLoader.getController();
                cont.setScene(scene);

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
    public void setEdit(boolean edit) {
        isEdit = edit;
    }
    public void setEditablePurchase(Purchase editablePurchase) {
        this.editablePurchase = editablePurchase;
    }
    public void loadEditData(){
        this.purchaseId = Integer.parseInt(editablePurchase.getPurchaseId());
        datePicker.setValue(LocalDate.parse(editablePurchase.getDate()));
        Map<Integer, Integer> purchaseDetails = purchaseDAO.getPurchaseDetailsByPurchaseId(purchaseId);
        for (Map.Entry<Integer, Integer> entry : purchaseDetails.entrySet()) {
            int itemId = entry.getKey();
            int quantity = entry.getValue();

            switch (itemId) {
                case 1:
                    jumlahTextField1.setText(String.valueOf(quantity));
                    break;
                case 2:
                    jumlahTextField2.setText(String.valueOf(quantity));
                    break;
                case 3:
                    jumlahTextField3.setText(String.valueOf(quantity));
                    break;
                case 4:
                    jumlahTextField4.setText(String.valueOf(quantity));
                    break;
                case 5:
                    jumlahTextField5.setText(String.valueOf(quantity));
                    break;
                case 6:
                    jumlahTextField6.setText(String.valueOf(quantity));
                    break;
                case 7:
                    jumlahTextField7.setText(String.valueOf(quantity));
                    break;
                case 8:
                    jumlahTextField8.setText(String.valueOf(quantity));
                    break;
                case 9:
                    jumlahTextField9.setText(String.valueOf(quantity));
                    break;
                case 10:
                    jumlahTextField10.setText(String.valueOf(quantity));
                    break;
            }
        }
    }
}
