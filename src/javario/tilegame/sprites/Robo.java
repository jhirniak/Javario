package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A Robo(Duck) is a Creature that moves slowly on the ground.
    It does not need grease to move around.
*/
public class Robo extends Creature {

    public Robo(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.20f;
    }

}
