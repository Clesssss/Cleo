package com.example.prototype.dao;

import com.example.prototype.JDBC;
import com.example.prototype.entity.DailySales;
import com.example.prototype.entity.Sales;
import javafx.scene.control.Label;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.example.prototype.JDBC.LOGGER;

public class SalesInvoiceDAO {
    private final JDBC jdbc = new JDBC();
    public int getBuyPrice(int itemId) {
        AtomicReference<Integer> buyPrice = new AtomicReference<>(0);
        String sql = "SELECT buy_price FROM item WHERE item_id = ?";

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, itemId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        buyPrice.set(rs.getInt("buy_price"));
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return buyPrice.get();
    }
    public void setPriceLabel(Map<Integer, Label> priceLabelMap){
        DecimalFormat numberFormatter = new DecimalFormat("#,###");
        String sql = "SELECT item_id, sell_price FROM item WHERE item_id BETWEEN 1 AND 10";
        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int sellPrice = rs.getInt("sell_price");

                    Label priceLabel = priceLabelMap.get(itemId);
                    if (priceLabel != null) {

                        priceLabel.setText(numberFormatter.format(sellPrice));
                    }
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }


    public Sales getSalesById(int salesId) {
        String sql = "SELECT store_name, area, discount_amount, is_bill FROM sales WHERE sales_id = ?";
        Sales sales = new Sales();

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, salesId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    sales.setStoreName(rs.getString("store_name"));
                    sales.setArea(rs.getString("area"));
                    sales.setDiscountAmount(rs.getString("discount_amount"));
                    sales.setIsBill(rs.getString("is_bill"));

                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return sales;
    }

    public Map<Integer, Integer> getSalesDetailsBySalesId(int salesId) {
        String sql = "SELECT item_id, amount FROM sales_detail WHERE sales_id = ?";
        Map<Integer, Integer> salesDetails = new HashMap<>();

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, salesId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("item_id");
                    int amount = rs.getInt("amount");
                    salesDetails.put(itemId, amount);
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return salesDetails;
    }

