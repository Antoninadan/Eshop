package com.mainacad.dao;

import com.mainacad.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static User save(User user) {
        String sql = "INSERT INTO users " +
                "(login, password, first_name, last_name, email, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String sequenceSQL = "SELECT currval(pg_get_serial_sequence('users','id'))";

        int result = 0;
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(sql);
             PreparedStatement sequenceStatement =
                     connection.prepareStatement(sequenceSQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhone());
            result = preparedStatement.executeUpdate();

            if (result == 1) {
                ResultSet resultSet = sequenceStatement.executeQuery();

                while (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    user.setId(id);
                    break;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return user;


    }

    public static User getById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        return null;
    }

    public static User getByLoginAndPassword(String login, String password) {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";

        return null;
    }

    public static User update(User user) {
        String sql = "UPDATE users SET "
                + "login = ?"
                + "password = ?"
                + "first_name = ?"
                + "last_name = ?"
                + "email = ?"
                + "phone = ?"
                + "WHERE id = ?";

        return user;
    }

    public static void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
    }


}
