package com.DataStructures;

import com.Components.Sprite;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//completely static class to get the assets
//handles the creation & destruction of objects so we won't duplicate anything
//call this from anywhere in the prog
public class AssetPool {
    static Map<String, Sprite> sprites = new HashMap<>();       //container for all the sprites added

    //true/false whether or not we have a sprite
    public static boolean hasSprite(String pictureFile) {
        return AssetPool.sprites.containsKey(pictureFile);
    }

    //returns the sprite
    public static Sprite getSprite(String picFile) {
        File file = new File(picFile);
        if(AssetPool.hasSprite(picFile)) {
            //make sure it always returns the same pic no matter the path sent
            return AssetPool.sprites.get(file.getAbsolutePath().toString());
        } else {
            Sprite sprite = new Sprite(picFile);
            AssetPool.addSprite(picFile, sprite);
            return AssetPool.sprites.get(file.getAbsolutePath());
        }
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
}
