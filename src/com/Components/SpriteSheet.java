package com.Components;

import com.DataStructures.AssetPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the sprite sheets.
 */
public class SpriteSheet {
    /**
     * List of the sprites inside a sprite sheet
     */
    public List<Sprite> sprites;        //sprites attached
    public int tileWidth, tileHeight, spacing;  //properties of the sprites

    /**
     * Constructor for the class.
     * @param picFile the file from where this sprite sheet is loading
     * @param tileWidth how big each individual sprite is
     * @param tileHeight how big each individual sprite is
     * @param spacing spacing between each sprite in the sprite sheet
     * @param columns number of columns in the sprite sheet
     * @param size number of sprites in the sprite sheet
     */
    public SpriteSheet(String picFile, int tileWidth, int tileHeight, int spacing, int columns, int size) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;

        Sprite parent = AssetPool.getSprite(picFile);   //should return us the sprite that contains all the sub-sprites

        sprites = new ArrayList<>();
        int row = 0, count = 0;         //row nr we are starting on and nr of sprites loaded

        while(count < size) {
            for(int column = 0; column < columns; column++) {           //column wise
                int imgX = (column * tileWidth) + (column * spacing);
                int imgY = (row * tileHeight) + (row * spacing);

                sprites.add(new Sprite(parent.img.getSubimage(imgX, imgY, tileWidth, tileHeight), row, column, count, picFile));
                count++;
                if(count > size -1) {
                    break;
                }
            }
            row++;
        }
    }
}
