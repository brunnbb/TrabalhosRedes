package com.example.clientserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

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
        System.out.println("PUT <file_name>");
        System.out.println("GET <file_name>");
        System.out.println("<EXIT>");
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
                    throw new IllegalArgumentException("GET command requires a file name");
                }
                instruction = new Instruction(command, fileName);
                break;
            default:
                throw new IllegalArgumentException("Invalid command");

        }
        return instruction;
    }

    public static void sendFile(String path)
            throws Exception {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Here we send the File to Server
        // dataOutputStream.writeLong(file.length());
        // Here we break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            // Send the file to Server Socket
            // dataOutputStream.write(buffer, 0, bytes);
            // dataOutputStream.flush();
        }
        // close the file here
        fileInputStream.close();
    }

    public static void readFile(String fileName) {

    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();
        Instruction instruction = new Instruction();

        Socket socket = new Socket("127.0.0.1", 6777);
        File file = null;
        String filePath = null;
        PrintWriter out; // Send instruction
        BufferedReader in; // Read Message
        DataOutputStream dataOutputStream; // Read file
        DataInputStream dataInputStream; // Send file

        boolean isValid = true;
        System.out.println("Welcome, please choose a command: ");
        while (true) {
            try {
                showCommandMenu();
                String command = input.nextLine();

                int startIndex = command.indexOf("<");
                int endIndex = command.indexOf(">");

                filePath = "src/main/java/com/example/clientserver/files/client/";
                String realCommand, fileName;

                if (startIndex != -1) {
                    realCommand = command.substring(0, startIndex).toUpperCase();
                    fileName = command.substring(startIndex + 1, endIndex);
                } else {
                    realCommand = command.toUpperCase();
                    fileName = null;
                }

                if (realCommand.equals("<EXIT>")) {
                    System.out.println("Exiting...");
                    break;
                }

                filePath += fileName;
                file = new File(filePath);

                instruction = createInstruction(realCommand, fileName, filePath);

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

            out = new PrintWriter(socket.getOutputStream(), true);
            out.print(payload);
            out.flush();

            if (instruction.getCommand().equals("PUT")) {
                sendFile(filePath);
            }

            switch (instruction.getCommand()) {
                case ("LIST"):
                    // Wait and show an arraylist
                    break;
                case ("PUT"):
                    // Wait for confirmation
                    break;
                case ("GET"):
                    // Receive message and calculate hash value
                    break;
            }
        }
        socket.close();
    }
}
