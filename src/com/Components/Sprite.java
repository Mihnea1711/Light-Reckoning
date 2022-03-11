package com.Components;

import com.DataStructures.AssetPool;
import com.Game.Component;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sprite extends Component {
    String picFile;
    public BufferedImage img; //actual img contained at the picture
    public int width, height;

    public boolean isSubSprite = false;
    public int row, column, index;

    //sprites that come directly from the picFile
    public Sprite(String picFile) {
        this.picFile = picFile;
        try {
            File file = new File(picFile);
            if(AssetPool.hasSprite(picFile)) {
                throw new Exception("Asset already exists");
            }
            this.img = ImageIO.read(file);              //open & read a picture & stores the data in img
            this.width = img.getWidth();
            this.height = img.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //new constructor
    //sprites that come directly from the buffered img
    public Sprite(BufferedImage img) {
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    public Sprite(BufferedImage img, int row, int column, int index) {
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.row = row;
        this.column = column;
        this.index = index;
        this.isSubSprite = true;
    }

    //draw sprite
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, (int)gameObject.getPosX(), (int)gameObject.getPosY(), width, height, null);
    }

    @Override
    public Component copy() {
        if(!isSubSprite) {
            return new Sprite(this.img);
        }
        else return new Sprite(this.img, this.row, this.column, this.index);
    }
}
