package com.brainboost;
public class User {
    private String username;
    private String password;
    private int quiz_id;
    public User() {
        username = null;
        password = null;
        quiz_id = 0;
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
    public int getQuiz_id() {
        return quiz_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }
}
