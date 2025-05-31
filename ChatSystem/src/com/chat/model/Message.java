// ملف جديد: Message.java
package com.chat.model;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String type; // "TEXT" أو "IMAGE"
    private Object content;

    public Message(String sender, String type, Object content) {
        this.sender = sender;
        this.type = type;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}
