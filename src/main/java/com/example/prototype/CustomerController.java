package com.example.prototype;

import com.example.prototype.dao.CustomerDAO;
import com.example.prototype.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {
    private Scene scene;
    @FXML
    private TableView customerTableView;
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    private static final CustomerDAO customerDAO = new CustomerDAO();
    @FXML
    public void initialize(){

        TableColumn<Customer, String> customerId = new TableColumn<>("ID");
        customerId.setCellValueFactory(cellData -> cellData.getValue().customerIdProperty());
        customerId.setResizable(false);
        customerId.setSortable(false);
        customerId.setPrefWidth(30);

        TableColumn<Customer, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        name.setResizable(false);
        name.setSortable(false);
        name.setPrefWidth(300);

        TableColumn<Customer, String> area = new TableColumn<>("Area");
        area.setCellValueFactory(cellData -> cellData.getValue().areaProperty());
        area.setResizable(false);
        area.setSortable(false);
        area.setPrefWidth(300);


        customerTableView.getColumns().clear();
        customerTableView.getColumns().add(customerId);
        customerTableView.getColumns().add(name);
        customerTableView.getColumns().add(area);
        customerTableView.setPlaceholder(new Label("No content in table"));
        customers.setAll(customerDAO.getCustomer());
        customerTableView.setItems(customers);
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
            scene.setRoot(fxmlLoader.load());
            AddCustomerController cont = fxmlLoader.getController();
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDelete(ActionEvent event) throws SQLException {
        if(customerTableView.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Apakah anda yakin ingin menghapus data ?" );
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                customerDAO.deleteCustomer(((Customer)customerTableView.getSelectionModel().getSelectedItem()).getCustomerId());
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
        if(customerTableView.getSelectionModel().getSelectedItem() != null) {
            Customer customer = (Customer) customerTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addCustomer.fxml"));
                scene.setRoot(fxmlLoader.load());
                AddCustomerController cont = fxmlLoader.getController();
                cont.setScene(scene);
                cont.setEdit(true);
                cont.setEditableCustomer(customer);
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
        customers.setAll(customerDAO.getCustomer());
        customerTableView.setItems(customers);
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }

}

