package com.Components;

import com.DataStructures.AssetPool;
import com.Game.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sprite extends Component {
    String picFile;
    public BufferedImage img; //actual img contained at the picture
    public int width, height;

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

    //draw sprite
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(img, (int)gameObject.getX(), (int)gameObject.getY(), width, height, null);
    }
}
