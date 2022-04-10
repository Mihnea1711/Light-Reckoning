package com.Buttons;

import com.Components.Button;
import com.Components.Sprite;
import com.Game.GameObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class SaveLevelButton extends Button {
    String filename;
    List<GameObject> gameObjectList;
    private static final byte[] BUFFER = new byte[4096 * 1024];

    public SaveLevelButton(int width, int height, Sprite Image, Sprite SelectedImage, String filename, List<GameObject> gameObjectList) {
        super(width, height, Image, SelectedImage);
        this.filename = filename;
        this.gameObjectList = gameObjectList;
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
            levels = new ZipFile("levels/levels.zip");
            ZipOutputStream append = new ZipOutputStream(new FileOutputStream("levels/append.zip"));

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

            //append the extra content
            ZipEntry zipEntry = new ZipEntry(filename + ".json");
            append.putNextEntry(zipEntry);
            int i = 0;
            for (GameObject obj : gameObjectList) {
                //serialize all the game objects in the level
                String str = obj.serialize(0);      //0 is the tab size
                if (str.compareTo("") != 0) {        //empty string is the flag for a game object we don't want to serialize
                    append.write(str.getBytes());      //writing in zip files
                    if (i != gameObjectList.size() - 1) {
                        append.write(",\n".getBytes());    //write a comma to separate all the game objects
                    }
                }
                i++;
            }

            append.closeEntry();
            levels.close();
            append.close();

            Files.delete(Paths.get("levels/levels.zip"));
            renameFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void buttonPressed() {
        exportLvl(filename);
    }
}
