package com.DataStructures;

import com.Components.Sprite;
import com.Components.SpriteSheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//completely static class to get the assets
//handles the creation & destruction of objects so we won't duplicate anything
//call this from anywhere in the prog
public class AssetPool {
    static Map<String, Sprite> sprites = new HashMap<>();       //container for all the sprites added
    static Map<String, SpriteSheet> spritesheets = new HashMap<>();

    //true/false whether or not we have a sprite
    public static boolean hasSprite(String pictureFile) {
        File tmp = new File(pictureFile);
        return AssetPool.sprites.containsKey(tmp.getAbsolutePath());
    }

    public static boolean hasSpriteSheet(String picFile) {
        File tmp = new File(picFile);
        return AssetPool.spritesheets.containsKey(tmp.getAbsolutePath());
    }

    //returns the sprite
    public static Sprite getSprite(String picFile) {
        File file = new File(picFile);
        if(AssetPool.hasSprite(file.getAbsolutePath())) {
            //make sure it always returns the same pic no matter the path sent
            return AssetPool.sprites.get(file.getAbsolutePath());
        } else {
            Sprite sprite = new Sprite(picFile);
            AssetPool.addSprite(picFile, sprite);
            return AssetPool.sprites.get(file.getAbsolutePath());
        }
    }

    public static SpriteSheet getSpritesheet(String picFile) {
        File file = new File(picFile);
        if(AssetPool.hasSpriteSheet((file.getAbsolutePath()))) {
            return AssetPool.spritesheets.get(file.getAbsolutePath());
        } else {
            System.out.println("Spritesheet " + picFile + "Doesn't exist.");
            System.exit(-1);
        }
        return null;
    }

    //add sprite if it doesn't exist
    public static void addSprite(String picFile, Sprite sprite) {
        File file = new File(picFile);
        if(!AssetPool.hasSprite(file.getAbsolutePath())) {
            AssetPool.sprites.put(file.getAbsolutePath(), sprite);
        } else {
            System.out.println("AssetPool already has asset");
            System.exit(-1);
        }
    }

    public static void addSpritesheet(String picFile, int tileWidth, int tileHeight, int spacing, int columns, int size) {
        File file = new File(picFile);
        if(!AssetPool.hasSpriteSheet(file.getAbsolutePath())) {         //we try not to duplicate assets in any way and keep the memory to a minimum
            SpriteSheet spriteSheet = new SpriteSheet(picFile, tileWidth, tileHeight, spacing, columns, size);
            AssetPool.spritesheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }
}
