package com.File;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Parser {
    private static int offset = 0;
    private static int line = 1;
    private static byte[] bytes;        //letters on the screen   array full of chars

    public static void openFile(String filename) {
        File tmp = new File("levels/" + filename + ".zip");
        if(!tmp.exists()) {
            return;
        }
        try {
            ZipFile zipFile = new ZipFile("levels/" + filename + ".zip");
            ZipEntry jsonFile = zipFile.getEntry(filename + ".json");
            InputStream stream = zipFile.getInputStream(jsonFile);

            Parser.bytes = stream.readAllBytes();       //not the most efficient (reading through the file twice
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static boolean atEnd() {
        return offset == bytes.length;  //if offset is at the end of the bytes
    }

    public static char peek() {
        return (char)bytes[offset];
    }

    public static char advance() {
        char c = (char)bytes[offset++];
        return c;
    }

    public static void consume(char c) {
        char actual = peek();
        if(actual != c) {
            System.out.println("Error: Expected " + c + " but instead got " + actual + " at line " + Parser.line);
            System.exit(-1);
        }
        offset++;
    }

    public static void skipWhiteSpaces() {
        while(!atEnd() && (peek() == ' ' || peek() == '\n' || peek() == '\t' || peek() == '\r')) {              // \r = windows newline character
            if(peek() == '\n') Parser.line++;
            advance();
        }
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static int parseInt() {
        skipWhiteSpaces();
        char c;
        StringBuilder builder = new StringBuilder();
        while(!atEnd() && isDigit(peek()) || peek() == '-') {
            c = advance();
            builder.append(c);
        }
        return Integer.parseInt(builder.toString());
    }

    public static double parseDouble() {
        skipWhiteSpaces();
        char c;
        StringBuilder builder = new StringBuilder();
        while(!atEnd() && isDigit(peek()) || peek() == '-' || peek() == '.') {
            c = advance();
            builder.append(c);
        }
        return Double.parseDouble(builder.toString());
    }

    public static float parseFloat() {
        float f = (float)parseDouble();
        consume('f');
        return f;
    }

    public static String parseString() {
        skipWhiteSpaces();
        char c;
        StringBuilder builder = new StringBuilder();
        consume('"');

        while(!atEnd() && peek() != '"') {
            c = advance();
            builder.append(c);
        }
        consume('"');
        return builder.toString();
    }

    public static boolean parseBoolean() {
        skipWhiteSpaces();
        StringBuilder builder = new StringBuilder();
        if(!atEnd() && peek() == 't') {
            builder.append("true");
            consume('t');
            consume('r');
            consume('u');
            consume('e');
        } else if (!atEnd() && peek() == 'f') {
            builder.append("false");
            consume('f');
            consume('a');
            consume('l');
            consume('s');
            consume('e');
        } else {
            System.out.println("Expected true or false, instead got : " + peek() + "at line " + Parser.line);
            System.exit(-1);
        }
        return builder.toString().compareTo("true") == 0;
    }
}
