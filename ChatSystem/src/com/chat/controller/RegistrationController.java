/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.controller;

import com.chat.model.UsersModel;
import com.chat.view.RegistrationView;

/**
 *
 * @author HP
 */
public class RegistrationController {
    
    
    private RegistrationView view;

    public RegistrationController(RegistrationView view) {
        this.view = view;
    }
    
        public void create
        ( 
            String full_name,
            String email,
            String password,
            String phone_number
        ) {
            UsersModel user =new UsersModel(full_name, email, password, phone_number);
        if (user.save()) {
            view.showMessage("Creating new account successful!");
        } else {
            view.showError("Creating new account failled!");
        }
    
        }
}
