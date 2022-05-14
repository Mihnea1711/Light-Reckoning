package com.Components;

import com.DataStructures.AssetPool;
import com.File.Parser;
import com.Game.Component;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Component to add to any game object.
 * Contains a picture.
 */
public class Sprite extends Component {
    /**
     * the path to the picture file
     */
    String picFile;

    /**
     * the actual image
     */
    public BufferedImage img;   //actual img contained at this picture
    public int width, height;

    /**
     * flag whether the sprite is its own image or is part of a larger sprite
     */
    public boolean isSubSprite = false;
    public int row, column, index;  //sprite values if it is a subsprite

    /**
     * Constructor to do the whole process of loading the picture, if it comes directly from the picture file.
     * @param picFile the picture file where it came from
     */
    public Sprite(String picFile) {
        this.picFile = picFile;
        try {
            File file = new File(picFile);
            if(AssetPool.hasSprite(picFile)) {
                throw new Exception("Asset already exists " + picFile);
            }
            this.img = ImageIO.read(file);              //open & read a picture & stores the data in img
            this.width = img.getWidth();
            this.height = img.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Constructor for sprites that come directly from the buffered img.
     * We are passing an image, so no need to check for sub-sprite
     * @param img the image from the buffered image
     * @param picFile the picture file where it came from
     */
    public Sprite(BufferedImage img, String picFile) {      //pass the picture file to never lose where we got the data from
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.picFile = picFile;
    }

    /**
     * Constructor for a sub sprite.
     * @param img   the image of the sprite
     * @param row   row of the sub-sprite
     * @param column    column of the sub-sprite
     * @param index     index of the sub-sprite
     * @param picFile   the picture file where it came from
     */
    public Sprite(BufferedImage img, int row, int column, int index, String picFile) {
        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.row = row;
        this.column = column;
        this.index = index;
        this.isSubSprite = true;
        this.picFile = picFile;
    }

    /**
     * Draws the sprite.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();          //provides the fake coordinate system to modify the object
        transform.setToIdentity();                                  //resetting the transform to be sure it is empty
        transform.translate(gameObject.getPosX(), gameObject.getPosY());

        //anchor the object rotation to its width * scaleX, height * scaleY
        transform.rotate(Math.toRadians(gameObject.getRotation()), width * gameObject.getScaleX() / 2.0f, height * gameObject.getScaleY() / 2.0f);
        transform.scale(gameObject.getScaleX(), gameObject.getScaleY());
        g2.drawImage(img, transform, null);
    }

    /**
     * Creates a new object sprite instead of passing a reference around.
     * @return new object = copy of a sprite/ sub-sprite
     */
    @Override
    public Component copy() {
        if(!isSubSprite) {
            return new Sprite(this.img, picFile);
        }
        else return new Sprite(this.img, this.row, this.column, this.index, picFile);
    }

    /**
     * Serializes the sprite.
     * @param tabSize number of tabs to be indented correctly
     * @return the sprite serialized
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Sprite", tabSize));
        builder.append(addBooleanProperty("isSubSprite", isSubSprite, tabSize + 1, true, true));

        if(isSubSprite) {
            builder.append(addStringProperty("FilePath", picFile, tabSize + 1, true, true));
            builder.append(addIntProperty("Row", row, tabSize + 1, true, true));
            builder.append(addIntProperty("Column", column, tabSize + 1, true, true));
            builder.append(addIntProperty("Index", index, tabSize + 1, true, false));
            builder.append(closeObjectProperty(tabSize));
        } else {
            builder.append(addStringProperty("FilePath", picFile, tabSize + 1, true, false));
            builder.append(closeObjectProperty(tabSize));
        }

        return builder.toString();
    }

    /**
     * Deserializes the sprite.
     * @return a new sprite object with all its deserialized properties
     */
    public static Sprite deserialize() {
        boolean isSubSprite = Parser.consumeBooleanProperty("isSubSprite");
        Parser.consume(',');
        String filepath = Parser.consumeStringProperty("FilePath");

        if(isSubSprite) {
            Parser.consume(',');
            Parser.consumeIntProperty("Row");
            Parser.consume(',');
            Parser.consumeIntProperty("Column");
            Parser.consume(',');
            int index = Parser.consumeIntProperty("Index");

            if(!AssetPool.hasSpriteSheet(filepath)) {
                System.out.println("Spritesheet " + filepath + " has not been loaded yet!");
                System.exit(-1);
            }

            Parser.consumeEndObjectProperty();
            return (Sprite)AssetPool.getSpritesheet(filepath).sprites.get(index).copy();
        }
        if(!AssetPool.hasSprite(filepath)) {
            System.out.println("Sprite " + filepath + " has not been loaded yet!");
            System.exit(-1);
        }
        Parser.consumeEndObjectProperty();
        return (Sprite)AssetPool.getSprite(filepath).copy();
    }
}
