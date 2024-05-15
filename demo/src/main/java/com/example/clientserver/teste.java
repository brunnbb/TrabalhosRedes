package com.example.clientserver;

import java.io.File;
import java.io.FileNotFoundException;
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
        Scanner input = new Scanner(System.in);
        String inputString = input.nextLine();
        String[] words = inputString.split("\\s+");
        String command = words[0].toUpperCase();
        String fileName = null;

        if (words.length > 1) {
            fileName = words[1];
        }

        System.out.println(command);
        System.out.println(fileName);

    }

    public static String hashFile(String filePath) throws NoSuchAlgorithmException, IOException {
        byte[] fileToBytes = Files.readAllBytes(Paths.get(filePath));
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(fileToBytes);
        BigInteger bigInteger = new BigInteger(1, messageDigest);
        return bigInteger.toString(16);
    }

    public static void readRandomFile() throws FileNotFoundException {
        File file = new File("src/main/java/com/example/clientserver/files/client/teste.txt");

        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            System.out.println(linha);
        }
        scanner.close();

    }

    public static void testarFileDirectory() {
        File fatherDirectory = new File("src/main/java/com/example/clientserver/files/server");
        String[] list = fatherDirectory.list();

        for (String item : list) {
            System.out.println(item);
        }

        System.out.println(list.toString());
    }

}
