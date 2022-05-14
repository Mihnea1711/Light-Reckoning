package com.UserInterface;

import com.Components.*;
import com.DataStructures.AssetPool;
import com.DataStructures.Transform;
import com.Game.Component;
import com.Game.GameObject;
import com.Game.Window;
import com.Utilities.Constants;
import com.Utilities.Pair;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores all the blocks and objects (sprites) that can be placed in the game.
 */
public class MainContainer extends Component {
    public Sprite containerBg;

    List<GameObject> menuItems; //every button on the screen

    public List<GameObject> tabs;       //points to a dictionary(Map)
    public Map<GameObject, List<GameObject>> tabMaps;

    private GameObject hotTab = null;       //controls the tab we are on
    private GameObject hotButton = null;

    /**
     * Constructor for the container.
     */
    public MainContainer() {
        this.menuItems = new ArrayList<>();
        this.tabs = new ArrayList<>();
        this.tabMaps = new HashMap<>();
        this.containerBg = AssetPool.getSprite("Assets/UI/menuContainerBackground.png");
        init();
    }

    /**
     * Initializes the main container with all its tabs and items.
     */
    public void init() {
        SpriteSheet tabSprites = AssetPool.getSpritesheet("Assets/UI/tabs.png");

        for (int i = 0; i < tabSprites.sprites.size(); i++) {
            Sprite currentTab = tabSprites.sprites.get(i);

            int x = Constants.TabOffX + (currentTab.column * Constants.TabWidth) + (currentTab.column * Constants.TabHzSpacing);
            int y = Constants.TabOffY;

            GameObject obj = new GameObject("Tab", new Transform(new Pair(x, y)), 10);
            obj.setUI(true);
            obj.setNonserializable();

            //make the tabs half transparent
            TabItem item = new TabItem(x, y, Constants.TabWidth, Constants.TabHeight, currentTab, this);
            obj.addComponent(item);

            this.tabs.add(obj);
            this.tabMaps.put(obj, new ArrayList<>());
            Window.getWindow().getCurrentScene().addGameObject(obj);
        }

        //first tab is selected
        this.hotTab = this.tabs.get(0);
        this.hotTab.getComp(TabItem.class).isSelected = true;

        addTabObjects();
    }

