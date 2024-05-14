package com.example.clientserver;

public class Message {
    private String file;
    private String operation;
    private String status;

    public Message(String file, String operation, String status) {
        this.file = file;
        this.operation = operation;
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