    public List<Sales> getSalesByDate(LocalDate date) throws SQLException {
        String sql = "SELECT sales_id, store_name FROM sales WHERE date = ?";
        List<Sales> salesInvoices = new ArrayList<>();

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(date));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Sales sales = new Sales();
                    sales.setSalesId(resultSet.getString("sales_id"));
                    sales.setStoreName(resultSet.getString("store_name"));
                    salesInvoices.add(sales);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return salesInvoices;
    }

    public Collection<DailySales> getDailySales(LocalDate date) {
        String itemSql = "SELECT item_id, name, sell_price FROM item";
        String salesIdSql = "SELECT sales_id FROM sales WHERE date = ?";
        String totalAmountSqlTemplate = "SELECT SUM(amount) AS total_amount FROM sales_detail WHERE item_id = ? AND sales_id IN (%s)";

        Collection<DailySales> dailySales = new ArrayList<>();

        jdbc.connection.ifPresent(conn -> {
            try (PreparedStatement salesIdStmt = conn.prepareStatement(salesIdSql)) {
                salesIdStmt.setDate(1, java.sql.Date.valueOf(date));
                ResultSet salesIdResultSet = salesIdStmt.executeQuery();

                StringBuilder salesIds = new StringBuilder();
                while (salesIdResultSet.next()) {
                    if (salesIds.length() > 0) {
                        salesIds.append(", ");
                    }
                    salesIds.append(salesIdResultSet.getInt("sales_id"));
                }

                if (salesIds.length() > 0) {
                    // Properly format the totalAmountSql query
                    String salesIdList = salesIds.toString();
                    String totalAmountSql = String.format(totalAmountSqlTemplate, salesIdList);

                    try (Statement itemStmt = conn.createStatement();
                         ResultSet itemResultSet = itemStmt.executeQuery(itemSql);
                         PreparedStatement totalAmountStmt = conn.prepareStatement(totalAmountSql)) {

                        while (itemResultSet.next()) {
                            String itemId = itemResultSet.getString("item_id");
                            String itemName = itemResultSet.getString("name");
                            int itemPrice = itemResultSet.getInt("sell_price");

                            totalAmountStmt.setString(1, itemId);
                            try (ResultSet totalAmountResultSet = totalAmountStmt.executeQuery()) {

                                int totalAmount = 0;
                                if (totalAmountResultSet.next()) {
                                    totalAmount = totalAmountResultSet.getInt("total_amount");
                                }

                                int totalPrice = totalAmount * itemPrice;
                                DailySales dailySale = new DailySales(itemId, itemName, String.valueOf(totalAmount), String.valueOf(itemPrice), String.valueOf(totalPrice));
                                dailySales.add(dailySale);
                            }
                        }

                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "SQL error in getDailySales", ex);
            }
        });

        return dailySales;
    }
    public int getTotalSumPrice(Collection<DailySales> dailySales) {
        return dailySales.stream()
                .mapToInt(ds -> Integer.parseInt(ds.getTotalPrice()))
                .sum();
    }
    public int getTotalDiscount(String date) throws SQLException {
        String sql = "SELECT SUM(discount_amount) AS total_discount FROM sales WHERE date = ?";
        int totalDiscount = 0;

        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql)) {
            stm.setString(1, date);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    totalDiscount = rs.getInt("total_discount");
                }
            }
        }

        return totalDiscount;
    }

    public int addSalesInvoice(
            String storeName,
            String area,
            String date,
            int itemType,
            int[] itemIds,
            int[] amounts,
            int discountAmount,
            boolean isBill
    ) throws SQLException {
        String sql = "INSERT INTO `sales`(" +
                "`store_name`, " +
                "`area`, " +
                "`date`, " +
                "`total_sales`, " +
                "`discount_amount`, " +
                "`is_bill`)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        // Prepare the statement with auto-generated keys
        try (PreparedStatement stm = jdbc.connection.get().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, storeName);
            stm.setString(2, area);
            stm.setString(3, date);

            // Calculate total sales
            int totalSales = 0;
            for (int i = 0; i < itemType; i++) {
                int itemId = itemIds[i] + 1;
                int amount = amounts[i];
                int sellPrice = 0;

                String sql3 = "SELECT `sell_price` FROM `item` WHERE `item_id` = ?";
                try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                    stm3.setInt(1, itemId);
                    try (ResultSet rs = stm3.executeQuery()) {
                        if (rs.next()) {
                            sellPrice = rs.getInt("sell_price");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                totalSales += sellPrice * amount;
            }
            stm.setInt(4, totalSales); // Set total_sales
            stm.setInt(5, discountAmount); // Set discount_amount
            stm.setInt(6, isBill ? 1 : 0); // Set is_bill (0 or 1)

            stm.executeUpdate();

            // Retrieve the generated sales_id
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int salesId = generatedKeys.getInt(1);

                    // Insert into sales_detail
                    for (int i = 0; i < itemType; i++) {
                        int itemId = itemIds[i] + 1;
                        int amount = amounts[i];
                        int sellPrice = 0;

                        String sql3 = "SELECT `sell_price` FROM `item` WHERE `item_id` = ?";
                        try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                            stm3.setInt(1, itemId);
                            try (ResultSet rs = stm3.executeQuery()) {
                                if (rs.next()) {
                                    sellPrice = rs.getInt("sell_price");
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        int totalPrice = sellPrice * amount;
                        String sql2 = "INSERT INTO `sales_detail`(" +
                                "`sales_id`, " +
                                "`item_id`, " +
                                "`amount`, " +
                                "`total_price`)" +
                                " VALUES (?, ?, ?, ?)";

                        try (PreparedStatement stm2 = jdbc.connection.get().prepareStatement(sql2)) {
                            stm2.setInt(1, salesId);
                            stm2.setInt(2, itemId);
                            stm2.setInt(3, amount);
                            stm2.setInt(4, totalPrice);
                            stm2.executeUpdate();
                        }
                    }

                    return salesId; // Return the generated sales ID
                } else {
                    throw new SQLException("Failed to retrieve generated sales ID.");
                }
            }
        }
    }
    public void updateSalesInvoice(
            String salesId,
            String storeName,
            String area,
            String date,
            int itemType,
            int[] itemIds,
            int[] amounts,
            int discountAmount,
            boolean isBill
    ) throws SQLException {
        // Update the main sales record
        String sqlUpdateSales = "UPDATE `sales` SET " +
                "`store_name` = ?, " +
                "`area` = ?, " +
                "`date` = ?, " +
                "`total_sales` = ?, " +
                "`discount_amount` = ?, " +
                "`is_bill` = ? " +
                "WHERE `sales_id` = ?";
        PreparedStatement stm = jdbc.connection.get().prepareStatement(sqlUpdateSales);
        stm.setString(1, storeName);
        stm.setString(2, area);
        stm.setString(3, date);
        // Calculate total sales
        int totalSales = 0;
        for(int i = 0; i < itemType; i++){
            int itemId = itemIds[i] + 1;
            int amount = amounts[i];
            int sellPrice = 0;
            String sql3 = "SELECT `sell_price` FROM `item` WHERE `item_id` = ?";
            try (PreparedStatement stm3 = jdbc.connection.get().prepareStatement(sql3)) {
                stm3.setInt(1, itemId);
                try (ResultSet rs = stm3.executeQuery()) {
                    if (rs.next()) {
                        sellPrice = rs.getInt("sell_price");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            totalSales += sellPrice * amount;
        }
        stm.setInt(4, totalSales);
        stm.setInt(5, discountAmount);
        stm.setInt(6, isBill ? 1 : 0);
        stm.setString(7, salesId);
        stm.executeUpdate();

        // Retrieve existing item IDs for this sales_id
        Set<Integer> existingItemIds = new HashSet<>();
        String sqlSelectDetails = "SELECT `item_id` FROM `sales_detail` WHERE `sales_id` = ?";

        try (PreparedStatement stmSelectDetails = jdbc.connection.get().prepareStatement(sqlSelectDetails)) {
            stmSelectDetails.setString(1, salesId);
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
            String sqlDelete = "DELETE FROM `sales_detail` WHERE `sales_id` = ? AND `item_id` IN (" +
                    String.join(",", Collections.nCopies(itemsToDelete.size(), "?")) + ")";

            try (PreparedStatement stmDelete = jdbc.connection.get().prepareStatement(sqlDelete)) {
                int index = 1;
                stmDelete.setString(index++, salesId);
                for (int itemId : itemsToDelete) {
                    stmDelete.setInt(index++, itemId);
                }
                stmDelete.executeUpdate();
            }
        }

        for (int i = 0; i < itemType; i++) {
            int itemId = itemIds[i] + 1;
            int amount = amounts[i];
            if (amount > 0) {
                int sellPrice = 0;
                String sqlSelectPrice = "SELECT `sell_price` FROM `item` WHERE `item_id` = ?";

                try (PreparedStatement stmSelectPrice = jdbc.connection.get().prepareStatement(sqlSelectPrice)) {
                    stmSelectPrice.setInt(1, itemId);
                    try (ResultSet rs = stmSelectPrice.executeQuery()) {
                        if (rs.next()) {
                            sellPrice = rs.getInt("sell_price");
                        }
                    }
                }

                int totalPrice = sellPrice * amount;
                String sqlUpsertDetail = "INSERT INTO `sales_detail` (" +
                        "`sales_id`, " +
                        "`item_id`, " +
                        "`amount`, " +
                        "`total_price`) " +
                        "VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "`amount` = VALUES(`amount`), " +
                        "`total_price` = VALUES(`total_price`)";

                try (PreparedStatement stmUpsertDetail = jdbc.connection.get().prepareStatement(sqlUpsertDetail)) {
                    stmUpsertDetail.setString(1, salesId);
                    stmUpsertDetail.setInt(2, itemId);
                    stmUpsertDetail.setInt(3, amount);
                    stmUpsertDetail.setInt(4, totalPrice);
                    stmUpsertDetail.executeUpdate();
                }
            }
        }
    }
}
