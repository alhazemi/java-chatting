/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.controller;

import com.chat.model.UsersModel;
import com.chat.view.LoginView;





public class LoginController {

    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }
    
        public void login(String email, String password) {
            UsersModel user = UsersModel.login(email, password);
        if (user != null) {
            view.showMessage("Login successful!");
        } else {
            view.showError("Invalid email or password");
        }
    } 
}