package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    An Emperor(Duck) is a Creature that moves quite rapidly on the ground.
    It is overgrown, and it takes 5 hits to kill the bastard.
*/
public class Emperor extends Creature {

    public Emperor(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
		setForce(5);
    }


    public float getMaxSpeed() {
        return 0.25f;
    }

}
