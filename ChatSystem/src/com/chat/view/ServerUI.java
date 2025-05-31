package com.chat.view;

import com.chat.controller.ServerController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

public class ServerUI extends JFrame {

    private JTextPane chatArea;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton emojiButton;
    private JButton recordButton;
    private JButton imageButton;

    private JDialog emojiDialog;
    private ServerController controller;

    private final String[] emojis = {
            "😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😉", "😊",
            "😋", "😎", "😍", "😘", "🥰", "😗", "😙", "😚", "🙂", "🤗",
            "🤩", "🤔", "🤨", "😐", "😑", "😶", "🙄", "😏", "😣", "😥",
            "😮", "🤐", "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝"
    };

    public ServerUI() {
        setTitle("Server Chat");
        setSize(520, 550); // نفس حجم واجهة الكلاينت
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel label = new JLabel("SERVER CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(label, BorderLayout.NORTH);

        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setContentType("text/plain");
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        messageArea = new JTextArea(3, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        inputPanel.add(msgScroll, BorderLayout.CENTER);

        // إعداد حجم الأزرار
        Dimension btnSize = new Dimension(60, 35);

        emojiButton = new JButton("😊");
          emojiButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        emojiButton.setBackground(Color.YELLOW);
        emojiButton.setFocusPainted(false);
        emojiButton.setPreferredSize(btnSize);
        emojiButton.addActionListener(e -> showEmojiPanel());

        imageButton = new JButton("📷");
         imageButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        imageButton.setBackground(Color.PINK);
        imageButton.setFocusPainted(false);
        imageButton.setPreferredSize(btnSize);
        imageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = chooser.getSelectedFile();
                    byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    controller.sendImage(imageBytes);
                } catch (Exception ex) {
                    appendMessage("Error reading image file: " + ex.getMessage());
                }
            }
        });

        recordButton = new JButton("🎙️");
         recordButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));//
        recordButton.setBackground(Color.GREEN.darker());
        recordButton.setForeground(Color.WHITE);
        recordButton.setFocusPainted(false);
        recordButton.setPreferredSize(btnSize);
        recordButton.addActionListener(e -> controller.recordAndSendAudio());

        sendButton = new JButton("➤"); // زر الإرسال على شكل سهم أنيق
         sendButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));// // تكبير الرمز
        sendButton.setBackground(new Color(0, 122, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(btnSize);
        sendButton.addActionListener(e -> {
            String msg = messageArea.getText().trim();
            if (!msg.isEmpty()) {
                controller.sendMessage(msg);
                messageArea.setText("");
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.add(emojiButton);
        buttonPanel.add(imageButton);
        buttonPanel.add(recordButton);
        buttonPanel.add(sendButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        controller = new ServerController(this);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                controller.startServer();
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
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            doc.insertString(doc.getLength(), msg + "\n\n", null);
            chatArea.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendImage(byte[] imageBytes) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (img != null) {
                Image scaledImg = img.getScaledInstance(200, -1, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImg);

                StyledDocument doc = chatArea.getStyledDocument();
                Style style = chatArea.addStyle("ImageStyle", null);
                StyleConstants.setIcon(style, icon);

                doc.insertString(doc.getLength(), "ignored text", style);
                doc.insertString(doc.getLength(), "\n\n", null);
                chatArea.setCaretPosition(doc.getLength());
            } else {
                appendMessage("[Error displaying image]");
            }
        } catch (Exception e) {
            appendMessage("[Error displaying image: " + e.getMessage() + "]");
        }
    }
}
