package com.Accio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Connection connection = null;
    public static Connection getConnection() {
        if(connection != null) {
            return connection;
        }
        String user = "root";
        String pwd = "Surya@18";
        String db = "searchengineapp";
        return getConnection(user, pwd, db);
    }
    private static Connection getConnection(String user, String pwd, String db) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            final String DB_URL = "jdbc:mysql://localhost:3306/"+ db;

            connection = DriverManager.getConnection(DB_URL,user,pwd);
            //connection = DriverManager.getConnection("jdbc:mysql://localhost/" + db + "?user=" + user + "&password=" + pwd);
        }
        catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }
}
