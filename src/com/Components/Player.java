package com.Components;

import com.DataStructures.AssetPool;
import com.Game.Component;
import com.Game.DataBaseHandler;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import static com.main.Main.conn;

/**
 * Class for the player.
 */
public class Player extends Component {
    Sprite layer1, layer2, layer3, spaceship;          //layer sprites
    public int width, height;               //player width, height

    /**
     * flag whether the player is on ground or not
     */
    public boolean onGround  = true;

    /**
     * current state of the player, since it has 2 states.
     */
    public PlayerState state;

    private int collectedCoins, numberOFJumps = 0, maxXCounter = 0;

    /**
     * Constructor for coloring the player.
     * @param layer1    first layer of the player
     * @param layer2    second layer of the player
     * @param layer3    third layer of the player
     * @param c1    first color
     * @param c2    second color
     */
    public Player(Sprite layer1, Sprite layer2, Sprite layer3, Color c1, Color c2) {
        this.spaceship = AssetPool.getSprite("Assets/PlayerSprites/ufo.png");
        this.width = Constants.PlayerWidth;
        this.height = Constants.PlayerHeight;
        this.layer1 = layer1;
        this.layer2 = layer2;
        this.layer3 = layer3;
        this.state = PlayerState.Normal;

        int threshold = 200;        //the threshold for a pixel value
        //looping through each pixel of the img
        for (int y = 0; y < layer1.img.getWidth(); y++) {       //column wise
            for(int x = 0; x < layer1.img.getHeight(); x++) {   //row wise
                Color color = new Color(layer1.img.getRGB(x, y));       //get the color at this pixel
                if(color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {     //if rgb values are correct
                    layer1.img.setRGB(x, y, c1.getRGB());       //changes the pixel color at that x,y coordinate with the color we passed
                }
            }
        }
        for (int y = 0; y < layer2.img.getWidth(); y++) {       //column wise
            for(int x = 0; x < layer2.img.getHeight(); x++) {   //row wise
                Color color = new Color(layer2.img.getRGB(x, y));       //get the color at this pixel
                if(color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {     //if rgb values are correct
                    layer2.img.setRGB(x, y, c2.getRGB());       //changes the pixel color at that x,y coordinate with the color we passed
                }
            }
        }
    }

    /**
     * Update method.
     * Checking whether the player is jumping or not and adding the jump forces.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        if(onGround && Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE) && !Window.getWindow().isInEditor) {
            if(state == PlayerState.Normal) {
                addJumpForce();
            }
            this.onGround = false;
            numberOFJumps++;
            DataBaseHandler.updateJumps(conn, Window.getScene().name);
        }

        if(PlayerState.Flying == this.state && Window.keyListener().isKeyPressed(KeyEvent.VK_SPACE) && !Window.getWindow().isInEditor) {
            addFlyForce();
            this.onGround = false;
            numberOFJumps++;
            DataBaseHandler.updateJumps(conn, Window.getScene().name);
        }

        //TODO:: check the rotation not snapping correctly and dying bug
        if(this.state != PlayerState.Flying && !onGround) {
            gameObject.transform.rotation += 4.8f * dTime;            //will be smooth if there is a varying frame rate
        } else if(this.state != PlayerState.Flying) {
            gameObject.transform.rotation = (int)gameObject.transform.rotation % 360;       //snap it so the rotation is between 0 360
            if(gameObject.transform.rotation > 180) {
                gameObject.transform.rotation = 0;
            } else if(gameObject.transform.rotation > 0 && gameObject.transform.rotation < 180) {
                gameObject.transform.rotation = 0;
            }
        }
    }

    /**
     * Add the force necessary for the player to jump.
     */
    private void addJumpForce() {
        gameObject.getComp(RigidBody.class).speed.y = Constants.JumpForce;
    }

    /**
     * Add the force necessary for the player to fly.
     */
    private void addFlyForce() {
        gameObject.getComp(RigidBody.class).speed.y = Constants.FlyForce;
    }

    /**
     * Method called whenever the player dies.
     */
    public void die() {
        gameObject.transform.pos.x = 150;
        gameObject.transform.pos.y = 300;
        gameObject.getComp(RigidBody.class).speed.y = 0;        //player won't continue to jump after he dies
        gameObject.transform.rotation = 0;                      //resetting the transform
        Window.getWindow().getCurrentScene().camera.pos.x = 0;
        gameObject.getComp(Player.class).state = PlayerState.Normal;
        Window.getMusic().restartClip();
        DataBaseHandler.updateAttempts(conn, Window.getScene().name);
        DataBaseHandler.updateCoins(conn, Window.getScene().name, collectedCoins);
        numberOFJumps = 0;
    }

    /**
     * Draws the player, depending on its state.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();          //provides the fake coordinate system to modify the object
        transform.setToIdentity();                                  //resetting the transform to be sure it is empty
        transform.translate(gameObject.getPosX(), gameObject.getPosY());        //we move it to the x,y of the game object
        //anchor the object rotation to its width * scaleX /2, height * scaleY /2
        transform.rotate(gameObject.getRotation(), width * gameObject.getScaleX() / 2.0f, height * gameObject.getScaleY() / 2.0f);
        transform.scale(gameObject.getScaleX(), gameObject.getScaleY());

        if(state == PlayerState.Normal) {
            g2.drawImage(layer1.img, transform, null);
            g2.drawImage(layer2.img, transform, null);
            g2.drawImage(layer3.img, transform, null);
        } else {
            //draw the player first
            transform.setToIdentity();
            transform.translate(gameObject.getPosX(), gameObject.getPosY());
            transform.rotate(gameObject.getRotation(), width * gameObject.getScaleX() / 4.0f, height * gameObject.getScaleY() / 4.0f);
            transform.translate(15, 9);
            transform.scale(gameObject.getScaleX() / 2, gameObject.getScaleY() / 2);

            g2.drawImage(layer1.img, transform, null);
            g2.drawImage(layer2.img, transform, null);
            g2.drawImage(layer3.img, transform, null);

            //draw the spaceship
            transform.setToIdentity();
            transform.translate(gameObject.getPosX(), gameObject.getPosY());
            transform.rotate(gameObject.getRotation(), width * gameObject.getScaleX() / 2.0f, height * gameObject.getScaleY() / 2.0f);
            transform.scale(gameObject.getScaleX(), gameObject.getScaleY());
            g2.drawImage(spaceship.img, transform, null);
        }
    }

    /**
     * Utility method to increment the coins collected.
     */
    public void increaseCoinsCollected() {
        collectedCoins++;
    }
    /**
     * Utility method to get the coins collected
     */
    public int getCollectedCoins() {
        return collectedCoins;
    }

    /**
     * Don't need to copy the player, since we have only one instance of it.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Don't need to save, we will be building it anyway.
     * @param tabSize   number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
