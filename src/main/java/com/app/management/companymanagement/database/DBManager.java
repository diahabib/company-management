package com.app.management.companymanagement.database;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {
    private static final String url;
    private static final String user;
    private static final String password;

    static {
        try (InputStream input = JDBCUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

            // Charger le driver
            Class.forName(properties.getProperty("db.driver"));

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erreur de chargement des configurations JDBC", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
