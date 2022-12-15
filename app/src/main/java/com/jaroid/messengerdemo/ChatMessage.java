package com.jaroid.messengerdemo;

public class ChatMessage {
    String uid;
    String userName;
    String message;
    String time;

    public ChatMessage(String uid, String userName, String message, String time) {
        this.uid = uid;
        this.userName = userName;
        this.message = message;
        this.time = time;
    }

    public ChatMessage() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
