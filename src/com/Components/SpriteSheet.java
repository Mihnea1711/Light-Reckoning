package com.Components;

import com.DataStructures.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    public List<Sprite> sprites;
    public int tileWidth, tileHeight, spacing;

    public SpriteSheet(String picFile, int tileWidth, int tileHeight, int spacing, int columns, int size) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;

        Sprite parent = AssetPool.getSprite(picFile);

        sprites = new ArrayList<>();
        int row = 0, count = 0;         //row nr and nr of sprites loaded

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
