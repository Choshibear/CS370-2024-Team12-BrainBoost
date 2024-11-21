package com.brainboost;
public class User {
    private String username;
    private String password;
    public User() {
        username = null;
        password = null;
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}