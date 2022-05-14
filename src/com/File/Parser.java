package com.File;

import com.Components.*;
import com.Game.Component;
import com.Game.GameObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Parses the JSON file.
 * Completely static class.
 */
public class Parser {
    private static int offset = 0;      //current char
    private static int line = 1;        //line nr
    private static byte[] bytes;        //letters on the screen   array full of chars

    /**
     * Function to open the file we are about to parse.
     * @param filename name of the file we want to parse
     */
    public static void openFile(String filename, String zipFilePath) {
        File tmp = new File(zipFilePath);
        if(!tmp.exists()) { return; }
        offset = 0;
        line = 1;

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);       //the zip file
            ZipEntry jsonFile = zipFile.getEntry(filename + ".json");       //the json file inside the zip
            InputStream stream = zipFile.getInputStream(jsonFile);          //read from a file

            Parser.bytes = stream.readAllBytes();       //not the most efficient (reading through the file twice)
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Tells us whether we are at the end of the file or not.
     * @return true/false if we are at the end of the file
     */
    public static boolean atEnd() {
        return offset == bytes.length;  //if offset is at the end of the bytes
    }

    /**
     * Tells us the character we are on.
     * @return whatever the offset is pointing to
     */
    public static char peek() {
        return (char)bytes[offset];
    }

    /**
     * Returns the current character and increments the offset.
     * @return the character we are on.
     */
    public static char advance() {
        char c = (char)bytes[offset++];
        return c;
    }

    /**
     * If we actually have the character we were expecting, increments the offset.
     * @param c thr character
     */
    public static void consume(char c) {
        char actual = peek();
        if(actual != c) {
            System.out.println("Error: Expected " + c + " but instead got " + actual + " at line " + Parser.line);
            System.exit(-1);
        }
        offset++;
    }

    /**
     * Skips the whitespaces inside the file.
     */
    public static void skipWhiteSpaces() {
        while(!atEnd() && (peek() == ' ' || peek() == '\n' || peek() == '\t' || peek() == '\r')) {              // \r = windows newline character
            if(peek() == '\n') Parser.line++;
            advance();
        }
    }

    /**
     * Verifies if the character is digit or not.
     * @param c the character
     * @return true/false if the character is digit or not.
     */
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Parses the int value.
     * @return the int value
     */
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

    /**
     * Parses the double value.
     * @return the double value
     */
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

    /**
     * Parses the float value.
     * @return the float value
     */
    public static float parseFloat() {
        float f = (float)parseDouble();
        consume('f');
        return f;
    }

    /**
     * Parses the string.
     * @return the string
     */
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

    /**
     * Parses the boolean value.
     * @return the boolean value
     */
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

    /**
     * Parses the beginning of the object.
     * @param name name of the object
     */
    public static void consumeBeginObjectProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        skipWhiteSpaces();
        consume(':');
        skipWhiteSpaces();
        consume('{');
    }

    /**
     * Parses the ending of the object.
     */
    public static void consumeEndObjectProperty() {
        skipWhiteSpaces();
        consume('}');
    }

    /**
     * Parses a string property line.
     * @param name name of the property
     * @return
     */
    public static String consumeStringProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseString();
    }

    /**
     * Parses an int property line.
     * @param name name of the property
     * @return
     */
    public static int consumeIntProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseInt();
    }

    /**
     * Parses a float property line.
     * @param name name of the property
     * @return
     */
    public static float consumeFloatProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseFloat();
    }

    /**
     * Parses a double property line.
     * @param name name of the property
     * @return
     */
    public static double consumeDoubleProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseDouble();
    }

    /**
     * Parses a boolean property line.
     * @param name name of the property
     * @return
     */
    public static boolean consumeBooleanProperty(String name) {
        skipWhiteSpaces();
        checkString(name);
        consume(':');
        return parseBoolean();
    }

    /**
     * Parses the game object.
     * @return the game object deserialized
     */
    public static GameObject parseGameObject() {
        if(bytes.length == 0 || atEnd()) return null;

        if(peek() == ',') Parser.consume(',');
        skipWhiteSpaces();
        if(atEnd()) return null;

        return GameObject.deserialize();
    }

    /**
     * Parses the component of an object.
     * @return the component deserialized
     */
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
            case "CircleBounds":
                return CircleBounds.deserialize();
            case "Coin":
                return Coin.deserialize();
            case "MarioPipe":
                return PipePart.deserialize();
            default:
                System.out.println("Could not find component " + compTitle + " at line " + Parser.line);
                System.exit(-1);
        }
        return null;
    }

    /**
     * Checks if we got the string we want inside the file.
     * @param str the string we want
     */
    private static void checkString(String str) {
        String title = Parser.parseString();
        if(title.compareTo(str) != 0) {
            System.out.println("Expected " + str + "but instead got " + title + " at line " + Parser.line);
            System.exit(-1);
        }
    }
}
