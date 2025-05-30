/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.controller;


import com.chat.model.MessageModel;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author HP
 */
public class MessageController {
    
     private   MessageModel messageModel;
    
        public void sendMessage(int sand_id ,int r_id,String message ) {
            messageModel=new MessageModel(sand_id, r_id, message);
            if(!messageModel.sendMessage()){
            
            }
    }

    public void loadConversation(int userA, int userB) {
            List<MessageModel> messages ;
            try{
             messages = messageModel.getAllMessage(userA, userB);
                    for(MessageModel msag:messages){
                          
                 }
            }catch(SQLException e){
            
            }
    }
}