package com.example.prototype;

import com.example.prototype.dao.LedgerEntryDAO;
import com.example.prototype.dao.SalesInvoiceDAO;
import com.example.prototype.entity.DailySales;
import com.example.prototype.entity.LedgerEntry;
import com.example.prototype.entity.Sales;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DailySalesController {


    @FXML
    private Label dateLabel;

    @FXML
    private Label dayLabel;
    @FXML
    private Label totalSalesLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private Label expenseDailyLabel;
    @FXML
    private Label setoranLabel;

    @FXML
    private TableView<DailySales> dailySalesTableView;
    @FXML
    private TableView<LedgerEntry> expenseTableView;

    @FXML
    private MenuButton storeMenuButton;

    private Scene scene;
    private LocalDate date;
    private LocalDate currentDate = LocalDate.now();
    private ObservableList<DailySales> dailySales = FXCollections.observableArrayList();
    private ObservableList<LedgerEntry> expenses = FXCollections.observableArrayList();

    private static final SalesInvoiceDAO salesInvoiceDAO = new SalesInvoiceDAO();
    private static final LedgerEntryDAO ledgerEntryDAO = new LedgerEntryDAO();



    @FXML
    public void initialize() throws SQLException {
        this.date = currentDate;
        DayOfWeek currentDay = currentDate.getDayOfWeek();
        String formattedDay = currentDay.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        dayLabel.setText(formattedDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = currentDate.format(formatter);
        dateLabel.setText(formattedDate);
        dateLabel.setText(formattedDate);

        salesInvoiceDAO.getSalesByDate(currentDate);

        TableColumn<DailySales, String> itemId = new TableColumn<>("ID");
        itemId.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty());
        itemId.setPrefWidth(28);
        itemId.setResizable(false);
        itemId.setSortable(false);

        TableColumn<DailySales, String> itemName = new TableColumn<>("Item Name");
        itemName.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        itemName.setPrefWidth(133);
        itemName.setResizable(false);
        itemName.setSortable(false);

        TableColumn<DailySales, String> totalAmount = new TableColumn<>("Total Amount");
        totalAmount.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
        totalAmount.setPrefWidth(84);
        totalAmount.setResizable(false);
        totalAmount.setSortable(false);
        totalAmount.setCellFactory(column -> new TableCell<DailySales, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });
        DecimalFormat numberFormatter = new DecimalFormat("#,###");
        TableColumn<DailySales, String> itemPrice = new TableColumn<>("Item Price");
        itemPrice.setCellValueFactory(cellData -> cellData.getValue().itemPriceProperty());
        itemPrice.setPrefWidth(116);
        itemPrice.setResizable(false);
        itemPrice.setSortable(false);
        itemPrice.setCellFactory(column -> new TableCell<DailySales, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setText(null);
                } else {
                    try {
                        // Convert string to integer, format it, and then convert it back to string
                        int value = Integer.parseInt(item.replace(",", "")); // Handle commas if present
                        setText(numberFormatter.format(value));
                    } catch (NumberFormatException e) {
                        setText(item); // Fallback in case of formatting issue
                    }
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        TableColumn<DailySales, String> totalPrice = new TableColumn<>("Total Price");
        totalPrice.setCellValueFactory(cellData -> cellData.getValue().totalPriceProperty());
        totalPrice.setPrefWidth(116);
        totalPrice.setMaxWidth(116);
        totalPrice.setSortable(false);
        totalPrice.setCellFactory(column -> new TableCell<DailySales, String>() {
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


        dailySalesTableView.getColumns().clear();
        dailySalesTableView.getColumns().add(itemId);
        dailySalesTableView.getColumns().add(itemName);
        dailySalesTableView.getColumns().add(totalAmount);
        dailySalesTableView.getColumns().add(itemPrice);
        dailySalesTableView.getColumns().add(totalPrice);
        dailySalesTableView.setPlaceholder(new Label("No content in table"));

        TableColumn<LedgerEntry, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        description.setPrefWidth(113);
        description.setResizable(false);
        description.setSortable(false);

        TableColumn<LedgerEntry, String> accountName = new TableColumn<>("Account");
        accountName.setCellValueFactory(cellData -> cellData.getValue().accountNameProperty());
        accountName.setPrefWidth(120);
        accountName.setResizable(false);
        accountName.setSortable(false);

        TableColumn<LedgerEntry, String> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
        amount.setPrefWidth(70);
        amount.setResizable(false);
        amount.setSortable(false);
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
        expenseTableView.getColumns().clear();
        expenseTableView.getColumns().add(description);
        expenseTableView.getColumns().add(accountName);
        expenseTableView.getColumns().add(amount);
        expenseTableView.setPlaceholder(new Label("No content in table"));

        loadTableItems(currentDate);
        loadMenuItem(currentDate);
    }
    private void loadTableItems(LocalDate date) throws SQLException {
        dailySales.setAll(salesInvoiceDAO.getDailySales(date));
        dailySalesTableView.setItems(dailySales);

        expenses.setAll(ledgerEntryDAO.getExpense(date));
        expenseTableView.setItems(expenses);

        int totalSales = salesInvoiceDAO.getTotalSumPrice(salesInvoiceDAO.getDailySales(date));
        int discount = salesInvoiceDAO.getTotalDiscount(String.valueOf(this.date));
        int totalExpenses = ledgerEntryDAO.getTotalExpenseDaily(ledgerEntryDAO.getExpense(date));

        totalSalesLabel.setText(numberFormatter(totalSales));
        discountLabel.setText(numberFormatter(discount));
        expenseDailyLabel.setText(numberFormatter(totalExpenses));

        int setoran = totalSales - totalExpenses - discount;

        setoranLabel.setText(numberFormatter(setoran));

    }
    private String numberFormatter(int number){
        DecimalFormat numberFormat = new DecimalFormat("#,###");
        String formattedNumber = numberFormat.format(number);
        return  formattedNumber;
    }
    private void loadMenuItem(LocalDate date) throws SQLException {
        storeMenuButton.getItems().clear();
        
        List<Sales> salesInvoices = salesInvoiceDAO.getSalesByDate(date);
        for (Sales salesInvoice : salesInvoices) {
            MenuItem menuItem = new MenuItem(salesInvoice.getStoreName());
            menuItem.setOnAction(event -> onMenuItemClick(Integer.parseInt(salesInvoice.getSalesId())));
            storeMenuButton.getItems().add(menuItem);
        }
    }
    private void onMenuItemClick(int salesId) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("salesInvoice.fxml"));
            scene.setRoot(fxmlLoader.load());
            SalesInvoiceController cont = fxmlLoader.getController();
            cont.setSalesId(salesId);
            cont.setDate(this.date);
            cont.setEdit(true);
            cont.setScene(scene);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    void onAddSales(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("salesInvoice.fxml"));
            scene.setRoot(fxmlLoader.load());
            SalesInvoiceController cont = fxmlLoader.getController();
            cont.setScene(scene);
            cont.setDate(this.date);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onAddExpense(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addLedgerEntry.fxml"));
            scene.setRoot(fxmlLoader.load());
            AddLedgerEntryController cont = fxmlLoader.getController();
            cont.setScene(scene);
            cont.setOriginDate(this.date);
            cont.setOrigin("dailySales");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditExpense(ActionEvent event) {
        if(expenseTableView.getSelectionModel().getSelectedItem() != null) {
            LedgerEntry expense = (LedgerEntry) expenseTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addLedgerEntry.fxml"));
                scene.setRoot(fxmlLoader.load());
                AddLedgerEntryController cont = fxmlLoader.getController();
                cont.setScene(scene);
                cont.setEdit(true);
                cont.setEditableExpense(expense);
                cont.loadEditData();
                cont.setOrigin("dailySales");
                cont.setOriginDate(this.date);

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
    @FXML
    void onDeleteExpense(ActionEvent event) throws SQLException {
        if(expenseTableView.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Apakah anda yakin ingin menghapus data ?" );
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if(alert.getResult() == ButtonType.YES){
                ledgerEntryDAO.deleteLedgerEntry(expenseTableView.getSelectionModel().getSelectedItem().getLedgerEntryId());
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
    public void refreshTable(){
        expenses.setAll(ledgerEntryDAO.getExpense(date));
        expenseTableView.setItems(expenses);
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    public void setDate(LocalDate date) throws SQLException {
        this.date = date;
        DayOfWeek currentDay = this.date.getDayOfWeek();
        String formattedDay = currentDay.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        dayLabel.setText(formattedDay);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = this.date.format(formatter);
        dateLabel.setText(formattedDate);
        loadTableItems(this.date);
        loadMenuItem(this.date);
    }
}
