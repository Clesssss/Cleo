package com.example.prototype;

import com.example.prototype.dao.ItemDAO;
import com.example.prototype.entity.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AddItemController {

    @FXML
    private TextField buyPriceTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField sellPriceTextField;
    private Scene scene;
    private boolean isEdit = false;
    private Item editableItem;
    private static final ItemDAO itemDAO = new ItemDAO();

    public void loadEditData(){
        nameTextField.setText(editableItem.getName());
        buyPriceTextField.setText(editableItem.getBuyPrice());
        sellPriceTextField.setText(editableItem.getSellPrice());
    }
    private boolean isValid(){
        if(nameTextField.getText().isBlank() || nameTextField.getText().isEmpty()
                || buyPriceTextField.getText().isBlank() || buyPriceTextField.getText().isEmpty()
                || sellPriceTextField.getText().isBlank() || sellPriceTextField.getText().isEmpty()) {
            return false;
        }
        return true;
    }
    @FXML
    void onCancel(ActionEvent event) {
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
    void onSave(ActionEvent event) throws SQLException {
        if (isValid()){
            if(!isEdit) {
                itemDAO.addItem(nameTextField.getText(),buyPriceTextField.getText(),sellPriceTextField.getText());

            } else {
                itemDAO.updateItem(editableItem.getItemId(), nameTextField.getText(),buyPriceTextField.getText(),sellPriceTextField.getText());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Data berhasil disimpan !");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("item.fxml"));
                scene.setRoot(fxmlLoader.load());
                ItemController cont = fxmlLoader.getController();
                cont.setScene(scene);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Harap Cek Data Kembali !" );
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setEditableItem(Item editableItem) {
        this.editableItem = editableItem;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

}
