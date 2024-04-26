package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import out.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(Files.readString(Path.of("data/programa.txt")));

        while (scanner.hasNext()) {
            System.out.println(scanner.nextToken());
        }
    }

}
