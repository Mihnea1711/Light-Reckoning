package com.UserInterface;

import com.Components.BoxBounds;
import com.Components.Sprite;
import com.Components.SpriteSheet;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Game.Component;
import com.Game.GameObject;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class MainContainer extends Component {
    List<GameObject> menuItems;

    public MainContainer() {
        this.menuItems = new ArrayList<>();
        init();
    }

    public void init() {
        SpriteSheet groundSprites = AssetPool.getSpritesheet("Assets/Blocks.png");
        SpriteSheet buttonSprites = AssetPool.getSpritesheet("Assets/buttonSprites.png");

        for(int i = 0; i < groundSprites.sprites.size(); i++) {
            Sprite currentSprite = groundSprites.sprites.get(i);
            //position of the tab on the screen
            int x = Constants.ButtonOffsetX + (currentSprite.column * Constants.ButtonWidth) + (currentSprite.column * Constants.ButtonSpacingHz);
            int y = Constants.ButtonOffsetY + (currentSprite.row * Constants.ButtonHeight) + (currentSprite.row * Constants.ButtonSpacingVt);

            GameObject obj = new GameObject("Generated", new Transform(new TwoPair(x, y)));
            obj.addComponent(currentSprite.copy());
            MenuItem menuItem = new MenuItem(x, y, Constants.ButtonWidth, Constants.ButtonHeight, buttonSprites.sprites.get(0), buttonSprites.sprites.get(1));
            obj.addComponent(menuItem);
            obj.addComponent(new BoxBounds(Constants.TileWidth, Constants.TileHeight));
            menuItems.add(obj);
        }
    }

    @Override
    public void start() {
        for(GameObject g : menuItems) {
            for(Component c : g.getAllComponents()) {
                c.start();
            }
        }
    }

    @Override
    public void update(double dTime) {
        for(GameObject g :this.menuItems) {
            g.update(dTime);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        for(GameObject g :this.menuItems) {
            g.draw(g2);
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
