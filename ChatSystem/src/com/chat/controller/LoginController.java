/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.controller;

import com.chat.model.UsersModel;
import com.chat.view.ClientUI;
import com.chat.view.LoginView;
import com.chat.view.ServerUI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;





public class LoginController {

    private LoginView view;
    private UsersModel user;

    private String email;
    private String password;
    

    public LoginController(LoginView view) {
        this.view = view;
        
    }
    
        public void login(String email, String password) {
           
             user = UsersModel.login(email, password);
        if (user != null) {
            view.showMessage("Login successful!");
            
            ServerUI app= new ServerUI();
            app.setVisible(true);
            view.dispose();
            
        } else {
            view.showError("Invalid email or password");
        }
       
        
        }
        
        
         
         public void logout (){
         user.logout();
         }
         
     
}