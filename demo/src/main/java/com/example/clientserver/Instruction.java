package com.example.clientserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;;

//Só vai enviar campos nao nulos
@JsonInclude(Include.NON_NULL)
public class Instruction {
    private String command;
    private String file;
    private String hash;

    public Instruction() {
    }

    // LIST
    public Instruction(String command) {
        this.command = command;
    }

    // PUT
    public Instruction(String command, String file, String hash) {
        this.command = command;
        this.file = file;
        this.hash = hash;

    }

    // GET
    public Instruction(String command, String file) {
        this.command = command;
        this.file = file;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
