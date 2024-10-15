    package com.example.prototype;

import com.example.prototype.dao.CustomerDAO;
import com.example.prototype.dao.ItemDAO;
import com.example.prototype.entity.Customer;
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

public class AddCustomerController {

    @FXML
    private TextField areaTextField;

    @FXML
    private TextField nameTextField;

    private Scene scene;
    private boolean isEdit = false;
    private Customer editableCustomer;
    private static final CustomerDAO customerDAO = new CustomerDAO();

    public void loadEditData(){
        nameTextField.setText(editableCustomer.getName());
        areaTextField.setText(editableCustomer.getArea());
    }
    private boolean isValid(){
        if(nameTextField.getText().isBlank() || nameTextField.getText().isEmpty()
                || areaTextField.getText().isBlank() || areaTextField.getText().isEmpty()) {
            return false;
        }
        return true;
    }
    @FXML
    void onCancel(ActionEvent event) {
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
    void onSave(ActionEvent event) throws SQLException {
        if (isValid()){
            if(!isEdit) {
                customerDAO.addCustomer(nameTextField.getText(), areaTextField.getText());

            } else {
                customerDAO.updateCustomer(editableCustomer.getCustomerId(), nameTextField.getText(),areaTextField.getText());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Data berhasil disimpan !");
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customer.fxml"));
                scene.setRoot(fxmlLoader.load());
                CustomerController cont = fxmlLoader.getController();
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

    public void setEditableCustomer(Customer editableCustomer) {
        this.editableCustomer = editableCustomer;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

}
