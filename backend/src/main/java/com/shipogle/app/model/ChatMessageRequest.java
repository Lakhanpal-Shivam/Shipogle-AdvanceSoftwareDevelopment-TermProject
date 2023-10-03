package com.shipogle.app.model;

/**
 * Chat message request model.
 *
 * @author Rahul Saliya
 */
public class ChatMessageRequest {

    //private fields
    private int senderId;
    private int receiverId;
    private String message;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
