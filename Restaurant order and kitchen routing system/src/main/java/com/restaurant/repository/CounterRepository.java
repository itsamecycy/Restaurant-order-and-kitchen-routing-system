package com.restaurant.repository;

import com.restaurant.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CounterRepository {

    public boolean authenticate(String username, String password) {
        String query = "SELECT 1 FROM counter_users WHERE username = ? AND password = ?";

        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            return false;
        }
    }
}