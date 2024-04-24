package sintactico;

import java.io.FileWriter;
import java.io.IOException;

import lexico.ParserLexOptions;
import util.Token;

public class fileMaker {
    private String[] tokensEnums;

    public fileMaker(String[] tokens, String fileName) {
        this.tokensEnums = tokens;
        createEnumFile(fileName);
    }

    private void createEnumFile(String fileName) {
        StringBuilder enumContent = new StringBuilder();
        // Añadimos el package
        enumContent.append("package sintactico;\n\n");
        enumContent.append("public enum " + fileName + " {\n");

        for (String token : tokensEnums) {
            enumContent.append("\t").append(token).append(",\n");
        }

        enumContent.append("}");

        try {
            String filePath = "C:/Users/andre/OneDrive/Documentos/GitClone/CompiladorV2/src/sintactico/" + fileName
                    + ".java";
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(enumContent.toString());
            fileWriter.close();
            System.out.println("Enum file created successfully at " + filePath);
        } catch (IOException e) {
            System.out.println("Error creating enum file: " + e.getMessage());
        }
    }
}
