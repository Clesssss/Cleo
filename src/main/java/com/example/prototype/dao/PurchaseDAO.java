package com.example.prototype.dao;

import com.example.prototype.JDBC;
import com.example.prototype.entity.Customer;
import com.example.prototype.entity.Purchase;
import javafx.scene.control.Label;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.example.prototype.JDBC.LOGGER;

public class PurchaseDAO {
    private final JDBC jdbc = new JDBC();
    public Collection<Purchase> getPurchase(){
        Collection<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM `purchase`";

        jdbc.connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String id = resultSet.getString("purchase_id");
                    String date = resultSet.getString("date");
                    String totalPurchase = resultSet.getString("total_purchase");

                    Purchase purchase = new Purchase(id, date, totalPurchase);
                    purchases.add(purchase);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return purchases;
    }
    public void deletePurchase(String purchaseId) throws SQLException {
        String sql = "DELETE FROM `purchase` WHERE `purchase_id` = " + purchaseId ;
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sql);
        stm.execute();
    }
    public void setPriceLabel(Map<Integer, Label> priceLabelMap){
        DecimalFormat numberFormatter = new DecimalFormat("#,###");
        String sql = "SELECT item_id, buy_price FROM item WHERE item_id BETWEEN 1 AND 10";
        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int buyPrice = rs.getInt("buy_price");

                    Label priceLabel = priceLabelMap.get(itemId);
                    if (priceLabel != null) {

                        priceLabel.setText(numberFormatter.format(buyPrice));
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
    public int addPurchase(
            String date,
            int itemType,
            int[] itemIds,
            int[] quantities

    ) throws SQLException {
        String sql = "INSERT INTO `purchase`(" +
                "`date`, " +
                "`total_purchase`)" +
                " VALUES (?, ?)";

        // Prepare the statement with auto-generated keys
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, date);

            // Calculate total sales
            int totalPurchase = 0;
            for (int i = 0; i < itemType; i++) {
                int itemId = itemIds[i] + 1;
                int quantity = quantities[i];
                int buyPrice = 0;

                String sql3 = "SELECT `buy_price` FROM `item` WHERE `item_id` = ?";
                try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                    stm3.setInt(1, itemId);
                    try (ResultSet rs = stm3.executeQuery()) {
                        if (rs.next()) {
                            buyPrice = rs.getInt("buy_price");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                totalPurchase += buyPrice * quantity;
            }
            stm.setInt(2, totalPurchase);
            stm.executeUpdate();

            // Retrieve the generated sales_id
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int purchaseId = generatedKeys.getInt(1);

                    // Insert into sales_detail
                    for (int i = 0; i < itemType; i++) {
                        int itemId = itemIds[i] + 1;
                        int quantity = quantities[i];
                        int buyPrice = 0;

                        String sql3 = "SELECT `buy_price` FROM `item` WHERE `item_id` = ?";
                        try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                            stm3.setInt(1, itemId);
                            try (ResultSet rs = stm3.executeQuery()) {
                                if (rs.next()) {
                                    buyPrice = rs.getInt("buy_price");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        int totalPrice = buyPrice * quantity;
                        String sql2 = "INSERT INTO `purchase_detail`(" +
                                "`purchase_id`, " +
                                "`item_id`, " +
                                "`quantity`, " +
                                "`total_price`)" +
                                " VALUES (?, ?, ?, ?)";

                        try (PreparedStatement stm2 = jdbc.connection.get().prepareStatement(sql2)) {
                            stm2.setInt(1, purchaseId);
                            stm2.setInt(2, itemId);
                            stm2.setInt(3, quantity);
                            stm2.setInt(4, totalPrice);
                            stm2.executeUpdate();
                        }
                    }
                    return purchaseId;
                } else {
                    throw new SQLException("Failed to retrieve generated sales ID.");
                }
            }
        }

    }
    public void updatePurchase(
            String purchaseId,
            String date,
            int itemType,
            int[] itemIds,
            int[] quantities
    ) throws SQLException {
        // Update the main sales record
        String sqlUpdatePurchase = "UPDATE `purchase` SET " +
                "`date` = ?, " +
                "`total_purchase` = ? " +
                "WHERE `purchase_id` = ?";
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sqlUpdatePurchase);
        stm.setString(1, date);

        // Calculate total sales
        int totalPurchase = 0;
        for (int i = 0; i < itemType; i++) {
            int itemId = itemIds[i] + 1;
            int quantity = quantities[i];
            int buyPrice = 0;

            String sql3 = "SELECT `buy_price` FROM `item` WHERE `item_id` = ?";
            try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                stm3.setInt(1, itemId);
                try (ResultSet rs = stm3.executeQuery()) {
                    if (rs.next()) {
                        buyPrice = rs.getInt("buy_price");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            totalPurchase += buyPrice * quantity;
        }
        stm.setInt(2, totalPurchase);
        stm.setInt(3, Integer.parseInt(purchaseId));
        stm.executeUpdate();

        // Retrieve existing item IDs for this sales_id
        Set<Integer> existingItemIds = new HashSet<>();
        String sqlSelectDetails = "SELECT `item_id` FROM `purchase_detail` WHERE `purchase_id` = ?";

        try (PreparedStatement stmSelectDetails = jdbc.connection.get().prepareStatement(sqlSelectDetails)) {
            stmSelectDetails.setString(1, purchaseId);
            try (ResultSet rs = stmSelectDetails.executeQuery()) {
                while (rs.next()) {
                    existingItemIds.add(rs.getInt("item_id"));
                }
            }
        }

        // Delete items that are not in the current update
        Set<Integer> updatedItemIds = Arrays.stream(itemIds)
                .map(id -> id + 1) // Increment each ID
                .boxed()          // Convert to Integer
                .collect(Collectors.toSet());
        Set<Integer> itemsToDelete = new HashSet<>(existingItemIds);
        itemsToDelete.removeAll(updatedItemIds);

        if (!itemsToDelete.isEmpty()) {
            String sqlDelete = "DELETE FROM `purchase_detail` WHERE `purchase_id` = ? AND `item_id` IN (" +
                    String.join(",", Collections.nCopies(itemsToDelete.size(), "?")) + ")";

            try (PreparedStatement stmDelete = jdbc.connection.get().prepareStatement(sqlDelete)) {
                int index = 1;
                stmDelete.setString(index++, purchaseId);
                for (int itemId : itemsToDelete) {
                    stmDelete.setInt(index++, itemId);
                }
                stmDelete.executeUpdate();
            }
        }

        // Update or insert items in sales_detail
        for (int i = 0; i < itemType; i++) {
            int itemId = itemIds[i] + 1;
            int quantity = quantities[i];
            // Only proceed if the amount is greater than 0
            if (quantity > 0) {
                int buyPrice = 0;
                String sqlSelectPrice = "SELECT `buy_price` FROM `item` WHERE `item_id` = ?";

                try (PreparedStatement stmSelectPrice = jdbc.connection.get().prepareStatement(sqlSelectPrice)) {
                    stmSelectPrice.setInt(1, itemId);
                    try (ResultSet rs = stmSelectPrice.executeQuery()) {
                        if (rs.next()) {
                            buyPrice = rs.getInt("buy_price");
                        }
                    }
                }

                int totalPrice = buyPrice * quantity;
                String sqlUpsertDetail = "INSERT INTO `purchase_detail` (" +
                        "`purchase_id`, " +
                        "`item_id`, " +
                        "`quantity`, " +
                        "`total_price`) " +
                        "VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "`quantity` = VALUES(`quantity`), " +
                        "`total_price` = VALUES(`total_price`)";

                try (PreparedStatement stmUpsertDetail = jdbc.connection.get().prepareStatement(sqlUpsertDetail)) {
                    stmUpsertDetail.setString(1, purchaseId);
                    stmUpsertDetail.setInt(2, itemId);
                    stmUpsertDetail.setInt(3, quantity);
                    stmUpsertDetail.setInt(4, totalPrice);
                    stmUpsertDetail.executeUpdate();
                }
            }
        }
    }
    public Map<Integer, Integer> getPurchaseDetailsByPurchaseId(int purchaseId) {
        String sql = "SELECT item_id, quantity FROM purchase_detail WHERE purchase_id = ?";
        Map<Integer, Integer> purchaseDetails = new HashMap<>();

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, purchaseId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int quantity = rs.getInt("quantity");
                    purchaseDetails.put(itemId, quantity);
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return purchaseDetails;
    }
}
