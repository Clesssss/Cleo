package com.example.prototype.dao;

import com.example.prototype.JDBC;
import com.example.prototype.entity.Customer;
import com.example.prototype.entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import static com.example.prototype.JDBC.LOGGER;

public class CustomerDAO {

    private final JDBC jdbc = new JDBC();

    public ObservableList<String> searchCustomerNames(String searchText) {
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        String query = "SELECT name FROM customer WHERE name LIKE ?";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, "%" + searchText + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        customerNames.add(resultSet.getString("name"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        return customerNames;
    }
    public String getCustomerArea(String customerName) {
        final String[] area = {""};
        String sql = "SELECT area FROM customer WHERE name = ?";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customerName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        area[0] = rs.getString("area");
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Database error", ex);
            }
        });

        return area[0];
    }
    public Collection<Customer> getCustomer(){
        Collection<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM `customer`";

        jdbc.connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String id = resultSet.getString("customer_id");
                    String name = resultSet.getString("name");
                    String area = resultSet.getString("area");

                    Customer customer = new Customer(id, name, area);
                    customers.add(customer);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return customers;
    }
    public void addCustomer (
            String name,
            String area
    ) throws SQLException {
        String sql = "INSERT INTO `customer`(" +
                "`name`, " +
                "`area`)" +
                " VALUES (" +
                "?," +
                "?)";
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, area);
        stm.execute();
    }

    public void deleteCustomer(String customerId) throws SQLException {
        String sql = "DELETE FROM `customer` WHERE `customer_id` = " + customerId ;
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.execute();
    }
    public void updateCustomer(
            String customerId,
            String name,
            String area
    ) throws SQLException {
        String sql = "UPDATE `customer` SET " +
                "`name` = ?," +
                "`area` = ?" +
                " WHERE `customer_id` = " + customerId;

        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, area);
        stm.execute();
    }
}
