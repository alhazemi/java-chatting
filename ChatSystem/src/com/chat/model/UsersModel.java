/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.model;


import com.chat.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsersModel {

    private int user_id;
    private String full_name;
    private String email;
    private String password;
    private String status;
    private String phone_number;
    

    public UsersModel(String full_name, String email, String password) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.status = "offline";
    }

    public UsersModel(String full_name, String email, String password, String phone_number) {
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.status = "offline";
    }

    public int getUser_id() {
        return user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean save() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "insert into users (full_name, email, password, status, phone_number)"
                + "values (?, ?, ?, ? ,?)";
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            pst.setString(1, this.full_name);
            pst.setString(2, this.email);
            pst.setString(3, this.password);
            pst.setString(4, this.status);
            pst.setString(5, this.phone_number);
           

            int affectRows = pst.executeUpdate();
            if (affectRows > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    this.user_id = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public static UsersModel login(String email, String password) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "select * from users where email = ? and password = ?";

        try (PreparedStatement pst = conn.prepareStatement(sql);) {
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                UsersModel user = new UsersModel(
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number")
                        
                );
                user.setUser_id(rs.getInt("user_id"));
                user.setStatus("online");
                user.updateStatus();
                return user;
            }
        } catch (SQLException ex) {

        }
        return null;

    }
     public boolean updateStatus() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "UPDATE USERS set status = ? where user_id = ?";

        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, status);
            pst.setInt(2, user_id);

            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    public boolean logout() {
        this.setStatus("offline");
        return this.updateStatus();
    }
}
