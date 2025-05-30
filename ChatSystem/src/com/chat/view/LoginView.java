/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.view;


import com.chat.controller.LoginController;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {

    private LoginController controller;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    public boolean  sseccefully=false;

    public LoginView() {
        controller = new LoginController(this);
        initComponents();
        customizeComponents();
        layoutComponents();
    }

    private void initComponents() {
        // Set frame properties
        setTitle("Chat System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(400, 300));

        // Initialize components
        mainPanel = new JPanel();
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }

    private void customizeComponents() {
        // Main Panel
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(100, 100, 100));
        mainPanel.setForeground(Color.WHITE);

        // Fields
        emailField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));

        // Buttons
        loginButton.setPreferredSize(new Dimension(100, 35));
        registerButton.setPreferredSize(new Dimension(100, 35));

        loginButton.setBackground(new Color(0, 144, 19));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        registerButton.setBackground(new Color( 200,129,255 ));
        registerButton.setFocusPainted(false);

        // Add ActionListeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
    }

    private void layoutComponents() {
        // Set layout
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel(" Chat System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(emailLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(emailField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(mainPanel.getBackground());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridy = 5;
        mainPanel.add(buttonPanel, gbc);

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public boolean handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
           
            return false;
            
        }
        controller.login(email, password);
        
       return sseccefully;
        
    }

    private void handleRegister() {
        RegistrationView registe=new RegistrationView();
        registe.setVisible(true);
        this.dispose();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    public void showLoginSuccess() {
        showMessage("Login successful!");
        clearFields();
    }
}
