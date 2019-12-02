package com.mainacad.dao;

import com.mainacad.model.Item;
import com.mainacad.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public static Item save(Item item) {
        String sql = "INSERT INTO items " +
                "(name, code, price, availability) " +
                "VALUES (?, ?, ?, ?)";
        String sequenceSQL = "SELECT currval(pg_get_serial_sequence('items','id'))";

        int result = 0;
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             PreparedStatement sequenceStatement =
                     connection.prepareStatement(sequenceSQL)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getCode());
            preparedStatement.setInt(3, item.getPrice());
            preparedStatement.setInt(4, item.getAvailability());
            result = preparedStatement.executeUpdate();

            if (result == 1) {
                ResultSet resultSet = sequenceStatement.executeQuery();
                while (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    item.setId(id);
                    break;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static List<Item> getAll() {
        String sql = "SELECT * FROM items";
        List<Item> items = new ArrayList<>();
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Item item = getItemFromTable(resultSet);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static List<Item> getAllAvailable() {
        String sql = "SELECT * FROM items WHERE availability > 0";
        List<Item> items = new ArrayList<>();
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Item item = getItemFromTable(resultSet);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    // !!!!!!!!!!!!List<ItemDTO> getAllByUserAndPeriod(User user, Long timeFrom, Long timeTo)   .... select rewrite  item, order cart
    public static List<ItemHeader> getAllNamesAndPricesPurchasedByUserInPeriod(User user, Long timeFrom, Long timeTo) {
        String sql = "SELECT i.id as itemId, i.name as item_name, i.price as item_price FROM carts c " +
                "JOIN orders o ON o.cart_id = c.id " +
                "JOIN items i ON i.id = o.item_id " +
                "WHERE c.user_id = ? " +
                "AND c.creation_time >=?  AND c.creation_time <=? " +
                "AND c.status = 2";
        List<ItemHeader> itemHeaders = new ArrayList<>();
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setLong(2, timeFrom);
            preparedStatement.setLong(3, timeTo);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ItemHeader itemHeader = new ItemHeader(
                        resultSet.getInt("itemId"),
                        resultSet.getString("item_name"),
                        resultSet.getInt("item_price")
                );
                itemHeaders.add(itemHeader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemHeaders;
    }

    public static Item getById(Integer id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = getItemFromTable(resultSet);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Item update(Item item) {
        String sql = "UPDATE items SET " +
                "name=?, " +
                "code=?, " +
                "price=?, " +
                "availability=?" +
                "WHERE id = ?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getCode());
            preparedStatement.setInt(3, item.getPrice());
            preparedStatement.setInt(4, item.getAvailability());
            preparedStatement.setInt(5, item.getId());

            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(Integer id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Item getItemFromTable(ResultSet resultSet) throws SQLException {
        return new Item(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("code"),
                resultSet.getInt("price"),
                resultSet.getInt("availability"));
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemHeader {
        Integer id;
        String name;
        Integer price;
    }
}
/*
select u.first_name, c.user_id, SUM(i.price * o.amount), c.creation_time
from items i
join orders o on o.item_id = i.id
JOIN carts c ON c.id = o.cart_id
JOIN users u ON c.user_id = u.id
GROUP BY u.first_name, c.user_id, c.creation_time
ORDER BY c.creation_time


select u.first_name, c.user_id, SUM(i.price * o.amount), c.creation_time
from items i
JOIN (SELECT * FROM orders WHERE amount >200) AS o ON o.item_id = i.id
JOIN carts c ON c.id = o.cart_id
JOIN users u ON c.user_id = u.id
GROUP BY u.first_name, c.user_id, c.creation_time
ORDER BY c.creation_time

*/