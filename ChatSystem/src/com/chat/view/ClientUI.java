package com.chat.view;

import com.chat.controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {

    private JTextArea chatArea;
    private JTextArea messageArea;
    private JButton sendButton;

    private ClientController controller;

    public ClientUI() {
        setTitle("Client Chat");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10,10));

        JLabel label = new JLabel("CLIENT CHAT", SwingConstants.CENTER);
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
        sendButton.setBackground(new Color(0, 122, 255)); // أزرق
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        controller = new ClientController(this);

        sendButton.addActionListener(e -> {
            String msg = messageArea.getText().trim();
            if (!msg.isEmpty()) {
                controller.sendMessage(msg);
                messageArea.setText("");
            }
        });

        controller.startClient();
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
