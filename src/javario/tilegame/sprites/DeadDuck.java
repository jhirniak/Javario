package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    A DeadDuck is a Duck Creature that is going to die very soon.
    Once it dies it should look like a fried chicken.
    It does not move, it only fails from the heavens.
*/
public class DeadDuck extends Creature {

	private static final int DIE_TIME = 2000;

    public DeadDuck(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
		//super.setLive(super.STATE_DYING);
    }


    public float getMaxSpeed() {
        return 0.00f;
    }

	public void fancyUpdate(long time) {
		if (time > 1750)
			super.setLive(super.STATE_DYING);
	}

}
