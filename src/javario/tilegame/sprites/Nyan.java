package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    Nyan - see Nyan cat.
    Nyan cat - see Nyan.

    It is awesome and has rainbow, just see the video
    http://www.youtube.com/watch?v=QH2-TGUlwu4
    
    In the next edition of the game everything is going to be Nyan.
    
    Nyan is as fast as the player, it flies.
*/
public class Nyan extends Creature {

    public Nyan(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }


    public float getMaxSpeed() {
        return 0.50f;
    }

    public boolean isFlying() {
        return isAlive();
    }

}
