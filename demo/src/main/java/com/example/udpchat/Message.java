package com.example.udpchat;

public class Message {

    private String date;
    private String time;
    private String username;
    private String message;

    public Message() {
    }

    public Message(String date, String time, String username, String message) {
        this.date = date;
        this.time = time;
        this.username = username;
        this.message = message;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{date: " + date + ", time: " + time + ", username: " + username + ", message: "
                + message + "}";
    }

}
