package com.brainboost.frames;


import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {

        setTitle("BrainBoost");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("BrainBoost");
        title.setFont(new Font("Arial", Font.BOLD, 32)); // Make title bigger
        titlePanel.add(title);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(120, 300, 120, 300));
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(userLabel);
        formPanel.add(userTextField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

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

    public void login() {
      //login logic
      System.out.println("Loggin in...");
    }

    public void register() {
      //register logic
    }
}