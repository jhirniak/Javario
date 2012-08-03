package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    An EmperorClone is a Creature that has all properties of Emperor(Duck).
    It appears when Emperor(Duck) is close to its death. It replicates
    the Emperor(Duck), but it takes only 2 hits to kill it.
*/
public class EmperorClone extends Creature {

    public EmperorClone(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
		setForce(1);
    }


    public float getMaxSpeed() {
        return 0.25f;
    }

}
