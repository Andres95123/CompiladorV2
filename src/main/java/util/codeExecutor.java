package main.java.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;

public class codeExecutor {

    public static void execute(String code) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String program = "package tmp; public class tmpCode { public static void main(String args[]) " + code + " }";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/tmp/tmpCode.java"))) {
            writer.write(program);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = compiler.run(null, null, null, "src/tmp/tmpCode.java");

        if (result == 0) {
            try {
                Process process = Runtime.getRuntime().exec("java src/tmp/tmpCode.java");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}