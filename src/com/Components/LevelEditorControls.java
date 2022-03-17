package com.Components;

import com.Game.Component;
import com.Game.GameObject;
import com.Game.LevelEditorScene;
import com.Game.Window;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

enum Direction {
    Up, Down, Left, Right
}

public class LevelEditorControls extends Component {

    private float debounceTime = 0.1f;      //every 0.1 sec we will register one click -> no spamming blocks
    private float debounceLeft = 0.0f;

    private float debounceKey = 0.24f;
    private float debounceKeyLeft = 0.0f;

    private boolean shiftKeyPressed = false;

    int gridWidth, gridHeight;

    private float worldX, worldY;
    private boolean isEditing = false;

    private boolean wasDragged = false;
    private float dragX, dragY, dragWidth, dragHeight;

    private List<GameObject> selectedObjects;

    public LevelEditorControls(int gridWidth, int gridHeight) {
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.selectedObjects = new ArrayList<>();
    }

    public void updateSpritePosition() {
        this.worldX = (float)Math.floor((Window.getWindow().mouseListener.x + Window.getWindowCamX() + Window.getWindow().mouseListener.dx) / gridWidth);
        this.worldY = (float)Math.floor((Window.getWindow().mouseListener.y + Window.getWindowCamY() + Window.getWindow().mouseListener.dy) / gridHeight);

        this.gameObject.transform.pos.x = worldX * gridWidth - Window.getWindowCamX();       //transforms it to be local to the window
        this.gameObject.transform.pos.y = worldY * gridHeight - Window.getWindowCamY();
    }

    public void copyGameObjectToScene() {
        GameObject obj = gameObject.copy();
        obj.transform.pos = new TwoPair(this.worldX * gridWidth, this.worldY * gridHeight);
        Window.getWindow().getCurrentScene().addGameObject(obj);
    }

    public void addGameObjectToSelected(TwoPair mousePos) {
        mousePos.x += Window.getWindowCamX();
        mousePos.y += Window.getWindowCamY();
        for(GameObject obj : Window.getScene().getAllGameObjects()) {
            Bounds bounds = obj.getComp(Bounds.class);
            if(bounds != null && bounds.rayCast(mousePos)) {
                selectedObjects.add(obj);
                bounds.isSelected = true;
                break;
            }
        }
    }

    public List<GameObject> boxCast(float x, float y, float width, float height) {
        float x0 = x + Window.getWindowCamX();
        float y0 = y + Window.getWindowCamY();

        List<GameObject> objs = new ArrayList<>();
        for(GameObject obj : Window.getScene().getAllGameObjects()) {
            Bounds bounds = obj.getComp(Bounds.class);
            if(bounds != null) {
                if(obj.getPosX() >= x0 && obj.getPosX() + bounds.getWidth() <= x0 + width &&
                   obj.getPosY() >= y0 && obj.getPosY() + bounds.getHeight() <= y0 + height) {
                        objs.add(obj);
                }
            }
        }
        return objs;
    }

    public void clearSelectedObjectsAndAdd(TwoPair mousePos) {
        clearSelected();
        addGameObjectToSelected(mousePos);
    }

    public void escapeKeyPressed() {
        GameObject newGameObj = new GameObject("Mouse Cursor", this.gameObject.transform.copy(), this.gameObject.zIndex);
        newGameObj.addComponent(this);
        LevelEditorScene scene = (LevelEditorScene)Window.getScene();
        scene.mouseCursor = newGameObj;
        isEditing = false;
        clearSelected();
    }

    public void clearSelected() {
        for(GameObject obj : selectedObjects) {
            obj.getComp(Bounds.class).isSelected = false;
        }
        selectedObjects.clear();
    }

