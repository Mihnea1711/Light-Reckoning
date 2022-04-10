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

public class SubmitButton extends Button {
    private String textAttached;
    private static final byte[] BUFFER = new byte[4096 * 1024];
    private boolean create;

    public SubmitButton(int width, int height, Sprite Image, Sprite SelectedImage, String textAttached) {
        super(width, height, Image, SelectedImage);
        this.textAttached = textAttached;
    }

    public void setTextAttached(String text) {
        this.textAttached = text;
    }

    public void setCreateFalse() {
        create = false;
    }

    public void setCreateTrue() {
        create = true;
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int bytesRead;
        while((bytesRead = inputStream.read(BUFFER)) != -1) {           //-1 EOF
            outputStream.write(BUFFER, 0, bytesRead);
        }
    }

    private void renameFile() {
        Path sourceFile = Paths.get("levels/append.zip");
        try {
            Files.move(sourceFile, sourceFile.resolveSibling("levels.zip"));
        } catch (IOException e) {
            System.out.println("Rename error");
            e.printStackTrace();
        }
    }

    private void exportLvl(String filename) {
        ZipFile levels;
        try {
            ZipOutputStream append = new ZipOutputStream(new FileOutputStream("levels/append.zip"));
            File check = new File("levels/levels.zip");
            if(check.exists()) {
                levels = new ZipFile("levels/levels.zip");
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
                Files.delete(Paths.get("levels/levels.zip"));
            }

            //append the extra content
            ZipEntry zipEntry = new ZipEntry(filename + ".json");
            append.putNextEntry(zipEntry);

            append.closeEntry();
            append.close();

            renameFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void buttonPressed() {
        if (create) {
            exportLvl(textAttached);
            Window.getWindow().changeScene(0, textAttached, "Assets/LevelSoundTracks/stereoMadness.wav",
                    "Assets/Background/bg01.png", "Assets/Ground/ground01.png", false);
        } else {
            Window.getWindow().changeScene(0, textAttached, "",
                    "", "", true);
        }
    }
}
