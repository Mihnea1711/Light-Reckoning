package com.Components;

import com.Game.Component;
import com.Utilities.Constants;
import com.Utilities.TwoPair;

//the way the player reacts to any physical constraints
public class RigidBody extends Component {

    public TwoPair speed;

    public RigidBody(TwoPair speed) {
        this.speed = speed;
    }

    @Override
    public void update(double dTime) {
        gameObject.transform.pos.y += speed.y * dTime;
        gameObject.transform.pos.x += speed.x * dTime;

        speed.y += Constants.Gravity * dTime;
        if(Math.abs(speed.y) > Constants.Terminal_Speed) {
            speed.y = Math.signum(speed.y) * Constants.Terminal_Speed;          //negative stays negative, positive stays positive
        }
    }

    @Override
    public Component copy() {
        return null;
    }
}
