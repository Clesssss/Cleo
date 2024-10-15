package com.example.prototype.dao;

import com.example.prototype.JDBC;
import com.example.prototype.entity.DailySales;
import com.example.prototype.entity.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import static com.example.prototype.JDBC.LOGGER;

public class ItemDAO {
    private final JDBC jdbc = new JDBC();


    public Collection<Item> getItem(){
        Collection<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM `item`";

        jdbc.connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String id = resultSet.getString("item_id");
                    String name = resultSet.getString("name");
                    String buyPrice = resultSet.getString("buy_price");
                    String sellPrice = resultSet.getString("sell_price");

                    Item item = new Item(id, name, buyPrice, sellPrice);
                    items.add(item);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return items;
    }


    public void addItem (
            String name,
            String buyPrice,
            String sellPrice
    ) throws SQLException {
        String sql = "INSERT INTO `item`(" +
                "`name`, " +
                "`buy_price`, " +
                "`sell_price`)" +
                " VALUES (" +
                "?," +
                "?," +
                "?)";
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, buyPrice);
        stm.setString(3, sellPrice);
        stm.execute();
    }

    public void deleteItem(String itemId) throws SQLException {
        String sql = "DELETE FROM `item` WHERE `item_id` = " + itemId ;
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.execute();
    }
    public void updateItem(
            String itemId,
            String name,
            String buyPrice,
            String sellPrice
    ) throws SQLException {
        String sql = "UPDATE `item` SET " +
                "`name` = ?," +
                "`buy_price` = ?," +
                "`sell_price` = ?" +
                " WHERE `item_id` = " + itemId;

        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.setString(1, name);
        stm.setString(2, buyPrice);
        stm.setString(3, sellPrice);
        stm.execute();
    }
}
