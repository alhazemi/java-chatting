package com.chat.view;

import com.chat.controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {

    private JTextArea chatArea;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton emojiButton;
    private JButton recordButton;
    private JDialog emojiDialog;

    private ClientController controller;

    private final String[] emojis = {
        "😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😉", "😊",
        "😋", "😎", "😍", "😘", "🥰", "😗", "😙", "😚", "🙂", "🤗",
        "🤩", "🤔", "🤨", "😐", "😑", "😶", "🙄", "😏", "😣", "😥",
        "😮", "🤐", "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝"
    };

    public ClientUI() {
        setTitle("Client Chat");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("CLIENT CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(label, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        messageArea = new JTextArea(3, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        inputPanel.add(msgScroll, BorderLayout.CENTER);

        emojiButton = new JButton("😊");
        emojiButton.setBackground(Color.YELLOW);
        emojiButton.setFocusPainted(false);
        emojiButton.addActionListener(e -> showEmojiPanel());

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(0, 122, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);

        recordButton = new JButton("🎙️");
        recordButton.setBackground(Color.GREEN.darker());
        recordButton.setForeground(Color.WHITE);
        recordButton.setFocusPainted(false);
        recordButton.addActionListener(e -> controller.recordAndSendAudio());

        JPanel buttonPanel = new JPanel(new BorderLayout(5, 5));
        buttonPanel.add(emojiButton, BorderLayout.WEST);
        buttonPanel.add(recordButton, BorderLayout.EAST);
        buttonPanel.add(sendButton, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        controller = new ClientController(this);

        sendButton.addActionListener(e -> {
            String msg = messageArea.getText().trim();
            if (!msg.isEmpty()) {
                controller.sendMessage(msg);
                messageArea.setText("");
            }
        });

        setVisible(true);
    }

    private void showEmojiPanel() {
        if (emojiDialog == null) {
            emojiDialog = new JDialog(this, false);
            emojiDialog.setUndecorated(true);
            emojiDialog.setSize(300, 300);
            emojiDialog.setLocationRelativeTo(emojiButton);

            JPanel panel = new JPanel(new GridLayout(5, 8, 4, 4));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            for (String emoji : emojis) {
                JButton btn = new JButton(emoji);
                btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                btn.setMargin(new Insets(2, 2, 2, 2));
                btn.addActionListener(e -> {
                    messageArea.append(emoji);
                    emojiDialog.setVisible(false);
                });
                panel.add(btn);
            }

            emojiDialog.add(new JScrollPane(panel));
        }

        emojiDialog.setVisible(true);
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
