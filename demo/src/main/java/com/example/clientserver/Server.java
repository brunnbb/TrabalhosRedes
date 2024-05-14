package com.example.clientserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Server {
    public static String hashFileSting(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input);
        BigInteger bigInteger = new BigInteger(1, messageDigest);

        return bigInteger.toString(16);
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6777);

        while (true) {
            System.out.println("[SERVER] Wainting for client connection...");
            Socket socket = serverSocket.accept();
            System.out.println("[SERVER] Connected to client!");
        }

        /*
         * FileInputStream file = new FileInputStream("C:\\Users\\bruno\\test.txt");
         * byte[] b = new byte[2000];
         * file.read(b, 0, b.length);
         * 
         * OutputStream outputStream = socket.getOutputStream();
         * outputStream.write(b, 0, b.length);
         * socket.close();
         * file.close();
         * serverSocket.close();
         */

    }

}