    /**
     * Pretty cluttered function, but its one purpose is to add the tab objects.
     */
    private void addTabObjects() {
        SpriteSheet groundSprites = AssetPool.getSpritesheet("Assets/Blocks/Blocks.png");
        SpriteSheet buttonSprites = AssetPool.getSpritesheet("Assets/UI/buttonSprites.png");

        SpriteSheet spikesSprites = AssetPool.getSpritesheet("Assets/Blocks/spikes.png");
        SpriteSheet bigSprites = AssetPool.getSpritesheet("Assets/Blocks/bigBlocks.png");
        SpriteSheet smallBlocks = AssetPool.getSpritesheet("Assets/Blocks/smallBlocks.png");
        SpriteSheet portalSprites = AssetPool.getSpritesheet("Assets/Portals/portal.png");
        SpriteSheet coinSprites = AssetPool.getSpritesheet("Assets/Collectibles/coin.png");

        for(int i = 0; i < groundSprites.sprites.size(); i++) {
            Sprite currentSprite = groundSprites.sprites.get(i);
            //position of the button on the screen
            int x = Constants.ButtonOffsetX + (currentSprite.column * Constants.ButtonWidth) + (currentSprite.column * Constants.ButtonSpacingHz);
            int y = Constants.ButtonOffsetY + (currentSprite.row * Constants.ButtonHeight) + (currentSprite.row * Constants.ButtonSpacingVt);

            //ads first tab container objects
            GameObject obj = createTabObject(new Pair(x, y), (Sprite) currentSprite.copy(), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                    Constants.ButtonWidth, Constants.ButtonHeight);

            obj.addComponent(new BoxBounds(Constants.TileWidth, Constants.TileHeight));
            this.tabMaps.get(this.tabs.get(0)).add(obj);

            //add second tab container objects
            if(i < smallBlocks.sprites.size()) {
                obj = createTabObject(new Pair(x, y), smallBlocks.sprites.get(i), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                        Constants.ButtonWidth, Constants.ButtonHeight);

                if(i == 0) {
                    BoxBounds boxBounds = new BoxBounds(Constants.TileWidth, 16);       //some obj have different widths or heights
                    boxBounds.yBuffer = 26;
                    obj.addComponent(boxBounds);
                } else {
                    obj.addComponent(new BoxBounds(Constants.TileWidth, Constants.TileHeight));
                }
                this.tabMaps.get(tabs.get(1)).add(obj);
            }

            //add third tab objects
            if(i < coinSprites.sprites.size()) {
                x = Constants.ButtonOffsetX + (currentSprite.column * (Constants.BigButtonWidth)) + (currentSprite.column * Constants.ButtonSpacingHz);
                y = Constants.ButtonOffsetY + (currentSprite.row * (Constants.BigButtonHeight)) + (currentSprite.row * Constants.ButtonSpacingVt);

                obj = createTabObject(new Pair(x, y), coinSprites.sprites.get(i), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                        Constants.BigButtonWidth, Constants.BigButtonHeight);

                if(i == 0) {
                    CircleBounds circleBounds = new CircleBounds(69, true);       //some obj have different widths or heights
                    circleBounds.xBuffer = 3;
                    circleBounds.yBuffer = 3;
                    obj.addComponent(circleBounds);
                    obj.addComponent(new Coin(false));
                }
                this.tabMaps.get(tabs.get(2)).add(obj);
            }

            //add fourth tab container objects
            if(i < spikesSprites.sprites.size()) {
                obj = createTabObject(new Pair(x, y), spikesSprites.sprites.get(i), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                        Constants.ButtonWidth, Constants.ButtonHeight);

                if(i == 1) {
                    TriangleBounds triangleBounds = new TriangleBounds(Constants.TileWidth, 15);       //some obj have different widths or heights
                    triangleBounds.yBuffer = 27;
                    obj.addComponent(triangleBounds);
                }

                if(i == 2) {
                    TriangleBounds triangleBounds = new TriangleBounds(21, 21);       //some obj have different widths or heights
                    triangleBounds.xBuffer = 10.5f;
                    triangleBounds.yBuffer = 21;
                    obj.addComponent(triangleBounds);
                }

                if(i == 3) {
                    BoxBounds boxBounds = new BoxBounds(Constants.TileWidth, 25);       //some obj have different widths or heights
                    boxBounds.yBuffer = 18;
                    obj.addComponent(boxBounds);
                }
                obj.addComponent(new TriangleBounds(Constants.TileWidth, Constants.TileHeight));

                this.tabMaps.get(tabs.get(3)).add(obj);
            }

            //add fifth tab container objects
            if(i < bigSprites.sprites.size()) {
                x = Constants.ButtonOffsetX + (currentSprite.column * (Constants.BigButtonWidth)) + (currentSprite.column * Constants.ButtonSpacingHz);
                y = Constants.ButtonOffsetY + (currentSprite.row * (Constants.BigButtonHeight)) + (currentSprite.row * Constants.ButtonSpacingVt);

                obj = createTabObject(new Pair(x, y), bigSprites.sprites.get(i), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                        Constants.BigButtonWidth, Constants.BigButtonHeight);

                if (i == 0) {
                    obj.addComponent(new BoxBounds(Constants.TileWidth * 2, Constants.TileHeight * 2));
                } else if(i == 1) {
                    BoxBounds boxBounds = new BoxBounds(Constants.TileWidth * 2, 56);       //some obj have different widths or heights
                    boxBounds.yBuffer = 28;
                    obj.addComponent(boxBounds);
                } else if(i == 2) {
                    obj.addComponent(new BoxBounds(Constants.TileWidth * 2, Constants.TileHeight * 2, true));
                    obj.addComponent(new PipePart(true));
                }

                this.tabMaps.get(tabs.get(4)).add(obj);
            }

            //add sixth tab container objects
            if(i < portalSprites.sprites.size()) {
                x = Constants.ButtonOffsetX + (currentSprite.column * (Constants.BigButtonWidth)) + (currentSprite.column * Constants.ButtonSpacingHz);
                y = Constants.ButtonOffsetY + (currentSprite.row * (Constants.BigButtonHeight)) + (currentSprite.row * Constants.ButtonSpacingVt);

                obj = createTabObject(new Pair(x, y), portalSprites.sprites.get(i), buttonSprites.sprites.get(0), buttonSprites.sprites.get(1),
                        Constants.BigButtonWidth, Constants.BigButtonHeight);

                obj.addComponent(new BoxBounds(44, 85, true));

                if(i == 0) {
                    obj.addComponent(new Portal(PlayerState.Flying));
                } else {
                    obj.addComponent(new Portal(PlayerState.Normal));
                }
                this.tabMaps.get(tabs.get(5)).add(obj);
            }
        }
    }

