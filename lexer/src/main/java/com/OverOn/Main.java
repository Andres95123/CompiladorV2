package com.OverOn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.OverOn.LexerCompiler.lexerCompiler;

public class Main {

    public static void main(String[] args) throws IOException {

        // El usuario debe introducir el path por argumento
        // Si no introduce argumento, error

        if (args.length == 0) {
            System.err.println("Introduce el path del archivo de lexico a compilar");
            return;
        }

        // Si ponemos un path que no existe, error
        if (!Files.exists(Path.of(args[0]))) {
            System.err.println("El archivo no existe");
            return;
        }

        // Si añade un argumento extra para el output, guardarlo y sino usar el working
        // directory
        String output = args.length == 2 ? args[1] : System.getProperty("user.dir") + "\\Scanner.java";
        String input = args[0];

        lexerCompiler lexerCompiler = new lexerCompiler();

        try {
            lexerCompiler.compilerLexer(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
