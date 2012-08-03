package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A Rubber is a Creature that fly slowly in the air.
    It looks like its a real duck, but trust me it's genuine rubber.
*/
public class Rubber extends Creature {

    public Rubber(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.2f;
    }


    public boolean isFlying() {
        return isAlive();
    }

}
