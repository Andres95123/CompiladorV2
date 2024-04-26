package com.OverOn.LexerCompiler;

public class reservedWords {

    private static final String[] reservedWords = {
            "Hola"
    };

    private static final String[] startDeclarations = {
            "package"
    };

    // Creamos los hashSet
    private static final java.util.HashSet<String> reservedWordsSet = new java.util.HashSet<>(
            java.util.Arrays.asList(ArrayToUpper(reservedWords)));
    private static final java.util.HashSet<String> startDeclarationsSet = new java.util.HashSet<>(
            java.util.Arrays.asList(ArrayToUpper(startDeclarations)));

    private static String[] ArrayToUpper(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i].toUpperCase();
        }
        return newArray;
    }

    public static boolean isReservedWord(String word) {
        return reservedWordsSet.contains(word.toUpperCase());
    }

    public static boolean isStartDeclaration(String word) {
        return startDeclarationsSet.contains(word.toUpperCase());
    }
}
