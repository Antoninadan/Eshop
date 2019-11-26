package com.mainacad.dao;

import com.mainacad.model.User;

import java.sql.*;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());

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
        User user = new User();
        int result = 0;

        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             Statement statement = connection.createStatement()) {
            preparedStatement.setInt(1, id);

            result = preparedStatement.executeUpdate();
            if (result == 1) {
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.first() == true) {
                    user.setId(resultSet.getInt("id"));
                    user.setLogin(resultSet.getString("login"));
                    user.setPassword(resultSet.getString("password"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhone(resultSet.getString("phone"));
                }
                resultSet.close();
            }

            } catch(SQLException e){
                e.printStackTrace();
            }
            return user;
        }

        public static User getByLoginAndPassword (String login, String password){
            String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
            return null;
        }

        public static User update (User user){
            String sql = "UPDATE users SET "
                    + "login = ?, "
                    + "password = ?, "
                    + "first_name = ?, "
                    + "last_name = ?, "
                    + "email = ?, "
                    + "phone = ? "
                    + "WHERE id = ?";
            try (Connection connection = ConnectionToDB.getConnection();
                 PreparedStatement preparedStatement =
                         connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getFirstName());
                preparedStatement.setString(4, user.getLastName());
                preparedStatement.setString(5, user.getEmail());
                preparedStatement.setString(6, user.getPhone());
                preparedStatement.setInt(7, user.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }

        public static void delete (Integer id){
            String sql = "DELETE FROM users WHERE id = ?";
            try (Connection connection = ConnectionToDB.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
