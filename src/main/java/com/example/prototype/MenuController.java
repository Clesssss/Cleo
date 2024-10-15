package com.example.prototype;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;

public class MenuController {

    private Scene scene;
    @FXML
    private DatePicker datePicker;

    @FXML
    void onShowSales(ActionEvent event) {
        if(datePicker.getValue() != null){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
                scene.setRoot(fxmlLoader.load());
                DailySalesController cont = fxmlLoader.getController();
                cont.setDate(datePicker.getValue());
                cont.setScene(scene);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void onAccountReceivable(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("accountReceivable.fxml"));
            scene.setRoot(fxmlLoader.load());
            AccountReceivableController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onToday(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
            scene.setRoot(fxmlLoader.load());
            DailySalesController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onItems(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("item.fxml"));

            scene.setRoot(fxmlLoader.load());
            ItemController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onCustomers(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer.fxml"));

            scene.setRoot(fxmlLoader.load());
            CustomerController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addLedgerEntry(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addLedgerEntry.fxml"));
            scene.setRoot(fxmlLoader.load());
            AddLedgerEntryController cont = fxmlLoader.getController();
            cont.setScene(scene);
            cont.setOrigin("menu");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onPurchase(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("purchase.fxml"));
            scene.setRoot(fxmlLoader.load());
            PurchaseController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
