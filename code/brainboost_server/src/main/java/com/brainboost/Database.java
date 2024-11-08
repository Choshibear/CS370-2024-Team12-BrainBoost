package com.brainboost;

import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    
    public Database() {
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
    
    public void addUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO users (username, password) VALUES (?, ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getUser(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return "User found: " + rs.getString("username") + 
                       ", " + rs.getString("password");
            }
            return "User not found";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving user";
        }
    }
}
