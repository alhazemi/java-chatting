package com.chat.view;

import com.chat.controller.ClientController;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class ClientUI extends JFrame {

    private JTextPane chatArea;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton emojiButton;
    private JButton recordButton;
    private JButton imageButton;

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
        setSize(520, 550); // تكبير عرض الفورم
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // العنوان العلوي
        JLabel label = new JLabel("CLIENT CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(label, BorderLayout.NORTH);

        // منطقة المحادثة
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // أسفل الشاشة (الرسالة + الأزرار)
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        messageArea = new JTextArea(3, 30);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(messageArea);
        inputPanel.add(msgScroll, BorderLayout.CENTER);

        // إعدادات حجم الأزرار
        Dimension btnSize = new Dimension(60, 35);

        // زر الإيموجي
        emojiButton = new JButton("😊");
        emojiButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        
        emojiButton.setBackground(Color.YELLOW);
        emojiButton.setFocusPainted(false);
        emojiButton.setPreferredSize(btnSize);
        emojiButton.addActionListener(e -> showEmojiPanel());

        // زر الصورة
        imageButton = new JButton("📷");
         imageButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        imageButton.setBackground(Color.PINK);
        imageButton.setFocusPainted(false);
        imageButton.setPreferredSize(btnSize);
        imageButton.addActionListener(e -> controller.sendImage());

        // زر التسجيل الصوتي
        recordButton = new JButton("🎙️");
        recordButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        recordButton.setBackground(Color.GREEN.darker());
        recordButton.setForeground(Color.WHITE);
        recordButton.setFocusPainted(false);
        recordButton.setPreferredSize(btnSize);
        recordButton.addActionListener(e -> controller.recordAndSendAudio());

        // زر الإرسال
       sendButton = new JButton("➤"); // زر الإرسال على شكل سهم أنيق
        sendButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));// 
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

        // ترتيب الأزرار في صف واحد
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        buttonPanel.add(emojiButton);
        buttonPanel.add(imageButton);
        buttonPanel.add(recordButton);
        buttonPanel.add(sendButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        controller = new ClientController(this);
        setVisible(true);
    }

    // نافذة اختيار الإيموجي
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

    // عرض رسالة نصية في منطقة الدردشة
    public void appendMessage(String msg) {
        try {
            StyledDocument doc = chatArea.getStyledDocument();
            doc.insertString(doc.getLength(), msg + "\n\n", null);
            chatArea.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // عرض صورة في منطقة الدردشة
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
