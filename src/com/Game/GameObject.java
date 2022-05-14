package com.Game;

import com.DataStructures.Transform;
import com.File.Parser;
import com.File.Serialize;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Core component for any engine.
 */
public class GameObject extends Serialize {
    private List<Component> componentList;              //list of components attached to the object(each will have update method & draw methods)
    private String name;                                //debugging purpose

    public Transform transform;                         //the transform of the object
    public boolean isSerializable = true;
    public int zIndex;
    public boolean isUI = false;

    /**
     * Constructor for the class
     * @param name  name of object
     * @param transform position of object
     * @param zIndex index on the "Z axis" for the object
     */
    public GameObject(String name, Transform transform, int zIndex) {           //transform = position + properties
        this.name = name;
        this.transform = transform;
        this.componentList = new ArrayList<>();
        this.zIndex = zIndex;
    }

    /**
     * Generic function to get the component from the list
     * @param componentClass    the class of the component we want
     * @param <T>               type of the object
     * @return                  the component
     */
    public <T extends Component> T getComp(Class<T> componentClass) {
        for(Component c : componentList){
            if(componentClass.isAssignableFrom(c.getClass())){      //if these 2 have the same class
                try {
                    return componentClass.cast(c);      //casting to the type of c.getClass
                } catch(ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;        //indicator that there was no component to be found inside the gameObject of that class
    }

    /**
     * Removes the component from an object
     * @param componentClass component class
     * @param <T> type of the component
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(Component c : componentList) {
            if(componentClass.isAssignableFrom(c.getClass())){
                componentList.remove(c);
                return;
            }
        }
    }

    /**
     * @return all the components of an object
     */
    public List<Component> getAllComponents() {
        return this.componentList;
    }

    /**
     * Adds a component to the game object
     * @param c component to add to the game object
     */
    public void addComponent(Component c){
        componentList.add(c);
        c.gameObject = this;
    }

    /**
     * Creates a new object with the same properties, instead of passing a reference around
     * @return new object = copy of a game object
     */
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

    /**
     * Updates the whole object with all the components
     * @param dTime frames
     */
    public void update(double dTime){
        for(Component c : componentList){
            c.update(dTime);
        }
    }

    public void setNonserializable() {
        isSerializable = false;
    }

    /**
     * Used to draw every game object if any of the components has something to be drawn (ex : sprite)
     * @param g2 graphics handler
     */
    public void draw(Graphics2D g2) {
        for(Component c : componentList){
            c.draw(g2);
        }
    }

    /**
     * Helper method
     * @return object x position
     */
    public float getPosX() {
        return transform.pos.x;
    }

    /**
     * Helper method
     * @return object y position
     */
    public float getPosY() {
        return transform.pos.y;
    }

    /**
     * Helper method
     * @return object x scale
     */
    public float getScaleX() {
        return transform.scale.x;
    }

    /**
     * Helper method
     * @return object y scale
     */
    public float getScaleY() {
        return transform.scale.y;
    }

    /**
     * Helper method
     * @return object rotation
     */
    public float getRotation() {
        return transform.rotation;
    }

    /**
     * Serializes the game object.
     * @param tabSize   number of tabs to be indented correctly
     * @return the game object serialized as a string
     */
    @Override
    public String serialize(int tabSize) {
        if(!isSerializable) return "";
        StringBuilder builder = new StringBuilder(); //slightly faster than the normal way to concatenate strings
        //Game object
        builder.append(beginObjectProperty("GameObject", tabSize));

        //Transform
        builder.append(transform.serialize(tabSize + 1));
        builder.append(addEnding(true, true));

        //Name
        builder.append(addStringProperty("Name", name, tabSize + 1, true, true));

        //ZIndex and components
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

    /**
     * Deserializes the game object
     * @return a new game object with all its deserialized components
     */
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

    /**
     * Method to set the object as UI or not.
     * If an object is UI, we don't have to render it, because it is being drawn specially.
     * @param val true/false whether we want the object to be UI or not
     */
    public void setUI(boolean val) {
        this.isUI = val;
    }
}
