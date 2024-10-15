package com.example.prototype;

import com.example.prototype.dao.ItemDAO;
import com.example.prototype.dao.LedgerEntryDAO;
import com.example.prototype.entity.Item;
import com.example.prototype.entity.LedgerEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Optional;

public class AccountReceivableController {

    private Scene scene;
    @FXML
    private TableView<LedgerEntry> accountReceivableTableView;
    private ObservableList<LedgerEntry> accountReceivables = FXCollections.observableArrayList();
    private static final LedgerEntryDAO ledgerEntryDAO = new LedgerEntryDAO();

    @FXML
    public void initialize(){
        DecimalFormat numberFormatter = new DecimalFormat("#,###");

        TableColumn<LedgerEntry, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(cellData -> cellData.getValue().entryDateProperty());

        TableColumn<LedgerEntry, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<LedgerEntry, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        amount.setCellFactory(column -> new TableCell<LedgerEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                } else {
                    try {
                        // Convert string to integer, format it, and then convert it back to string
                        int value = Integer.parseInt(item.replace(",", "")); // Remove commas if present
                        setText(numberFormatter.format(value));
                    } catch (NumberFormatException e) {
                        setText(item); // Fallback in case of formatting issue
                    }
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        accountReceivableTableView.getColumns().clear();
        accountReceivableTableView.getColumns().add(date);
        accountReceivableTableView.getColumns().add(description);
        accountReceivableTableView.getColumns().add(amount);
        accountReceivableTableView.setPlaceholder(new Label("No content in table"));
        accountReceivables.setAll(ledgerEntryDAO.getUnpaidAccountReceivables());
        accountReceivableTableView.setItems(accountReceivables);
    }
    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
            scene.setRoot(fxmlLoader.load());
            MenuController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onPaid(ActionEvent event) throws SQLException {
        if(accountReceivableTableView.getSelectionModel().getSelectedItem() != null) {
            // Create a confirmation alert
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Confirm Payment");
            confirmAlert.setContentText("Are you sure you want to mark this account receivable as paid?");

            // Add "Yes" and "No" buttons
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            confirmAlert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // Show the confirmation dialog and wait for the user's response
            Optional<ButtonType> result = confirmAlert.showAndWait();

            // Check the user's response
            if (result.isPresent() && result.get() == buttonTypeYes) {
                // User confirmed the action
                // Retrieve the selected item
                LedgerEntry selectedEntry = accountReceivableTableView.getSelectionModel().getSelectedItem();

                // Perform the update to mark as paid
                ledgerEntryDAO.updateIsPaidStatus(selectedEntry.getLedgerEntryId());

                // Optionally, refresh the TableView or provide feedback
                refreshTable();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Payment Recorded");
                successAlert.setContentText("The selected account receivable has been marked as paid.");
                successAlert.getButtonTypes().setAll(ButtonType.OK);
                successAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Tidak ada data yang dipilih");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public void refreshTable(){
        accountReceivables.setAll(ledgerEntryDAO.getUnpaidAccountReceivables());
        accountReceivableTableView.setItems(accountReceivables);
    }
}
