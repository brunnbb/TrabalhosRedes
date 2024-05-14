package com.example.jsons;

import java.io.File;
import java.io.IOException;

import com.example.udpchat.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    public JsonParser() {

    }

    public void writeJson(Message message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String fileName = "src/main/java/com/example/msg.json";
            objectMapper.writeValue(new File(fileName), message);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Message readJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Message message = objectMapper.readValue(new File(filePath), Message.class);
            return message;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
