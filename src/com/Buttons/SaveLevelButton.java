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

/**
 * Class for the button that saves the edits made inside editor.
 */
public class SaveLevelButton extends Button {

    /**
     * Name of the file to be saved.
     */
    String filename;

    /**
     * List of objects inside the current file.
     */
    List<GameObject> gameObjectList;

    /**
     * Utility buffer used for reading the file.
     */
    private static final byte[] BUFFER = new byte[4096 * 1024];

    /**
     * Constructor for the save button inside the editor.
     * @param width width of the button
     * @param height height of the button
     * @param Image button image when non-clicked
     * @param SelectedImage button image when clicked
     * @param filename the name of the modified file
     * @param gameObjectList the list of objects inside the level
     */
    public SaveLevelButton(int width, int height, Sprite Image, Sprite SelectedImage, String filename, List<GameObject> gameObjectList) {
        super(width, height, Image, SelectedImage);
        this.filename = filename;
        this.gameObjectList = gameObjectList;
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
            levels = new ZipFile("levels/CreatedLevels.zip");
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

            //delete old file and rename the new one
            Files.delete(Paths.get("levels/CreatedLevels.zip"));
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
        exportLvl(filename);
    }
}
