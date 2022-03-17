package com.File;

import com.Components.BoxBounds;
import com.Components.Portal;
import com.Components.Sprite;
import com.Components.TriangleBounds;
import com.Game.Component;
import com.Game.GameObject;

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
        if(!tmp.exists()) { return; }
        offset = 0;
        line = 1;

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

    public static void consumeBeginObjectProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        skipWhiteSpaces();
        consume(':');
        skipWhiteSpaces();
        consume('{');
    }

    public static void consumeEndObjectProperty() {
        skipWhiteSpaces();
        consume('}');
    }

    public static String consumeStringProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseString();
    }

    public static int consumeIntProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseInt();
    }

    public static float consumeFloatProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseFloat();
    }

    public static double consumeDoubleProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseDouble();
    }

    public static boolean consumeBooleanProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseBoolean();
    }

    public static GameObject parseGameObject() {
        if(bytes.length == 0 || atEnd()) return null;

        if(peek() == ',') Parser.consume(',');
        skipWhiteSpaces();
        if(atEnd()) return null;

        return GameObject.deserialize();
    }

    public static Component parseComponent() {
        String compTitle = Parser.parseString();
        skipWhiteSpaces();
        Parser.consume(':');
        skipWhiteSpaces();
        Parser.consume('{');
        switch (compTitle) {
            case "Sprite":
                return Sprite.deserialize();
            case "BoxBounds":
                return BoxBounds.deserialize();
            case "TriangleBounds":
                return TriangleBounds.deserialize();
            case "Portal":
                return Portal.deserialize();
            default:
                System.out.println("Could not find component " + compTitle + "at line " + Parser.line);
                System.exit(-1);
        }
        return null;
    }

    private static void checkString(String str) {
        String title = Parser.parseString();
        if(title.compareTo(str) != 0) {
            System.out.println("Expected " + str + "but instead got " + title + " at line " + Parser.line);
            System.exit(-1);
        }
    }
}
