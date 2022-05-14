package com.DataStructures;

import com.Components.Sprite;
import com.Components.SpriteSheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Completely static class to get the assets.
 * Handles the creation and destruction of the sprites and sprite sheets, so we won't duplicate anything.
 * Can call this from anywhere in the program, we should be fine.
 */
public class AssetPool {
    static Map<String, Sprite> sprites = new HashMap<>();       //container for all the sprites added
    static Map<String, SpriteSheet> spritesheets = new HashMap<>();

    /**
     * True or false whether this asset pool has a sprite or not.
     * @param pictureFile the picture file
     * @return Whether the asset pool has the sprite or not
     */
    public static boolean hasSprite(String pictureFile) {
        File tmp = new File(pictureFile);
        return AssetPool.sprites.containsKey(tmp.getAbsolutePath());
    }

    /**
     * Returns the sprite inside the picture file.
     * @param picFile the picture file
     * @return the sprite we want
     */
    public static Sprite getSprite(String picFile) {
        File file = new File(picFile);
        if(!AssetPool.hasSprite(file.getAbsolutePath())) {
            Sprite sprite = new Sprite(picFile);
            AssetPool.addSprite(picFile, sprite);
        }
        return AssetPool.sprites.get(file.getAbsolutePath());
    }

    /**
     * Adds the sprite if it doesn't exist.
     * @param picFile the picture
     * @param sprite the sprite
     */
    public static void addSprite(String picFile, Sprite sprite) {
        File file = new File(picFile);
        if(!AssetPool.hasSprite(file.getAbsolutePath())) {
            AssetPool.sprites.put(file.getAbsolutePath(), sprite);
        } else {
            System.out.println("AssetPool already has asset " + file.getAbsolutePath());
            System.exit(-1);
        }
    }

    /**
     * True or false whether this asset pool has a sprite sheet or not.
     * @param picFile the picture file
     * @return Whether the asset pool has the sprite or not
     */
    public static boolean hasSpriteSheet(String picFile) {
        File tmp = new File(picFile);
        return AssetPool.spritesheets.containsKey(tmp.getAbsolutePath());
    }

    /**
     * Returns the sprite sheet inside the picture file.
     * @param picFile the picture file
     * @return the sprite sheet at the file
     */
    public static SpriteSheet getSpritesheet(String picFile) {
        File file = new File(picFile);
        if(AssetPool.hasSpriteSheet((file.getAbsolutePath()))) {
            return AssetPool.spritesheets.get(file.getAbsolutePath());
        } else {
            System.out.println("Spritesheet " + picFile + " doesn't exist.");
            System.exit(-1);
        }
        return null;
    }

    /**
     * Adds the sprite sheet if it doesn't exist.
     * @param picFile   the picture file
     * @param tileWidth sub sprite width
     * @param tileHeight sub sprite height
     * @param spacing spacing between sub sprites
     * @param columns number of columns in the sprite sheet
     * @param size size of the sprite sheet (number of sub sprites)
     */
    public static void addSpritesheet(String picFile, int tileWidth, int tileHeight, int spacing, int columns, int size) {
        File file = new File(picFile);
        if(!AssetPool.hasSpriteSheet(file.getAbsolutePath())) {         //we try not to duplicate assets in any way and keep the memory to a minimum
            SpriteSheet spriteSheet = new SpriteSheet(picFile, tileWidth, tileHeight, spacing, columns, size);
            AssetPool.spritesheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }
}
