package com.chat.controller;

import com.chat.model.MessageModel;

import java.sql.SQLException;
import java.util.List;

public class MessageController {

    private MessageModel messageModel;

    // إرسال رسالة من مستخدم لآخر
    public void sendMessage(int senderId, int receiverId, String messageText) {
        messageModel = new MessageModel(senderId, receiverId, messageText);
        if (!messageModel.sendMessage()) {
            System.err.println("فشل في إرسال الرسالة إلى قاعدة البيانات.");
        }
    }

    // تحميل المحادثة بين مستخدمين
    public List<MessageModel> loadConversation(int userA, int userB) {
        messageModel = new MessageModel();
        try {
            return messageModel.getAllMessage(userA, userB);
        } catch (SQLException e) {
            System.err.println("خطأ في تحميل المحادثة: " + e.getMessage());
            return null;
        }
    }
}
