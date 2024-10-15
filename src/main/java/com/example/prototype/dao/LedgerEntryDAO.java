package com.example.prototype.dao;

import com.example.prototype.JDBC;
import com.example.prototype.entity.Item;
import com.example.prototype.entity.LedgerEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;

import static com.example.prototype.JDBC.LOGGER;

public class LedgerEntryDAO {
    private final JDBC jdbc = new JDBC();
    public ObservableList<String> getExpenseAccountNames() {
        ObservableList<String> accountNames = FXCollections.observableArrayList();
        String query = "SELECT name FROM account " +
                "WHERE (type = 'Expense' OR name = 'Employee Advance' OR name = 'Owner''s Drawing') " +
                "AND name != 'Discount Allowed'";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        accountNames.add(resultSet.getString("name"));
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return accountNames;
    }
    public ObservableList<String> getAccountNames() {
        ObservableList<String> accountNames = FXCollections.observableArrayList();
        String query = "SELECT name FROM account";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        accountNames.add(resultSet.getString("name"));
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return accountNames;
    }
    public int getTotalExpenseDaily(Collection<LedgerEntry> expenses) {
        return expenses.stream()
                .mapToInt(ds -> Integer.parseInt(ds.getAmount()))
                .sum();
    }
    public Collection<LedgerEntry> getExpense(LocalDate date) {
        Collection<LedgerEntry> expenses = new ArrayList<>();
        String sql = "SELECT * FROM `ledger_entry` WHERE `is_sales` = 1 AND `entry_date` = ?";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(date));
                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        String ledgerEntryId = resultSet.getString("ledger_entry_id");
                        String accountId = resultSet.getString("account_id");
                        int amount = resultSet.getInt("amount");
                        String description = resultSet.getString("description");
                        String isSales = resultSet.getString("is_sales");

                        // Fetch account name using accountId
                        String accountName = getAccountNameById(accountId);

