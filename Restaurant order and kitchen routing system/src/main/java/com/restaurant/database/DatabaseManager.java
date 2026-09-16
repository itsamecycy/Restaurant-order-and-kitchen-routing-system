package com.restaurant.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseManager {

    private static final Path DATABASE_PATH = Paths.get("database", "counter.db");

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(DATABASE_PATH.getParent());
            Class.forName("org.sqlite.JDBC");
        } catch (java.io.IOException exception) {
            throw new SQLException("Could not create the database directory", exception);
        } catch (ClassNotFoundException exception) {
            throw new SQLException("SQLite JDBC driver is not available", exception);
        }

        Connection connection = DriverManager.getConnection(
                "jdbc:sqlite:" + DATABASE_PATH);
        initialize(connection);
        return connection;
    }

    private static void initialize(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS counter_users ("
                    + "username TEXT PRIMARY KEY, "
                    + "password TEXT NOT NULL"
                    + ")");
            statement.executeUpdate("INSERT OR IGNORE INTO counter_users "
                    + "(username, password) VALUES ('Counter', '12345')");
        }
    }
}