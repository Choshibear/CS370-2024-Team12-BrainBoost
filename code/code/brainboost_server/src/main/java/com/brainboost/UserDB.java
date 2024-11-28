package com.brainboost;

import java.sql.*;

public class UserDB {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    
    public UserDB() {
        // First, initialize the database
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            
            //CREATE TABLES
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "username TEXT PRIMARY KEY," +
                        "password TEXT NOT NULL)");
            addUser("connor", "1234");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String addUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    return "false";
                }
        }
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return "true";
        } catch (SQLException e) {
            e.printStackTrace();
            return "false";
        }
    }
    
    public String checkUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if(password.equals(rs.getString("password"))){
                return "true";
            }

            return "false";
        } catch (SQLException e) {
            e.printStackTrace();
            return "false";
        }
    }
}
