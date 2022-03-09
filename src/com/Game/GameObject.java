package com.Game;

import com.DataStructures.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private List<Component> componentList;              //list of components attached to the object(have update & draw methods)
    private String name;                                //debugging purpose
    public Transform transform;                         //the transform of the object

    public GameObject(String name, Transform transform) {           //transform = position + properties
        this.name = name;
        this.transform = transform;
        this.componentList = new ArrayList<>();
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

    public void addComponent(Component c){
        componentList.add(c);
    }

    //updating the whole object with all the components
    public void update(double dTime){
        for(Component c : componentList){
            c.update(dTime);
        }
    }
}
