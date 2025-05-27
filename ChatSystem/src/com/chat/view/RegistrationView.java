/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.view;

import javax.swing.JFrame;

import com.chat.controller.RegistrationController;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author HP
 */
public class RegistrationView extends JFrame{
    //status  
    private RegistrationController controlle;
    private JTextField  nameField ;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phone_number;
//    private  JComboBox status;
    private JButton saveButton;
    private JButton loginButton;
    private JPanel mainPanel;

    public RegistrationView() {
        controlle= new RegistrationController(this);
        initComponents();
        customizeComponents();
        layoutComponents();
//        status.addItem("offline");
//        status.addItem("online");
    }

    private void initComponents() {
        // Set frame properties
        setTitle("Chat System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(400, 300));

        // Initialize components
        mainPanel = new JPanel();
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        phone_number = new JTextField(20);
        saveButton = new JButton("Save");
        loginButton = new JButton("Login");
    }

    private void customizeComponents() {
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        emailField.setPreferredSize(new Dimension(200, 30));
        passwordField.setPreferredSize(new Dimension(200, 30));
        nameField.setPreferredSize(new Dimension(200, 30));
        phone_number.setPreferredSize(new Dimension(200, 30));
        
        saveButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setPreferredSize(new Dimension(100, 35));

        saveButton.setBackground(new Color(70, 130, 255));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        loginButton.setBackground(new Color(10, 127, 255));
        loginButton.setFocusPainted(false);
        loginButton.setForeground(Color.WHITE);

        // Add ActionListeners
        saveButton.addActionListener(e -> createAccount());
        loginButton.addActionListener(e -> handleLogin());
    }

    private void layoutComponents() {
        // Set layout
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel(" Create new account ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(titleLabel, gbc);

        // Name Field
         JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(nameLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(nameField, gbc);
        
        //// Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(emailLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(emailField, gbc);
        
        // Phon number Field
        JLabel phoneLabel = new JLabel("Phon Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
       
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(phoneLabel, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(phone_number, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(mainPanel.getBackground());
        buttonPanel.add(saveButton);
        buttonPanel.add(loginButton);

        gbc.gridy = 9;
        mainPanel.add(buttonPanel, gbc);

        // Add main panel to frame
        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private void createAccount() {
        String email = emailField.getText();
        String name = nameField.getText();
        String phon = phone_number.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phon.isEmpty()) {
            
            showError("Please fill in all fields");
            return;
        }

         controlle.create(name,email,password,phon);
    }

    private void handleLogin() {
        LoginView login=new LoginView();
        login.setVisible(true);
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
        nameField.setText("");
        phone_number.setText("");
    }

    public void showRegisterSuccess() {
        showMessage("Register successful!");
        clearFields();
    }
}


