package javario.tilegame.sprites;

import javario.graphics.Animation;

/**
    The Player.
*/
public class Player extends Creature {

    private static final float JUMP_SPEED = -.95f;

    private boolean onGround;
    private int jumpCounter;

	private Animation leftSuper;
    private Animation rightSuper;

    public Player(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }

	public void setForce(int force) {
		super.setForce(force);
	}

    public void collideHorizontal() {
        setVelocityX(0);
    }


    public void collideVertical() {
        // check if collided with ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }


    public void setY(float y) {
        // check if falling
        if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }


    public void wakeUp() {
        // do nothing
    }


    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump(boolean forceJump) {
        //if (onGround || forceJump) {
        //    onGround = false;
        //    setVelocityY(JUMP_SPEED);
        //}
	if (onGround || forceJump) {
            onGround = false;
	    jumpCounter = 0;
            setVelocityY(JUMP_SPEED);
        }
	else if (jumpCounter < 1) {
		jumpCounter++;
		setVelocityY(JUMP_SPEED*0.75f);
	}
    }


    public float getMaxSpeed() {
        return 0.5f;
    }

}
