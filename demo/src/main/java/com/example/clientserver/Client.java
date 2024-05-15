package com.example.clientserver;

import java.io.*;
import java.math.*;
import java.net.*;
import java.nio.file.*;
import java.security.*;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    private static DataOutputStream dataToServer = null;
    private static DataInputStream dataFromServer = null;

    public static String hashFile(String filePath) throws NoSuchAlgorithmException, IOException {
        byte[] fileToBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(fileToBytes);
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }

    public static void showCommandMenu() {
        System.out.println("---------------------");
        System.out.println("LIST");
        System.out.println("PUT file_name");
        System.out.println("GET file_name");
        System.out.println("EXIT");
        System.out.println("---------------------");
    }

    public static Instruction createInstruction(String command, String fileName, String filePath)
            throws IOException, NoSuchAlgorithmException {

        Instruction instruction = new Instruction();

        switch (command) {
            case ("LIST"):
                instruction = new Instruction(command);
                break;
            case ("PUT"):
                if (fileName == null) {
                    throw new IllegalArgumentException("PUT command requires a valid file name");
                }
                String hash = hashFile(filePath);
                instruction = new Instruction(command, fileName, hash);
                break;
            case ("GET"):
                if (fileName == null) {
                    throw new IllegalArgumentException("GET command requires a valid file name");
                }
                instruction = new Instruction(command, fileName);
                break;
            case ("EXIT"):
                instruction = new Instruction(command);
                break;
            default:
                throw new IllegalArgumentException("Invalid command");

        }
        return instruction;
    }

    public static void sendFile(String path) throws Exception {
        int bytes = 0;

        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Here we send the File to Server
        dataToServer.writeLong(file.length());
        // Here we break file into chunks
        byte[] buffer = new byte[4096];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            dataToServer.write(buffer, 0, bytes);
            dataToServer.flush();
        }
        fileInputStream.close();

    }

    public static void readFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        long fileSize = dataFromServer.readLong();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while (fileSize > 0
                && (bytesRead = dataFromServer.read(buffer, 0, (int) Math.min(buffer.length, fileSize))) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            fileSize -= bytesRead;
        }

        fileOutputStream.flush();
        fileOutputStream.close();

    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();
        Instruction instruction = new Instruction();

        Socket socket = new Socket("127.0.0.1", 6777);
        String filePath = null;

        dataToServer = new DataOutputStream(socket.getOutputStream());
        dataFromServer = new DataInputStream(socket.getInputStream());

        boolean isValid = true;
        System.out.println("Welcome, please choose a command: ");
        while (true) {
            try {
                showCommandMenu();
                String inputString = input.nextLine();
                String[] words = inputString.split("\\s+");
                String command = words[0].toUpperCase();
                String fileName = null;
                if (words.length > 1) {
                    fileName = words[1];
                }

                filePath = "src/main/java/com/example/clientserver/files/client/" + fileName;

                instruction = createInstruction(command, fileName, filePath);

            } catch (IllegalArgumentException e) {
                e.getMessage();
                isValid = false;
            }

            catch (Exception e) {
                System.out.println("Error: An unexpected error occurred");
                isValid = false;
            }

            if (!isValid) {
                isValid = true;
                continue;
            }

            String payload = objectMapper.writeValueAsString(instruction);
            dataToServer.writeUTF(payload);

            if (instruction.getCommand().equals("EXIT")) {
                System.out.println("Exiting...");
                break;
            }

            if (instruction.getCommand().equals("PUT")) {
                sendFile(filePath);
            }

            ObjectMapper mapper = new ObjectMapper();

            switch (instruction.getCommand()) {
                case ("LIST"):
                    String jsonListOfFiles = dataFromServer.readUTF();
                    String[] listOfFiles = mapper.readValue(jsonListOfFiles, String[].class);
                    System.out.println("List of available files:");
                    for (String fileName : listOfFiles) {
                        System.out.println(fileName);
                    }
                    break;
                case ("PUT"):
                    String jsonMessageConfirmation = dataFromServer.readUTF();
                    Message message = mapper.readValue(jsonMessageConfirmation, Message.class);

                    while (message.getStatus().equals("fail")) {
                        sendFile(filePath);
                        jsonMessageConfirmation = dataFromServer.readUTF();
                        message = mapper.readValue(jsonMessageConfirmation, Message.class);
                    }

                    break;
                case ("GET"):
                    // TO DO

                    break;
            }
        }
        input.close();
        socket.close();

    }
}
