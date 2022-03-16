package com.Components;

import com.DataStructures.AssetPool;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.Window;
import com.Utilities.Constants;

import java.awt.Graphics2D;

//want to override the methods to add to the editor to pick which backgrounds we choose for the levels => need to serialize the data
public class ParallaxBG extends Component {
    public int width, height;
    public Sprite sprite;
    public GameObject[] backgrounds;
    public int timeStep = 0;

    private float speed = 80.0f;
    private Ground ground;
    private boolean followGround;

    public ParallaxBG(String file, GameObject[] backgrounds, Ground ground, boolean followGround) {
        this.sprite = AssetPool.getSprite(file);
        this.width = this.sprite.width;
        this.height = this.sprite.height;
        this.backgrounds = backgrounds;
        this.ground = ground;

        if(followGround) this.speed = Constants.PlayerSpeed - 25;           //tweak here !!
        this.followGround = followGround;
    }

    @Override
    public void update(double dTime) {
        if(backgrounds == null) {
            return;
        }

        this.timeStep++;

        this.gameObject.transform.pos.x -= dTime * speed;
        this.gameObject.transform.pos.x = (float)Math.floor(this.gameObject.getPosX());

        if(this.gameObject.getPosX() < -width) {
            float maxX = 0;
            int otherTimeStep = 0;
            for(GameObject obj : backgrounds) {
                if(obj.getPosX() > maxX) {
                    maxX = obj.getPosX();
                    otherTimeStep = obj.getComp(ParallaxBG.class).timeStep;
                }
            }
            if(otherTimeStep == this.timeStep) {
                this.gameObject.transform.pos.x = maxX + width;
            } else {
                this.gameObject.transform.pos.x = (float)Math.floor((maxX + width) - (dTime * speed));
            }
        }
        if(this.followGround) {
            this.gameObject.transform.pos.y = ground.gameObject.getPosY();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if(followGround) {
            g2.drawImage(this.sprite.img, (int)this.gameObject.getPosX(),
                    (int)(this.gameObject.getPosY() - Window.getWindowCamY()), width, height, null);
        } else {
            int height = Math.min((int)(ground.gameObject.getPosY() - Window.getWindowCamY()), Constants.ScreenHeight);
            g2.drawImage(this.sprite.img, (int)this.gameObject.getPosX(), (int)(this.gameObject.getPosY()), width, Constants.ScreenHeight, null);
            g2.setColor(Constants.GroundColor);
            g2.fillRect((int)this.gameObject.getPosX(), height, width, Constants.ScreenHeight);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return null;
    }

    @Override
    public Component copy() {
        return null;
    }
}
