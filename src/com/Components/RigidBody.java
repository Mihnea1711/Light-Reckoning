package com.Components;

import com.Game.Component;
import com.Utilities.Constants;
import com.Utilities.Pair;

/**
 * The way the player reacts to any physical constraints.
 */
public class RigidBody extends Component {

    public Pair speed;   //velocity

    /**
     * Constructor for the rigid body.
     * @param speed the speed of the object
     */
    public RigidBody(Pair speed) {
        this.speed = speed;
    }

    /**
     * Updates the velocity.
     * @param dTime frames
     */
    @Override
    public void update(double dTime) {
        //first add velocity, then add acceleration, if we  do have acceleration
        gameObject.transform.pos.y += speed.y * dTime;
        gameObject.transform.pos.x += speed.x * dTime;

        speed.y += Constants.Gravity * dTime;
        if(Math.abs(speed.y) > Constants.Terminal_Speed) {
            speed.y = Math.signum(speed.y) * Constants.Terminal_Speed;          //negative stays negative, positive stays positive
        }
    }

    //TODO:: might need to implement it for the enemy (the player won't be the only rigid body anymore)

    /**
     * Copy method for the player.
     * @return a new object with the same properties as the player
     */
    @Override
    public Component copy() {
        return null;
    }

    /**
     * Don't need to save, we will be building it anyway.
     * @param tabSize   number of tabs to be indented correctly
     * @return nothing
     */
    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
