package com.example.prototype;

import com.example.prototype.dao.LedgerEntryDAO;
import com.example.prototype.entity.LedgerEntry;
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
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class AddLedgerEntryController {

    private Scene scene;
    private boolean isEdit = false;
    private LedgerEntry editableExpense;
    private String origin;
    private LocalDate originDate;
    private static final LedgerEntryDAO ledgerEntryDAO = new LedgerEntryDAO();
    @FXML
    private TextField amountTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private DatePicker datePicker;


    @FXML
    public void initialize(){
        ObservableList<String> accountNames = ledgerEntryDAO.getExpenseAccountNames();
        choiceBox.setItems(accountNames);
    }
    @FXML
    void onCancel(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader;
            if ("dailySales".equals(origin)) {
                fxmlLoader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
                scene.setRoot(fxmlLoader.load());
                DailySalesController cont = fxmlLoader.getController();
                cont.setScene(scene);
                if (originDate != null) {
                    cont.setDate(originDate);
                }
            } else {
                fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
                scene.setRoot(fxmlLoader.load());
                MenuController cont = fxmlLoader.getController();
                cont.setScene(scene);
            }

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSave(ActionEvent event) throws SQLException {
        if (isValid()){
            if("dailySales".equals(origin)){
                if(!isEdit) {
                    ledgerEntryDAO.addLedgerEntry(choiceBox.getValue(), amountTextField.getText(), descriptionTextField.getText(), datePicker.getValue().toString(), "1");
                    ledgerEntryDAO.addLedgerEntry("Cash", String.valueOf(Integer.parseInt(amountTextField.getText()) * -1), descriptionTextField.getText(), datePicker.getValue().toString(), "0");

                } else {

                    ledgerEntryDAO.updateLedgerEntry(editableExpense.getLedgerEntryId(), choiceBox.getValue(), amountTextField.getText(), descriptionTextField.getText(), datePicker.getValue().toString(), "1");
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText("Data berhasil disimpan !");
                alert.getButtonTypes().setAll(ButtonType.OK);
                alert.showAndWait();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dailySales.fxml"));
                    scene.setRoot(fxmlLoader.load());
                    DailySalesController cont = fxmlLoader.getController();
                    cont.setScene(scene);
                    cont.setDate(originDate);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {

            }
            try {

                // Load the workbook from the file if it exists
                String month = datePicker.getValue().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                int year = datePicker.getValue().getYear();
                ExcelManager.initializeSheet(month, year);

                Workbook workbook = ExcelManager.getWorkbook();
                Sheet sheet = workbook.getSheet(month + " " + year);

                CellStyle numberStyle = workbook.createCellStyle();
                DataFormat dataFormat = workbook.createDataFormat();
                numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));

                int rowIndex = sheet.getLastRowNum() + 1;


                ExcelManager.saveWorkbook();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Harap Cek Data Kembali !" );
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void loadDatePicker(LocalDate date){
        datePicker.setValue(date);
        if(originDate != null){
            datePicker.setDisable(true);
        }
    }
    public void loadEditData(){
        amountTextField.setText(editableExpense.getAmount());
        descriptionTextField.setText(editableExpense.getDescription());
        choiceBox.setValue(editableExpense.getAccountName());
    }
    private boolean isValid(){
        if(descriptionTextField.getText().isBlank() || descriptionTextField.getText().isEmpty()
                || amountTextField.getText().isBlank() || amountTextField.getText().isEmpty()
                || choiceBox.getValue() == null || datePicker.getValue() == null) {
            return false;
        }
        return true;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public void setEditableExpense(LedgerEntry editableExpense) {
        this.editableExpense = editableExpense;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setOriginDate(LocalDate originDate) {
        this.originDate = originDate;
        loadDatePicker(originDate);
    }
}
