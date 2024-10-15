package com.example.prototype;

import com.example.prototype.dao.CustomerDAO;
import com.example.prototype.dao.PurchaseDAO;
import com.example.prototype.entity.Customer;
import com.example.prototype.entity.LedgerEntry;
import com.example.prototype.entity.Purchase;
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

public class PurchaseController {

    private Scene scene;
    private ObservableList<Purchase> purchases = FXCollections.observableArrayList();
    @FXML
    private TableView<Purchase> purchaseTableView;
    private static final PurchaseDAO purchaseDAO = new PurchaseDAO();

    @FXML
    public void initialize(){

        DecimalFormat numberFormatter = new DecimalFormat("#,###");

        TableColumn<Purchase, String> purchaseId = new TableColumn<>("ID");
        purchaseId.setCellValueFactory(cellData -> cellData.getValue().purchaseIdProperty());
        purchaseId.setResizable(false);
        purchaseId.setSortable(false);
        purchaseId.setPrefWidth(30);

        TableColumn<Purchase, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        date.setResizable(false);
        date.setSortable(false);
        date.setPrefWidth(150);

        TableColumn<Purchase, String> totalPurchase = new TableColumn<>("Total Purchase");
        totalPurchase.setCellValueFactory(cellData -> cellData.getValue().totalPurchaseProperty());
        totalPurchase.setResizable(false);
        totalPurchase.setSortable(false);
        totalPurchase.setPrefWidth(300);
        totalPurchase.setCellFactory(column -> new TableCell<Purchase, String>() {
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


        purchaseTableView.getColumns().clear();
        purchaseTableView.getColumns().add(purchaseId);
        purchaseTableView.getColumns().add(date);
        purchaseTableView.getColumns().add(totalPurchase);
        purchaseTableView.setPlaceholder(new Label("No content in table"));
        purchases.setAll(purchaseDAO.getPurchase());
        purchaseTableView.setItems(purchases);
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
    void onAdd(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPurchase.fxml"));
            scene.setRoot(fxmlLoader.load());
            AddPurchaseController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        if(purchaseTableView.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Apakah anda yakin ingin menghapus data ?" );
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                purchaseDAO.deletePurchase(((Purchase)purchaseTableView.getSelectionModel().getSelectedItem()).getPurchaseId());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Tidak ada data yang dipilih");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
        refreshTable();
    }

    @FXML
    void onEdit(ActionEvent event) {
        if(purchaseTableView.getSelectionModel().getSelectedItem() != null) {
            Purchase purchase = (Purchase) purchaseTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addPurchase.fxml"));
                scene.setRoot(fxmlLoader.load());
                AddPurchaseController cont = fxmlLoader.getController();
                cont.setScene(scene);
                cont.setEdit(true);
                cont.setEditablePurchase(purchase);
                cont.loadEditData();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Tidak ada data yang dipilih");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void refreshTable(){
        purchases.setAll(purchaseDAO.getPurchase());
        purchaseTableView.setItems(purchases);
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
