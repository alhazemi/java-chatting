package com.chat.model;

import com.chat.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageModel {

    private int message_id;
    private int sender_id;
    private int receiver_id;
    private String message_text;
    private Timestamp send_at;
    private String is_read;

    private static final Connection conn = DatabaseConnection.getInstance().getConnection();

    public MessageModel(int sender_id, int receiver_id, String message_text) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_text = message_text;
        this.send_at = new Timestamp(System.currentTimeMillis());
        this.is_read = "no";
    }

    public MessageModel(int sender_id, int receiver_id, String message_text, Timestamp send_at, String is_read) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message_text = message_text;
        this.send_at = send_at;
        this.is_read = is_read;
    }

    public MessageModel() {}

    // Getters and Setters
    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public Timestamp getSend_at() {
        return send_at;
    }

    public void setSend_at(Timestamp send_at) {
        this.send_at = send_at;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    // إرسال رسالة وتخزينها في قاعدة البيانات
    public boolean sendMessage() {
        String sql = "INSERT INTO massages (sender_id, receiver_id, message_text, sand_at, is_read) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, this.sender_id);
            pst.setInt(2, this.receiver_id);
            pst.setString(3, this.message_text);
            pst.setTimestamp(4, this.send_at);
            pst.setString(5, this.is_read);

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    this.message_id = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // جلب جميع الرسائل بين مستخدمين
    public List<MessageModel> getAllMessage(int sender_id, int receiver_id) throws SQLException {
        String sql = "SELECT * FROM massages WHERE ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) ORDER BY sand_at";
        List<MessageModel> msgs = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sender_id);
            ps.setInt(2, receiver_id);
            ps.setInt(3, receiver_id);
            ps.setInt(4, sender_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                MessageModel msg = new MessageModel();
                msg.setMessage_id(rs.getInt("id"));
                msg.setSender_id(rs.getInt("sender_id"));
                msg.setReceiver_id(rs.getInt("receiver_id"));
                msg.setMessage_text(rs.getString("message_text"));
                msg.setSend_at(rs.getTimestamp("sand_at")); // تطابق اسم العمود
                msg.setIs_read(rs.getString("is_read"));
                msgs.add(msg);
            }
        }
        return msgs;
    }

    // حذف رسالة بواسطة المرسل
    public boolean deleteMessage(int messageId, int userId) {
        String sql = "DELETE FROM massages WHERE id = ? AND sender_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(MessageModel.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}
