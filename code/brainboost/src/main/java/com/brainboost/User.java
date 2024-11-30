package com.brainboost;
public class User {
    private String username; //holds the username of the user
    private String password; //holds the password of the user
    private int quiz_id; //holds the id of the current quiz the user is taking
    //constructor
    public User() {
        username = null;
        password = null;
        quiz_id = 0;
    }
    //constructor after logging in
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getQuiz_id() {
        return quiz_id;
    }
    //setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }
    public void logout() {
    username = null;
    password = null;
    quiz_id = 0;
    }
}
