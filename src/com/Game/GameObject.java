package com.Game;

import com.DataStructures.Transform;
import com.File.Parser;
import com.File.Serialize;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameObject extends Serialize {
    private List<Component> componentList;              //list of components attached to the object(have update & draw methods)
    private String name;                                //debugging purpose
    public Transform transform;                         //the transform of the object
    private boolean isSerializable = true;
    public int zIndex;

    public boolean isUI = false;

    public GameObject(String name, Transform transform, int zIndex) {           //transform = position + properties
        this.name = name;
        this.transform = transform;
        this.componentList = new ArrayList<>();
        this.zIndex = zIndex;
    }

    //generic method
    //generic type of component (<T extends Component>)
    public <T extends Component> T getComp(Class<T> componentClass) {
        for(Component c : componentList){
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);      //casting to the type of c.getClass
                } catch(ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;        //indicator that there was no object to be found inside the gameobject of that class
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(Component c : componentList) {
            if(componentClass.isAssignableFrom(c.getClass())){
                componentList.remove(c);
                return;
            }
        }
    }

    public List<Component> getAllComponents() {
        return this.componentList;
    }

    public void addComponent(Component c){
        componentList.add(c);
        c.gameObject = this;
    }

    public GameObject copy() {
        GameObject newGameObject = new GameObject("Generated", transform.copy(), this.zIndex);
        for(Component c : componentList) {
            Component copy = c.copy();
            if(copy != null) {
                newGameObject.addComponent(copy);
            }
        }
        return newGameObject;
    }

    //updating the whole object with all the components
    public void update(double dTime){
        for(Component c : componentList){
            c.update(dTime);
        }
    }

    public void setNonserializable() {
        isSerializable = false;
    }

    //used to draw every game object if any of the components has smth to be drawn(sprite)
    public void draw(Graphics2D g2) {
        for(Component c : componentList){
            c.draw(g2);
        }
    }

    public float getPosX() {
        return transform.pos.x;
    }

    public float getPosY() {
        return transform.pos.y;
    }

    public float getScaleX() {
        return transform.scale.x;
    }

    public float getScaleY() {
        return transform.scale.y;
    }

    public float getRotation() {
        return transform.rotation;
    }

    @Override
    public String serialize(int tabSize) {
        if(!isSerializable) return "";
        StringBuilder builder = new StringBuilder(); //slightly faster than the normal way to concatenate strings
        //game object
        builder.append(beginObjectProperty("GameObject", tabSize));

        //transform
        builder.append(transform.serialize(tabSize + 1));
        builder.append(addEnding(true, true));

        builder.append(addStringProperty("Name", name, tabSize + 1, true, true));

        //Name
        if(componentList.size() > 0) {
            builder.append(addIntProperty("ZIndex", this.zIndex, tabSize + 1, true, true));
            builder.append(beginObjectProperty("Components", tabSize + 1));
        } else {
            builder.append(addIntProperty("ZIndex", this.zIndex, tabSize + 1, true, false));
        }

        int i = 0;
        for (Component c : componentList) {
            String str = c.serialize(tabSize + 2);
            if(str.compareTo("") != 0) {
                builder.append(str);
                if(i != componentList.size() - 1) {
                    builder.append(addEnding(true, true));
                } else {
                    builder.append(addEnding(true, false));
                }
            }
            i++;
        }
        if(componentList.size() > 0) {
            builder.append(closeObjectProperty(tabSize + 1));
        }

        builder.append(addEnding(true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static GameObject deserialize() {
        Parser.consumeBeginObjectProperty("GameObject");

        Transform transform = Transform.deserialize();
        Parser.consume(',');
        String name = Parser.consumeStringProperty("Name");
        Parser.consume(',');
        int zIndex = Parser.consumeIntProperty("ZIndex");

        GameObject obj = new GameObject(name, transform, zIndex);
        if(Parser.peek() == ',') {                          //if we have components, then we will have , after name
            Parser.consume(',');
            Parser.consumeBeginObjectProperty("Components");
            obj.addComponent(Parser.parseComponent());

            while(Parser.peek() == ',') {
                Parser.consume(',');
                obj.addComponent(Parser.parseComponent());
            }
            Parser.consumeEndObjectProperty();
        }
        Parser.consumeEndObjectProperty();

        return obj;
    }

    public void setUI(boolean val) {
        this.isUI = val;
    }
}
