package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A Broccoli is a Creature that moves slowly on the ground.
    It also contains many proteins and kids don't like it.
*/
public class Broccoli extends Creature {

    public Broccoli(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.10f;
    }

}
