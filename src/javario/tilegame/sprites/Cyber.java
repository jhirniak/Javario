package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A Cyber(Duck) is a Creature that moves slowly on the ground (and gets killed).
*/
public class Cyber extends Creature {

    public Cyber(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.05f;
    }

}
