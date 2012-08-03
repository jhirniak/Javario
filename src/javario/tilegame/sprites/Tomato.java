package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A Tomato is a Tomato.
    Tomato moves slowly on the ground.
    ...
    Tomato just is.
    Tomato embraces the intricate philosophy of Winnie-the-Pooh.
*/
public class Tomato extends Creature {

    public Tomato(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.10f;
    }

}