                        LedgerEntry expense = new LedgerEntry(ledgerEntryId, accountName, String.valueOf(amount), description, isSales);
                        expenses.add(expense);
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return expenses;
    }

    public String getAccountNameById(String accountId) {
        final String[] accountName = {""};
        String sql = "SELECT `name` FROM `account` WHERE `account_id` = ?";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, accountId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        accountName[0] = resultSet.getString("name");
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return accountName[0];
    }
    public void addLedgerEntry(
            String accountType,
            String amount,
            String description,
            String entryDate,
            String isSales
    ) throws SQLException {
        String fetchAccountIdSql = "SELECT `account_id` FROM `account` WHERE `name` = ?";
        int accountId;

        try (PreparedStatement fetchStm = jdbc.connection.get().prepareStatement(fetchAccountIdSql)) {
            fetchStm.setString(1, accountType);
            try (ResultSet rs = fetchStm.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt("account_id");
                } else {
                    throw new SQLException("Account type not found: " + accountType);
                }
            }
        }

        String sql = "INSERT INTO `ledger_entry`(" +
                "`account_id`, " +
                "`amount`, " +
                "`description`, " +
                "`entry_date`, " +
                "`is_sales`)" +
                " VALUES (" +
                "?," +
                "?," +
                "?," +
                "?," +
                "?)";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setString(2, amount);
            stm.setString(3, description);
            stm.setString(4, entryDate);
            stm.setString(5, isSales);
            stm.execute();
        }
    }
    public void addLedgerEntrySales(
            int salesId,
            String accountType,
            String amount,
            String description,
            String entryDate,
            String isSales
    ) throws SQLException {
        String fetchAccountIdSql = "SELECT `account_id` FROM `account` WHERE `name` = ?";
        int accountId;

        try (PreparedStatement fetchStm = jdbc.connection.get().prepareStatement(fetchAccountIdSql)) {
            fetchStm.setString(1, accountType);
            try (ResultSet rs = fetchStm.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt("account_id");
                } else {
                    throw new SQLException("Account type not found: " + accountType);
                }
            }
        }

        String sql = "INSERT INTO `ledger_entry`(" +
                "`account_id`, " +
                "`sales_id`, " +
                "`amount`, " +
                "`description`, " +
                "`entry_date`, " +
                "`is_sales`)" +
                " VALUES (" +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?)";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, accountId);
            System.out.println(salesId);
            stm.setInt(2, salesId);
            stm.setString(3, amount);
            stm.setString(4, description);
            stm.setString(5, entryDate);
            stm.setString(6, isSales);
            stm.execute();
        }
    }
    public void addLedgerEntryPurchase(
            int purchaseId,
            String accountType,
            String amount,
            String description,
            String entryDate,
            String isSales
    ) throws SQLException {
        String fetchAccountIdSql = "SELECT `account_id` FROM `account` WHERE `name` = ?";
        int accountId;

        try (PreparedStatement fetchStm = jdbc.connection.get().prepareStatement(fetchAccountIdSql)) {
            fetchStm.setString(1, accountType);
            try (ResultSet rs = fetchStm.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt("account_id");
                } else {
                    throw new SQLException("Account type not found: " + accountType);
                }
            }
        }

        String sql = "INSERT INTO `ledger_entry`(" +
                "`account_id`, " +
                "`purchase_id`, " +
                "`amount`, " +
                "`description`, " +
                "`entry_date`, " +
                "`is_sales`)" +
                " VALUES (" +
                "?," +
                "?," +
                "?," +
                "?," +
                "?," +
                "?)";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setInt(2, purchaseId);
            stm.setString(3, amount);
            stm.setString(4, description);
            stm.setString(5, entryDate);
            stm.setString(6, isSales);
            stm.execute();
        }
    }


    public void updateLedgerEntry(
            String ledgerEntryId,
            String accountType,
            String amount,
            String description,
            String entryDate,
            String isSales
    ) throws SQLException {
        // Fetch the account_id based on the accountType
        String fetchAccountIdSql = "SELECT `account_id` FROM `account` WHERE `name` = ?";
        int accountId;

        try (PreparedStatement fetchStm = jdbc.connection.get().prepareStatement(fetchAccountIdSql)) {
            fetchStm.setString(1, accountType);
            try (ResultSet rs = fetchStm.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt("account_id");
                } else {
                    throw new SQLException("Account type not found: " + accountType);
                }
            }
        }

        // Update the ledger_entry table
        String sql = "UPDATE `ledger_entry` SET " +
                "`account_id` = ?, " +
                "`amount` = ?, " +
                "`description` = ?, " +
                "`entry_date` = ?, " +
                "`is_sales` = ? " +
                "WHERE `ledger_entry_id` = ?";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, accountId);
            stm.setString(2, amount);
            stm.setString(3, description);
            stm.setString(4, entryDate);
            stm.setString(5, isSales);
            stm.setString(6, ledgerEntryId);
            stm.execute();
        }
    }
    public void deleteLedgerEntry(String ledgerEntryId) throws SQLException {
        String sql = "DELETE FROM `ledger_entry` WHERE `ledger_entry_id` = ?";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setString(1, ledgerEntryId);
            stm.execute();
        }
    }
    public void deleteLedgerEntriesSales(int salesId) throws SQLException {
        String sql = "DELETE FROM `ledger_entry` WHERE `sales_id` = ?";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, salesId);
            stm.execute();
        }
    }
    public void deleteLedgerEntriesPurchase(int purchaseId) throws SQLException {
        String sql = "DELETE FROM `ledger_entry` WHERE `purchase_id` = ?";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setInt(1, purchaseId);
            stm.execute();
        }
    }
    public Collection<LedgerEntry> getUnpaidAccountReceivables() {
        Collection<LedgerEntry> unpaidAccountReceivables = new ArrayList<>();
        String sql = "SELECT ledger_entry_id, amount, description, entry_date " +
                "FROM ledger_entry " +
                "WHERE account_id = (SELECT account_id FROM account WHERE name = 'Account Receivable') " +
                "AND is_paid = FALSE";

        jdbc.connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    // Extract data from ResultSet
                    String ledgerEntryId = resultSet.getString("ledger_entry_id");
                    String amount = resultSet.getString("amount");
                    String description = resultSet.getString("description");
                    String entryDate = resultSet.getString("entry_date");

                    // Create a LedgerEntry object and add to the collection
                    LedgerEntry entry = new LedgerEntry(ledgerEntryId, amount, description, entryDate);
                    unpaidAccountReceivables.add(entry);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return unpaidAccountReceivables;
    }
    public void updateIsPaidStatus(
            String ledgerEntryId
    ) throws SQLException {

        String sql = "UPDATE `ledger_entry` SET " +
                "`is_paid` = ? " +
                "WHERE `ledger_entry_id` = ?";
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {

            stm.setInt(1, 1);
            stm.setString(2, ledgerEntryId);
            stm.execute();
        }
    }
}
