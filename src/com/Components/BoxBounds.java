package com.Components;

import com.Game.Component;

public class BoxBounds extends Component {
    public float width, height;

    public BoxBounds (float width, float height){
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(double dTime) {

    }

    @Override
    public Component copy() {
        return new BoxBounds(width, height);
    }
}
