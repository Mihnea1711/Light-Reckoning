package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.Window;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Class for the submit button.
 */
public class SubmitButton extends Button {
    /**
     * The text attached to the button.
     */
    private String textAttached;

    /**
     * Utility buffer for reading the file.
     */
    private static final byte[] BUFFER = new byte[4096 * 1024];

    /**
     * Flag whether we want to create a new level or not.
     */
    private boolean create;

    /**
     * Constructor for the button.
     * @param width button width
     * @param height button height
     * @param Image button image (non-pressed)
     * @param SelectedImage button image (pressed)
     * @param textAttached button text
     */
    public SubmitButton(int width, int height, Sprite Image, Sprite SelectedImage, String textAttached) {
        super(width, height, Image, SelectedImage);
        this.textAttached = textAttached;
    }

    /**
     * Utility function.
     * @param text button text
     */
    public void setTextAttached(String text) {
        this.textAttached = text;
    }

    /**
     * Utility function.
     */
    public void setCreateFalse() {
        create = false;
    }

    /**
     * Utility function.
     */
    public void setCreateTrue() {
        create = true;
    }

    /**
     * Utility function for exporting the level.
     * @param inputStream stream to be read
     * @param outputStream stream to be written
     * @throws IOException exception
     */
    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesRead;
        while((bytesRead = inputStream.read(BUFFER)) != -1) {           //-1 EOF
            outputStream.write(BUFFER, 0, bytesRead);
        }
    }

    /**
     * Utility function for renaming the zip file.
     */
    private void renameFile() {
        Path sourceFile = Paths.get("levels/append.zip");
        try {
            Files.move(sourceFile, sourceFile.resolveSibling("CreatedLevels.zip"));
        } catch (IOException e) {
            System.out.println("Rename error");
            e.printStackTrace();
        }
    }

    /**
     * Export function.
     * @param filename the file that has been modified
     */
    private void exportLvl(String filename) {
        ZipFile levels;
        try {
            ZipOutputStream append = new ZipOutputStream(new FileOutputStream("levels/append.zip"));
            File check = new File("levels/CreatedLevels.zip");
            if(check.exists()) {
                levels = new ZipFile("levels/CreatedLevels.zip");
                //copy contents from existing zip
                Enumeration<? extends ZipEntry> entries = levels.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    if (!zipEntry.getName().equals(filename + ".json")) {
                        append.putNextEntry(zipEntry);
                        if (!zipEntry.isDirectory()) {
                            copy(levels.getInputStream(zipEntry), append);
                        }
                        append.closeEntry();
                    }
                }
                levels.close();
                //delete old file
                Files.delete(Paths.get("levels/CreatedLevels.zip"));
            }

            //append the extra content
            ZipEntry zipEntry = new ZipEntry(filename + ".json");
            append.putNextEntry(zipEntry);

            append.closeEntry();
            append.close();

            //rename the new file
            renameFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Main function of the button.
     */
    @Override
    public void buttonPressed() {
        if (create) {
            exportLvl(textAttached);
            Window.getWindow().changeScene(0, 8, textAttached,"", "Assets/LevelSoundTracks/stereoMadness.wav",
                    "Assets/Background/bg01.png", "Assets/Ground/ground01.png", false);
        } else {
            Window.getWindow().changeScene(0, 8, textAttached, "", "",
                    "", "", true);
        }
    }
}