    @Override
    public void update(double dTime) {
        debounceLeft -= dTime;
        debounceKeyLeft -= dTime;
        if (!isEditing && this.gameObject.getComp(Sprite.class) != null) {
            this.isEditing = true;
        }

        if(isEditing) {
            if(selectedObjects.size() != 0) {
                clearSelected();
            }
            updateSpritePosition();
        }

        if(Window.getWindow().mouseListener.y < Constants.TabOffY &&
            worldY * gridHeight < Constants.GroundY &&
            Window.getWindow().mouseListener.mousePressed &&
            Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1 && debounceLeft < 0) {
            //Mouse clicked indicator
            debounceLeft = debounceTime;

            if(isEditing) {
                copyGameObjectToScene();
            } else if(Window.keyListener().isKeyPressed(KeyEvent.VK_SHIFT)) {
                addGameObjectToSelected(new TwoPair(Window.mouseListener().x, Window.mouseListener().y));
            } else {
                clearSelectedObjectsAndAdd(new TwoPair(Window.mouseListener().x, Window.mouseListener().y));
            }
        } else if(!Window.mouseListener().mousePressed && wasDragged) {
            wasDragged = false;
            clearSelected();
            List<GameObject> objs = boxCast(dragX, dragY, dragWidth, dragHeight);
            for(GameObject obj : objs) {
                selectedObjects.add(obj);
                Bounds b = obj.getComp(Bounds.class);
                if(b != null) {
                    b.isSelected = true;
                }
            }
        }

        if(Window.keyListener().isKeyPressed(KeyEvent.VK_ESCAPE)) {
            escapeKeyPressed();
        }

        if(Window.keyListener().isKeyPressed(KeyEvent.VK_SHIFT)) {
            shiftKeyPressed = true;
        } else {
            shiftKeyPressed = false;
        }

        if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_LEFT)) {
            moveObjects(Direction.Left, shiftKeyPressed? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_RIGHT)) {
            moveObjects(Direction.Right, shiftKeyPressed? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_UP)) {
            moveObjects(Direction.Up, shiftKeyPressed? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_DOWN)) {
            moveObjects(Direction.Down, shiftKeyPressed? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_DELETE)) {
            for(GameObject obj: selectedObjects) {
                Window.getScene().removeGameObject(obj);
            }
            selectedObjects.clear();
        }

        if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_D)) {
            if(Window.keyListener().isKeyPressed(KeyEvent.VK_CONTROL)) {
                duplicate();
                debounceKeyLeft = debounceKey;
            }
        }

        if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_Q)) {
            rotateObjects(90);
            debounceKeyLeft = debounceKey;
        } else if(debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_E)) {
            rotateObjects(-90);
            debounceKeyLeft = debounceKey;
        }
    }

    public void rotateObjects(float degrees) {
        for(GameObject obj : selectedObjects) {
            obj.transform.rotation += degrees;
            TriangleBounds b = obj.getComp(TriangleBounds.class);
            if(b != null) b. calculateTransform();
        }
    }

    public void moveObjects(Direction direction, float scale) {
        TwoPair distance = new TwoPair(0.0f, 0.0f);

        switch (direction) {
            case Up:
                distance.y = -Constants.TileHeight * scale;
                break;
            case Down:
                distance.y = Constants.TileHeight * scale;
                break;
            case Left:
                distance.x = -Constants.TileHeight * scale;
                break;
            case Right:
                distance.x = Constants.TileHeight * scale;
                break;
            default:
                System.out.println("Error Direction has no enum " + direction + "");
                System.exit(-1);
                break;
        }

        for(GameObject obj : selectedObjects) {
            obj.transform.pos.x += distance.x;
            obj.transform.pos.y += distance.y;
            float gridX = (float)((Math.floor(obj.getPosX() / Constants.TileWidth) + 1) * Constants.TileWidth);
            float gridY = (float)(Math.floor(obj.getPosY() / Constants.TileHeight) * Constants.TileHeight);

            if(obj.getPosX() < gridX + 1 && obj.getPosX() > gridX - 1) {
                obj.transform.pos.x = gridX;
            }
            if(obj.getPosY() < gridY + 1 && obj.getPosY() > gridY - 1) {
                obj.transform.pos.y = gridY;
            }

            TriangleBounds b = obj.getComp(TriangleBounds.class);
            if(b != null) b. calculateTransform();
        }
    }

    public void duplicate() {
        for(GameObject obj : selectedObjects) {
            Window.getScene().addGameObject(obj.copy());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if(isEditing) {
            Sprite sprite = gameObject.getComp(Sprite.class);           //we are having it as a game object cuz we will add several components to this
            if (sprite != null) {                                        //so when we copy it to the screen it will come with all of its properties
                float alpha = 0.5f;         //a little transparent before placing
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);     //apply the alpha over the entire image
                g2.setComposite(ac);
                g2.drawImage(sprite.img, (int) gameObject.getPosX(), (int) gameObject.getPosY(), (int) sprite.width, (int) sprite.height, null);
                alpha = 1.0f;                                                           //set it to full transparency
                ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);        //draw it non transparent
                g2.setComposite(ac);
            }
        } else if(Window.mouseListener().mouseDragged && Window.mouseListener().mouseButton == MouseEvent.BUTTON1) {
            wasDragged = true;
            g2.setColor(new Color(1,1,1,0.3f));
            dragX = Window.mouseListener().x;
            dragY = Window.mouseListener().y;
            dragWidth = Window.mouseListener().dx;
            dragHeight = Window.mouseListener().dy;
            if(dragWidth < 0) {
                dragWidth *= -1;
                dragX -= dragWidth;
            }
            if(dragHeight < 0) {
                dragHeight *= -1;
                dragY -= dragHeight;
            }
            g2.fillRect((int)dragX, (int)dragY, (int)dragWidth, (int)dragHeight);
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
