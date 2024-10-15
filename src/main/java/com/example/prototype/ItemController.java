package com.example.prototype;

import com.example.prototype.dao.ItemDAO;
import com.example.prototype.entity.Item;
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

public class ItemController {

    private Scene scene;
    @FXML
    private TableView tableItem;
    private ObservableList<Item> items = FXCollections.observableArrayList();

    private static final ItemDAO itemDAO = new ItemDAO();
    @FXML
    public void initialize(){
        DecimalFormat numberFormatter = new DecimalFormat("#,###");

        TableColumn<Item, String> itemId = new TableColumn<>("ID");
        itemId.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty());

        TableColumn<Item, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Create and configure the Buy Price column
        TableColumn<Item, String> buyPrice = new TableColumn<>("Buy Price");
        buyPrice.setCellValueFactory(cellData -> cellData.getValue().buyPriceProperty());

        buyPrice.setCellFactory(column -> new TableCell<Item, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                } else {
                    try {
                        // Remove commas and parse the item as an integer
                        int value = Integer.parseInt(item.replace(",", ""));
                        // Format the integer and set the text
                        setText(numberFormatter.format(value));
                    } catch (NumberFormatException e) {
                        setText(item); // Fallback in case of formatting issue
                    }
                    // Align text to the right
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        buyPrice.setSortable(false); // Set column as non-sortable
        buyPrice.setResizable(false); // Set column as non-resizable

        // Create and configure the Sell Price column
        TableColumn<Item, String> sellPrice = new TableColumn<>("Sell Price");
        sellPrice.setCellValueFactory(cellData -> cellData.getValue().sellPriceProperty());

        sellPrice.setCellFactory(column -> new TableCell<Item, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                } else {
                    try {
                        // Remove commas and parse the item as an integer
                        int value = Integer.parseInt(item.replace(",", ""));
                        // Format the integer and set the text
                        setText(numberFormatter.format(value));
                    } catch (NumberFormatException e) {
                        setText(item); // Fallback in case of formatting issue
                    }
                    // Align text to the right
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        sellPrice.setSortable(false); // Set column as non-sortable
        sellPrice.setResizable(false); // Set column as non-resizable

        tableItem.getColumns().clear();
        tableItem.getColumns().add(itemId);
        tableItem.getColumns().add(name);
        tableItem.getColumns().add(buyPrice);
        tableItem.getColumns().add(sellPrice);
        tableItem.setPlaceholder(new Label("No content in table"));
        items.setAll(itemDAO.getItem());
        tableItem.setItems(items);
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addItem.fxml"));
            scene.setRoot(fxmlLoader.load());
            AddItemController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        if(tableItem.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Apakah anda yakin ingin menghapus data ?" );
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                itemDAO.deleteItem(((Item)tableItem.getSelectionModel().getSelectedItem()).getItemId());
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
        if(tableItem.getSelectionModel().getSelectedItem() != null) {
            Item item = (Item) tableItem.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addItem.fxml"));
                scene.setRoot(fxmlLoader.load());
                AddItemController cont = fxmlLoader.getController();
                cont.setScene(scene);
                cont.setEdit(true);
                cont.setEditableItem(item);
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
        items.setAll(itemDAO.getItem());
        tableItem.setItems(items);
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}
