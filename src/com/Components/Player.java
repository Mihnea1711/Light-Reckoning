package com.Components;

import com.Game.Component;
import com.Utilities.Constants;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;

public class Player extends Component {
    Sprite layer1, layer2, layer3;          //layer sprites
    public int width, height;               //player width, height

    public Player(Sprite layer1, Sprite layer2, Sprite layer3, Color c1, Color c2) {
        this.width = Constants.PlayerWidth;
        this.height = Constants.PlayerHeight;
        this.layer1 = layer1;
        this.layer2 = layer2;
        this.layer3 = layer3;

        int threshold = 200;        //the threshold for a pixel value
        //looping through each pixel of the img
        for (int y = 0; y < layer1.img.getWidth(); y++) {       //column wise
            for(int x = 0; x < layer1.img.getHeight(); x++) {   //row wise
                Color color = new Color(layer1.img.getRGB(x, y));       //get the color at this pixel
                if(color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {     //if rgb values are correct
                    layer1.img.setRGB(x, y, c1.getRGB());       //changes the pixel color at that x,y coord with the color we got
                }
            }
        }
        for (int y = 0; y < layer2.img.getWidth(); y++) {       //column wise
            for(int x = 0; x < layer2.img.getHeight(); x++) {   //row wise
                Color color = new Color(layer2.img.getRGB(x, y));       //get the color at this pixel
                if(color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {     //if rgb values are correct
                    layer2.img.setRGB(x, y, c2.getRGB());       //changes the pixel color at that x,y coord with the color we got
                }
            }
        }
    }

    //draw the picture
    @Override
    public void draw(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();          //provides the fake coord system to modify the object
        transform.setToIdentity();                                  //resetting the transform to be sure it is empty
        transform.translate(gameObject.getPosX(), gameObject.getPosY());
        //anchor the object rotation to its width * scaleX, height * scaleY
        transform.rotate(gameObject.getRotation(), width * gameObject.getScaleX() / 2, height * gameObject.getScaleY() / 2);
        transform.scale(gameObject.getScaleX(), gameObject.getScaleY());

        g2.drawImage(layer1.img, transform, null);
        g2.drawImage(layer2.img, transform, null);
        g2.drawImage(layer3.img, transform, null);
    }

    @Override
    public Component copy() {
        return null;
    }
}
