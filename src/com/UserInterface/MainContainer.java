package com.UserInterface;

import com.Components.BoxBounds;
import com.Components.Sprite;
import com.Components.SpriteSheet;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.Window;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContainer extends Component {
    public Sprite containerBg;

    List<GameObject> menuItems;

    public List<GameObject> tabs;       //points to a dictionary(Map)
    public Map<GameObject, List<GameObject>> tabMaps;

    private GameObject hotTab = null;       //controls the tab we are on
    private GameObject hotButton = null;

    public MainContainer() {
        this.menuItems = new ArrayList<>();
        this.tabs = new ArrayList<>();
        this.tabMaps = new HashMap<>();
        this.containerBg = AssetPool.getSprite("Assets/UI/menuContainerBackground.png");
        init();
    }

    public void init() {
        SpriteSheet tabSprites = AssetPool.getSpritesheet("Assets/UI/tabs.png");

        for (int i = 0; i < tabSprites.sprites.size(); i++) {
            Sprite currentTab = tabSprites.sprites.get(i);

            int x = Constants.TabOffX + (currentTab.column * Constants.TabWidth) + (currentTab.column * Constants.TabHzPlace);
            int y = Constants.TabOffY;

            GameObject obj = new GameObject("Tab", new Transform(new TwoPair(x, y)), 10);
            obj.setUI(true);
            TabItem item = new TabItem(x, y, Constants.TabWidth, Constants.TabHeight, currentTab, this);
            obj.addComponent(item);

            this.tabs.add(obj);
            this.tabMaps.put(obj, new ArrayList<>());
            Window.getWindow().getCurrentScene().addGameObject(obj);
        }

        this.hotTab = this.tabs.get(0);
        this.hotTab.getComp(TabItem.class).isSelected = true;

        addTabObjects();
    }

    private void addTabObjects() {
        SpriteSheet groundSprites = AssetPool.getSpritesheet("Assets/Blocks.png");
        SpriteSheet buttonSprites = AssetPool.getSpritesheet("Assets/UI/buttonSprites.png");

        SpriteSheet spikesSprites = AssetPool.getSpritesheet("Assets/spikes.png");
        SpriteSheet bigSprites = AssetPool.getSpritesheet("Assets/bigSprites.png");
        SpriteSheet smallBlocks = AssetPool.getSpritesheet("Assets/smallBlocks.png");
        SpriteSheet portalSprites = AssetPool.getSpritesheet("Assets/portal.png");

        for(int i = 0; i < groundSprites.sprites.size(); i++) {
            Sprite currentSprite = groundSprites.sprites.get(i);
            //position of the tab on the screen
            int x = Constants.ButtonOffsetX + (currentSprite.column * Constants.ButtonWidth) + (currentSprite.column * Constants.ButtonSpacingHz);
            int y = Constants.ButtonOffsetY + (currentSprite.row * Constants.ButtonHeight) + (currentSprite.row * Constants.ButtonSpacingVt);

            //ads first tab container objs
            GameObject obj = new GameObject("Gen", new Transform(new TwoPair(x, y)), 10);
            obj.setUI(true);
            obj.setNonserializable();
            obj.addComponent(currentSprite.copy());
            MenuItem menuItem = new MenuItem(x, y, Constants.ButtonWidth, Constants.ButtonHeight,
                    buttonSprites.sprites.get(0), buttonSprites.sprites.get(1), this);
            obj.addComponent(menuItem);
            obj.addComponent(new BoxBounds(Constants.TileWidth, Constants.TileHeight));
            //obj.addComponent(menuItem);
            this.tabMaps.get(this.tabs.get(0)).add(obj);

            //add second tab container objs
            if(i < smallBlocks.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new TwoPair(x, y)), 10);
                obj.setUI(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(smallBlocks.sprites.get(i));
                obj.addComponent(menuItem);
                if(i == 0) {
                    obj.addComponent(new BoxBounds(Constants.TileWidth, 16));
                }
                this.tabMaps.get(tabs.get(1)).add(obj);
            }

            //add fourth tab container obj
            if(i < spikesSprites.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new TwoPair(x, y)), 10);
                obj.setUI(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(spikesSprites.sprites.get(i));
                obj.addComponent(menuItem);

                //TODO:: add triangleBounds comp here

                this.tabMaps.get(tabs.get(3)).add(obj);
            }

            //add fifth tab container obj
            if(i == 0) {
                obj = new GameObject("Gen", new Transform(new TwoPair(x, y)), 10);
                obj.setUI(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(bigSprites.sprites.get(i));
                obj.addComponent(menuItem);

                obj.addComponent(new BoxBounds(Constants.TileWidth * 2, 56));
                this.tabMaps.get(tabs.get(4)).add(obj);
            }

            //add sixth tab container objs
            if(i < portalSprites.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new TwoPair(x, y)), 10);
                obj.setUI(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(portalSprites.sprites.get(i));
                obj.addComponent(menuItem);

                //TODO:: add function for these ^

                obj.addComponent(new BoxBounds(44, 85));

                //TODO:: Add portal component here
                this.tabMaps.get(tabs.get(5)).add(obj);
            }
        }
    }

    @Override
    public void start() {
        for (GameObject g : tabs) {
            for (GameObject g2 : tabMaps.get(g)) {
                for (Component c : g2.getAllComponents()) {
                    c.start();
                }
            }
        }
    }

    @Override
    public void update(double dTime) {
        for(GameObject g :this.tabMaps.get(hotTab)) {
            g.update(dTime);

            MenuItem menuItem = g.getComp(MenuItem.class);
            if(g != hotButton && menuItem.isSelected) {
                menuItem.isSelected = false;
            }
        }
        for(GameObject g :this.tabs) {
            TabItem tabItem = g.getComp(TabItem.class);
            if(g != hotTab && tabItem.isSelected) {
                tabItem.isSelected = false;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.containerBg.img, 0, Constants.ContainerOffY, this.containerBg.width, this.containerBg.height, null);

        for(GameObject g :this.tabMaps.get(hotTab)) {
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

    public void setHotButton(GameObject obj) {
        this.hotButton = obj;
    }

    public void setHotTab(GameObject obj) {
         this.hotTab = obj;
    }
}