    /**
     * Helper method to add the properties of the tab objects.
     * @param pos position to start
     * @param objectSprite object sprite
     * @param buttonNormalSprite button not-pressed sprite
     * @param buttonPressedSprite button pressed sprite
     * @param width width
     * @param height height
     * @return the tab object
     */
    private GameObject createTabObject(Pair pos, Sprite objectSprite, Sprite buttonNormalSprite, Sprite buttonPressedSprite, int width, int height) {
        GameObject obj = new GameObject("Gen", new Transform(new Pair(pos.x, pos.y)), 10);
        obj.setUI(true);
        obj.setNonserializable();
        obj.addComponent(objectSprite);
        MenuItem menuItem = new MenuItem((int)pos.x, (int)pos.y, width, height,
                buttonNormalSprite, buttonPressedSprite, this);
        obj.addComponent(menuItem);

        return obj;
    }

    /**
     * The UI Components are actually separate from the scene, so this function starts the UI system.
     */
    @Override
    public void start() {
        for (GameObject tab : tabs) {
            for (GameObject g : tabMaps.get(tab)) {
                for (Component c : g.getAllComponents()) {
                    c.start();
                }
            }
        }
    }

    /**
     * Updates the container with all the tabs and buttons.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        for(GameObject g :this.tabMaps.get(hotTab)) {
            g.update(dTime);

            //if it is selected and is not the hot button, then deselect it
            MenuItem menuItem = g.getComp(MenuItem.class);
            if(g != hotButton && menuItem.isSelected) {
                menuItem.isSelected = false;
            }
        }
        //if it is selected and is not the hot tab, then deselect it
        for(GameObject g :this.tabs) {
            TabItem tabItem = g.getComp(TabItem.class);
            if(g != hotTab && tabItem.isSelected) {
                tabItem.isSelected = false;
            }
        }
    }

    /**
     * Draws the container on the screen.
     * @param g2 graphics handler
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.containerBg.img, 0, Constants.ContainerOffY, this.containerBg.width, this.containerBg.height, null);

        for(GameObject g :this.tabMaps.get(hotTab)) {
            g.draw(g2);
        }
    }

    /**
     * Does nothing. There is no need for copies of main container.
     * @return nothing
     */
    @Override
    public Component copy() {
        return null;
    }


    /**
     * Don't need to save, we will be building it anyway.
     * @param tabSize number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return "";
    }

    /**
     * Sets the current button as active.
     * @param obj the button selected
     */
    public void setHotButton(GameObject obj) {
        this.hotButton = obj;
    }

    /**
     * Sets the current tab as active.
     * @param obj the tab selected
     */
    public void setHotTab(GameObject obj) {
         this.hotTab = obj;
    }
}
