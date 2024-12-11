package com.brainboost.frames;


import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.brainboost.ServerAPI;
import com.brainboost.User;
//import java.awt.event.*;

public class LoginFrame extends JPanel {
  JLabel statusLabel = new JLabel("");
  public LoginFrame(JFrame frame) {
    
    // Title Panel
    JPanel titlePanel = new JPanel();
    JLabel title = new JLabel("BrainBoost");
    title.setFont(new Font("Arial", Font.BOLD, 48)); 
    titlePanel.add(title);

    
    
    // Form Panel
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBorder(BorderFactory.createEmptyBorder(60, 300, 60, 300));
    JLabel userLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField(20);
    JLabel passwordLabel = new JLabel("Password:");
    JTextField passwordField = new JPasswordField(20);
    statusLabel = new JLabel("Enter Username and Password");
    statusLabel.setMaximumSize(new Dimension(300, 20));
    
    // Check Server Status if online
    checkServerStatus();
    
    // Set Font
    userLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
    usernameField.setFont(new Font("Arial", Font.PLAIN, 18)); 
    passwordLabel.setFont(new Font("Arial", Font.BOLD, 18)); 
    passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
    statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
    
    //add components to panel
    formPanel.add(userLabel);
    formPanel.add(usernameField);
    formPanel.add(passwordLabel);
    formPanel.add(passwordField);
    formPanel.add(statusLabel);
    
    // Button Panel
    JPanel buttonPanel = new JPanel();
    JButton loginButton = new JButton("Login");
    JButton registerButton = new JButton("Register");

    // Set Font
    loginButton.setFont(new Font("Arial", Font.BOLD, 24));
    registerButton.setFont(new Font("Arial", Font.BOLD, 24));

    //Button Logic
    loginButton.addActionListener(e -> {
      User user = new User(usernameField.getText(), passwordField.getText());
      if(login(user))
      {
        System.out.println("Login successful");
        frame.setContentPane(new MenuFrame(frame,user));
        frame.revalidate();
      }
      else
      {
        System.out.println("Invalid username or password");
        statusLabel.setText("Invalid Username or Password.");
        statusLabel.setForeground(java.awt.Color.RED);
      }
    });

    registerButton.addActionListener(e -> {
        User user = new User(usernameField.getText(), passwordField.getText());
        System.out.println("Registering new user: " + user.getUsername());
        register(user); //call register method
    });
    buttonPanel.add(loginButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); 
    buttonPanel.add(registerButton);
    
    // Panelception
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); 
    mainPanel.add(titlePanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 30))); 
    mainPanel.add(formPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    mainPanel.add(buttonPanel);
    add(mainPanel);
  }

  public boolean login(User user) {
      //login logic
      try {    
        return ServerAPI.sendMessage("checkUser," + user.getUsername() + "," + user.getPassword()).equals("true");
      } catch (Exception ex) {
        System.out.println("Error logging in: " + ex.getMessage());
        return false;
      }
  }

  public void register(User user) {
    //register logic
    try {    
      if(ServerAPI.sendMessage("registerUser," + user.getUsername() + "," + user.getPassword()).equals("true"))
      {
          System.out.println("Registered new user: " + user.getUsername());
          statusLabel.setText("Registered new user: " + user.getUsername());
          statusLabel.setForeground(java.awt.Color.GREEN);
          statusLabel.setForeground(java.awt.Color.GREEN);
      }
      else
      {
          System.out.println("Username already exists");
          statusLabel.setText("Username already exists");
          statusLabel.setForeground(java.awt.Color.RED);
      } 
    } catch (Exception ex) {
      System.out.println("Error logging in: " + ex.getMessage());
      statusLabel.setText("Error logging in: " + ex.getMessage());
      statusLabel.setForeground(java.awt.Color.RED);
    }
  }
  public void checkServerStatus() {
    try {    
      if(ServerAPI.sendMessage("checkServer").equals("true"))
      {
          System.out.println("Server is online");
      }
      else
      {
          System.out.println("Server is offline");
          statusLabel.setText("Server is offline, please try again later.");
          statusLabel.setForeground(java.awt.Color.RED);
      } 
    } catch (Exception ex) {
          statusLabel.setText("Server is offline, please try again later.");
          statusLabel.setForeground(java.awt.Color.RED);
          
    }
  }
}
