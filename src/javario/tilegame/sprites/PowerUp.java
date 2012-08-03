package javario.tilegame.sprites;

import java.lang.reflect.Constructor;
import javario.graphics.*;

/**
    A PowerUp class is a Sprite that the player can pick up.
*/
public abstract class PowerUp extends Sprite {

    public PowerUp(Animation anim) {
        super(anim);
    }


    public Object clone() {
        // use reflection to create the correct subclass
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Animation)anim.clone()});
        }
        catch (Exception ex) {
            // should never happen
            ex.printStackTrace();
            return null;
        }
    }


    /**
        A Star PowerUp. Gives the player points.
    */
    public static class Star extends PowerUp {
        public Star(Animation anim) {
            super(anim);
        }
    }


    /**
        A Music PowerUp. Changes the game music.
    */
    public static class Music extends PowerUp {
        public Music(Animation anim) {
            super(anim);
        }
    }


    /**
        A Goal PowerUp. Advances to the next map.
    */
    public static class Goal extends PowerUp {
        public Goal(Animation anim) {
            super(anim);
        }
    }

    public static class FinalPortal extends PowerUp {
        public FinalPortal(Animation anim) {
            super(anim);
        }
    }

    public static class StartPortal extends PowerUp {
        public StartPortal(Animation anim) {
            super(anim);
        }
    }

    public static class Battery extends PowerUp {
        public Battery(Animation anim) {
            super(anim);
        }
    }

    public static class Force extends PowerUp {
        public Force(Animation anim) {
            super(anim);
        }
    }

    public static class Key extends PowerUp {
        public Key(Animation anim) {
            super(anim);
        }
    }

    public static class KeyRestart extends PowerUp {
        public KeyRestart(Animation anim) {
            super(anim);
        }
    }

}
