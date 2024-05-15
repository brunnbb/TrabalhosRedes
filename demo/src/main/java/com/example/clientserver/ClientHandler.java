package com.example.clientserver;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.*;
import java.security.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static String hashFile(String filePath) throws NoSuchAlgorithmException, IOException {
        byte[] fileToBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(fileToBytes);
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }

    public static void sendFile(DataOutputStream dataToClient, String path)
            throws Exception {
        int bytes = 0;

        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Here we send the File to Server
        dataToClient.writeLong(file.length());
        // Here we break file into chunks
        byte[] buffer = new byte[4096];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            dataToClient.write(buffer, 0, bytes);
            dataToClient.flush();
        }

    }

    public static void readFile(DataInputStream dataFromClient, String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        long fileSize = dataFromClient.readLong();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while (fileSize > 0
                && (bytesRead = dataFromClient.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            fileSize -= bytesRead;
        }

        fileOutputStream.flush();

    }

    public static String[] listFiles() {
        File fatherDirectory = new File("src/main/java/com/example/clientserver/files/server");
        String[] list = fatherDirectory.list();

        return list;
    }

    public static Message createMessage(Instruction instruction, String filePath)
            throws NoSuchAlgorithmException, IOException {
        Message message;
        String fileThatArrivedHash = hashFile(filePath);
        if (instruction.getHash().equals(fileThatArrivedHash)) {
            message = new Message(instruction.getFile(), instruction.getCommand(), "sucess");
        } else {
            message = new Message(instruction.getFile(), instruction.getCommand(), "fail");
        }
        return message;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectMapper mapper = new ObjectMapper();
                DataOutputStream dataToClient = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dataFromClient = new DataInputStream(clientSocket.getInputStream());

                Instruction instruction = mapper.readValue(dataFromClient.readUTF(), Instruction.class);
                String filePath = "src/main/java/com/example/clientserver/files/server/" + instruction.getFile();
                String[] listOfAvailableFiles = listFiles();

                if (instruction.getCommand().equals("EXIT")) {
                    dataFromClient.close();
                    dataToClient.close();
                    clientSocket.close();
                    break;
                }
                String jsonToSend = null;
                switch (instruction.getCommand()) {
                    case "LIST":
                        jsonToSend = mapper.writeValueAsString(listOfAvailableFiles);
                        dataToClient.writeUTF(jsonToSend);
                        break;
                    case "PUT":
                        readFile(dataFromClient, filePath);
                        Message message = createMessage(instruction, filePath);
                        jsonToSend = mapper.writeValueAsString(message);
                        dataToClient.writeUTF(jsonToSend);
                        break;
                    case "GET":
                        // TO DO
                        sendFile(dataToClient, filePath);
                        break;
                }

            }

        } catch (Exception e) {

        }

    }

}
