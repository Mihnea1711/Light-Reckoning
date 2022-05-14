package com.Components;

import com.DataStructures.AssetPool;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Class for the Parallax-type Background.
 */
public class ParallaxBG extends Component {
    public int width;               //width of the background img
    public int height;               //height of the background img
    public Sprite sprite;                   //sprite for the background img
    public String pictureFile;
    public GameObject[] backgrounds;        //collection of backgrounds so we can loop throw them

    /**
     * Variable that keeps track of the time step of the current background
     */
    public int timeStep = 0;                //keep track on which time step we are on

    private float speed = Constants.BgSpeed;

    /**
     * reference to the ground object
     */
    private Ground ground;      //reference to the ground object

    /**
     * flag to differentiate whether we are dealing with a background or a ground background
     */
    private boolean followGround;

    private Color color;

    /**
     * Constructor. Sets the background's speed less than the player's speed, so it has a parallax effect.
     * @param file  the file that contains the image
     * @param backgrounds   the backgrounds
     * @param ground    reference to the ground
     * @param followGround boolean value to see if we are doing the ground pieces or the background ones.
     */
    public ParallaxBG(String file, GameObject[] backgrounds, Ground ground, boolean followGround) {
        this.pictureFile = file;
        this.sprite = AssetPool.getSprite(file);
        this.width = this.sprite.width;
        this.height = this.sprite.height;
        this.backgrounds = backgrounds;
        this.ground = ground;

        //if the player is on the ground, it will move the same speed with the player, but the background will be slower
        if(followGround) this.speed = Constants.PlayerSpeed - 35;
        this.followGround = followGround;
    }

    /**
     * Updates the background.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(backgrounds == null) {       //we won t update the backgrounds if they are null (static)
            return;
        }

        //every tick, we add another time step, so we know which time step we are on
        this.timeStep++;

        this.gameObject.transform.pos.x -= dTime * speed;       //move it to the left
        this.gameObject.transform.pos.x = (float)Math.floor(this.gameObject.getPosX()); //locking it to an integer, so it gets rid of the pixel gaps/boundaries

        if(this.gameObject.getPosX() < -width) {        //if we can no longer see it, move it to the right
            float maxX = 0;     //look for the maximum x between the other backgrounds
            int otherTimeStep = 0;      //the time step that the other background is on
            for(GameObject obj : backgrounds) {
                if(obj.getPosX() > maxX) {
                    maxX = obj.getPosX();
                    otherTimeStep = obj.getComp(ParallaxBG.class).timeStep;
                }
            }
            if(otherTimeStep == this.timeStep) {    //if they have the same timeStep, we don't have to decrease by dt
                this.gameObject.transform.pos.x = maxX + width;
            } else {
                this.gameObject.transform.pos.x = (float)Math.floor((maxX + width) - (dTime * speed));  //so we compensate for the little pixel gap that might be appearing
            }
        }
        //if we are following the ground, we have the same y position as it.
        if(this.followGround) {
            this.gameObject.transform.pos.y = ground.gameObject.getPosY();
        }
    }

    /**
     * Draws the backgrounds
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        if(followGround) {  //background img
            g2.drawImage(this.sprite.img, (int)this.gameObject.getPosX(),
                    (int)(this.gameObject.getPosY() - Window.getWindowCamY()), width, height, null);    //subtract camY to transform it from world coordinate to screen coordinate
        } else {    //ground img
            int height = Math.min((int)(ground.gameObject.getPosY() - Window.getWindowCamY()), Constants.ScreenHeight);
            g2.drawImage(this.sprite.img, (int)this.gameObject.getPosX(), (int)(this.gameObject.getPosY()), width, Constants.ScreenHeight, null);
            g2.setColor(Constants.GroundColor);
            g2.fillRect((int)this.gameObject.getPosX(), height, width, Constants.ScreenHeight);
        }
    }

    /**
     * Serializes the background of the object in case we will ever need to make the user choose it in the level creator.
     * @param tabSize   number of tabs to be indented correctly
     * @return backgrounds serialized
     */
    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("Background", tabSize));
        //color
        builder.append(beginObjectProperty("Color", tabSize + 1));
        builder.append(addFloatProperty("R", color.getRed(), tabSize + 2, true, true));
        builder.append(addFloatProperty("G", color.getGreen(), tabSize + 2, true, true));
        builder.append(addFloatProperty("B", color.getBlue(), tabSize + 2, true, true));
        builder.append(addFloatProperty("A", color.getAlpha(), tabSize + 2, true, false));
        builder.append(closeObjectProperty(tabSize + 1));
        builder.append(addEnding(true, true));

        //path
        builder.append(addStringProperty("FilePath", pictureFile, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    /**
     * Deserializes the background
     * @return a new background object with the deserialized properties
     */
    public static ParallaxBG deserialize() {
        //deserializes the background
        //in case we will ever need it
        return null;
    }

    /**
     * Don't need to send a copy, since we will be building it every time.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Utility function to set the ground color
     * @param color new color
     */
    public void setGroundColor(Color color) {
        this.color = color;
    }
}
