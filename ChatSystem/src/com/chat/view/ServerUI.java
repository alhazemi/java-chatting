package com.chat.view;

import com.chat.controller.ServerController;

import javax.swing.*;
import java.awt.*;

public class ServerUI extends JFrame {

    private JTextArea chatArea;
    private JTextArea messageArea;
    private JButton sendButton;

    private ServerController controller;

    public ServerUI() {
        setTitle("Server Chat");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

       
        JLabel label = new JLabel("SERVER CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(label, BorderLayout.NORTH);

        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

       
        JPanel inputPanel = new JPanel(new BorderLayout(5,5));
        messageArea = new JTextArea(3, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        inputPanel.add(msgScroll, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0, 122, 255)); 
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        controller = new ServerController(this);

        sendButton.addActionListener(e -> {
            String msg = messageArea.getText().trim();
            if (!msg.isEmpty()) {
                controller.sendMessage(msg);
                messageArea.setText("");
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                controller.startServer();
            }
        });
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerUI().setVisible(true));
    }
}
