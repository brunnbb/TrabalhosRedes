package com.example.clientserver;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class teste {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        File file = new File("src/main/java/com/example/clientserver/files/teste.txt");

        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            System.out.println(linha);
        }
    }

    public static String hashFile(String filePath) throws NoSuchAlgorithmException, IOException {
        byte[] fileToBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(fileToBytes);
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }
}
