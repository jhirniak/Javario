package javario.tilegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;

import javario.graphics.*;
import javario.sound.*;
import javario.input.*;
import javario.test.GameCore;
import javario.tilegame.sprites.*;

//for updating status bar
import javario.tilegame.TileMapRenderer;

//for text printing operations
import java.awt.FontMetrics;
import java.awt.Font;

import javario.tilegame.ResourceManager;

/**
    GameManager manages all parts of the game.
*/
public class GameManager extends GameCore {

    public static void main(String[] args) {
        new GameManager().run();
    }

    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    private static final AudioFormat PLAYBACK_FORMAT =
        new AudioFormat(44100, 16, 1, true, false);

    private static final int DRUM_TRACK = 1;

    public static final float GRAVITY = 0.002f;

	//private ResourceManager resourceManager;

    private Point pointCache = new Point();
    private TileMap map; //was for a moment public
    private MidiPlayer midiPlayer;
    private SoundManager soundManager;
    private ResourceManager resourceManager;
    private Sound prizeSound;
    private Sound boopSound;
	private Sound batterySound;
	private Sound portalSound;
	private Sound portalBackSound;
    private InputManager inputManager;
    private TileMapRenderer renderer;

    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction jump;
    private GameAction exit;
    private GameAction restartGame;
	private GameAction goToMap1;
    private GameAction goToMap2;
    private GameAction goToMap3;
    private GameAction goToMap4;
    private GameAction goToMap5;
    private GameAction goToMap6;
    private GameAction goToMap7;
    private GameAction goToMap8;
    private GameAction goToMap9;
    private GameAction goToMap10;
    private GameAction pauseGame;

	//Initializes points and lives assessing default values
	private int points = 0;
	private int lives = 3;
	private int counter9 = 0;
	private boolean hideText = false;

    public void init() {
        super.init();

        // set up input manager
        initInput();

        // start resource manager
        resourceManager = new ResourceManager(
        screen.getFullScreenWindow().getGraphicsConfiguration());

        // load resources
        renderer = new TileMapRenderer();
        renderer.setBackground(resourceManager.loadImage("background1.png"));

        // load first map
        map = resourceManager.loadNextMap();

        // load sounds
        soundManager = new SoundManager(PLAYBACK_FORMAT);
        prizeSound = soundManager.getSound("sounds/prize.wav");
        boopSound = soundManager.getSound("sounds/boop2.wav");
		batterySound = soundManager.getSound("sounds/battery.wav");
		portalSound = soundManager.getSound("sounds/portal.wav");
		portalBackSound = soundManager.getSound("sounds/portalBack.wav");

        // start music
        //midiPlayer = new MidiPlayer();
        /*Sequence sequence =
            midiPlayer.getSequence("sounds/music.midi");
        midiPlayer.play(sequence, true);
        toggleDrumPlayback();
		*/
		midiPlayer = new MidiPlayer();
		changeMusic(1);
    }

	public void changeMusic(int stage) {
		midiPlayer.close();
		midiPlayer = new MidiPlayer();
        Sequence sequence =
        midiPlayer.getSequence("sounds/backgrounds/stage" + stage + ".mid");
        midiPlayer.play(sequence, true);
        toggleDrumPlayback();
	}

	public void changeBackground(int backgroundCode) {
        renderer.setBackground(
            resourceManager.loadImage("background" + backgroundCode + ".png"));
		changeMusic(backgroundCode);
	}

    /**
        Closes any resources used by the GameManager.
    */
    public void stop() {
        super.stop();
        midiPlayer.close();
        soundManager.close();
    }

    public void pause() {
	tooglePause();
	while (!pauseGame.isPressed()) {
		//do nothing
	}
	tooglePause();
    }


    private void initInput() {
        moveLeft = new GameAction("moveLeft");
        moveRight = new GameAction("moveRight");
        jump = new GameAction("jump",
            GameAction.DETECT_INITAL_PRESS_ONLY);
        exit = new GameAction("exit",
            GameAction.DETECT_INITAL_PRESS_ONLY);
		restartGame = new GameAction("restartGame",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
		goToMap1 = new GameAction("goToMap1",
			GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap2 = new GameAction("goToMap2",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap3 = new GameAction("goToMap3",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap4 = new GameAction("goToMap4",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap5 = new GameAction("goToMap5",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap6 = new GameAction("goToMap6",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap7 = new GameAction("goToMap7",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap8 = new GameAction("goToMap8",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap9 = new GameAction("goToMap9",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
        goToMap10 = new GameAction("goToMap10",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);
		pauseGame = new GameAction("pauseGame",
	    	GameAction.DETECT_INITAL_PRESS_ONLY);

        inputManager = new InputManager(
            screen.getFullScreenWindow());
        inputManager.setCursor(InputManager.INVISIBLE_CURSOR);

        inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
        inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);
        inputManager.mapToKey(jump, KeyEvent.VK_UP);
        inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
        inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
        inputManager.mapToKey(restartGame, KeyEvent.VK_R);
		inputManager.mapToKey(goToMap1, KeyEvent.VK_1);
		inputManager.mapToKey(goToMap2, KeyEvent.VK_2);
		inputManager.mapToKey(goToMap3, KeyEvent.VK_3);
		inputManager.mapToKey(goToMap4, KeyEvent.VK_4);
		inputManager.mapToKey(goToMap5, KeyEvent.VK_5);
		inputManager.mapToKey(goToMap6, KeyEvent.VK_6);
		inputManager.mapToKey(goToMap7, KeyEvent.VK_7);
		inputManager.mapToKey(goToMap8, KeyEvent.VK_8);
		inputManager.mapToKey(goToMap9, KeyEvent.VK_9);
		inputManager.mapToKey(goToMap10, KeyEvent.VK_0);
		inputManager.mapToKey(pauseGame, KeyEvent.VK_P);
    }


    private void checkInput(long elapsedTime) {

        if (exit.isPressed()) {
            stop();
        }

        Player player = (Player)map.getPlayer();
        if (player.isAlive()) {
            float velocityX = 0;
            if (moveLeft.isPressed()) {
                velocityX-=player.getMaxSpeed();
            }
            if (moveRight.isPressed()) {
                velocityX+=player.getMaxSpeed();
            }
            if (jump.isPressed()) {
                player.jump(false);
            }
	    if (restartGame.isPressed()) {
			points = 0;
			lives = 3;
			hideText = false;
			resourceManager.setCurrentMap(1);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
		if (goToMap1.isPressed()) {
			resourceManager.setCurrentMap(1);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
			return;
		}
	    if (goToMap2.isPressed()) {
			resourceManager.setCurrentMap(2);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap3.isPressed()) {
			resourceManager.setCurrentMap(3);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap4.isPressed()) {
			resourceManager.setCurrentMap(4);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap5.isPressed()) {
			resourceManager.setCurrentMap(5);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap6.isPressed()) {
			resourceManager.setCurrentMap(6);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap7.isPressed()) {
			resourceManager.setCurrentMap(7);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap8.isPressed()) {
			resourceManager.setCurrentMap(8);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap9.isPressed()) {
			resourceManager.setCurrentMap(9);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
	    if (goToMap10.isPressed()) {
			resourceManager.setCurrentMap(10);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
            return;
	    }
            if (pauseGame.isPressed()) {
	         pause();
    	    }
            player.setVelocityX(velocityX);
        }

    }


    public void draw(Graphics2D g) {
        renderer.draw(g, map,
            screen.getWidth(), screen.getHeight());

		//updates status bar (points, lives)
		updateStatusBar();
    }

	//update status bar
	public void updateStatusBar() {
		//define variables
		Graphics2D g = screen.getGraphics();
		int currentMap = resourceManager.getCurrentMap();
		Player player = (Player)map.getPlayer();
		
		//define parameters
		//g.setColor(Color.YELLOW);
		g.setColor(new Color(0xFF,0xCC,0));
		if (currentMap == 4) {
			g.setColor(new Color(0xAE,0,0));
		}

		//define content
		String[] mapDescription = {
			"Something is Wrong",
			"Escape Java Virtual Machine",
			"From Digital to Analog - Escape from Computer",
			"The Programmers Kitchen",
			"City - Way to the Outerspace",
			"Outerspace",
			"Approaching Starducks Mothership",
			"Coffee Aggregator",
			"Control Room"
			};
		String description = "";
		if (mapDescription.length >= currentMap) {
			description = " - " + mapDescription[currentMap-1];
		}
		else {
			description = "";
		}

		//Text anti-aliasing
   		if (g instanceof Graphics2D) {
    	    Graphics2D g2 = (Graphics2D)g;
        	g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    	}

		FontMetrics fm = g.getFontMetrics();
		int titleWidth = fm.stringWidth("Current Map: " + currentMap + description);
		int screenCenter = screen.getWidth()/2;

		if (currentMap == 1 && !hideText) {
			int baseSize = 30;
			int lineHeight = 45;
			int i = lineHeight;
			g.setFont(new Font("Arial", Font.PLAIN, baseSize));
			g.setColor(new Color(0xFF,0xCC,0));
			FontMetrics fm1 = g.getFontMetrics();
			String[] text1 = {
				"Starducks are evil creatures from the dark part of the universe.",
				"In order to conquer the Earth they planed to make people write bad software",
				"what will ultimately cause enormous chaos around the globe.",
				"The easiest way to do this is to cut the programmers' fuel, which is coffee.",
				"Tired and unhappy developers would start ultimately to write bad software.",
				"",
				"On the day all coffee is stolen by Starducks, Javario realizes something is wrong by",
				"the fact how Java Virtual Machine (JVM) where he lives is acting.",
				"",
				"Javario bravely decides to help humans and save the world before the evil Starducks.",
				"",
				"Grab the key and go through the portal."
			};
			
			
			for (String t : text1) {
				g.drawString(t, screenCenter-fm1.stringWidth(t)/2, i+=lineHeight);
			}
		}
		else if (currentMap == 10) {
			g.setFont(new Font("Arial", Font.PLAIN, 60));
			g.setColor(new Color(0xAE,0,0));
			FontMetrics fm9 = g.getFontMetrics();
			String t1 = "You wrote " + points + " lines of great code!";
			String t2 = "Well done.";
			if (points == 0) {
				t1 = "You wrote no code!";
				t2 = "How this suppose to work?";
			}
			else if (points == 1) {
				t1 = "One line of code. Bravo!";
				t2 = "Please, press Enter after \";\" next time.";
			}
			else if (points > 1 && points < 50) {
				t1 = "You wrote " + points + " lines of great code!";
				t2 = "Well done and optimal.";
			}
			else {
				t1 = "You typed like a crazy " + points + " lines of code!";
				t2 = "Is there any feature it does not have?";
			}
			//String t1 = "You wrote " + points + " lines of great code!";
			g.drawString(t1, screenCenter-fm9.stringWidth(t1)/2, 90);
			//String t2 = "Well done.";
			g.drawString(t2, screenCenter-fm9.stringWidth(t2)/2, 150);

			int baseSize = 32;
			int lineHeight = 45;
			int i = lineHeight+105;
			g.setFont(new Font("Arial", Font.PLAIN, baseSize));
			//g.setColor(new Color(0x44,0x11,0x11));
			g.setColor(new Color(0x22,0x22,0x22));
			FontMetrics fm10 = g.getFontMetrics();

			String[] text10 = {
				"Once the coffee has been restored everything had come back to normal,",
				"except the fact that, the Starducks burned by Javario blast and rosted by the sun,",
				"continued to fall down from the sky for a few days more days.",
				"Ultimately, they become another most valuable asset on the Earth",
				"as their meat has been found to be delicious, delectable and full of coffee.",
				"~ THE END ~"
			};

			for (String t : text10) {
					g.drawString(t, screenCenter-fm10.stringWidth(t)/2, i+=lineHeight);
				}
		/*
			g.setColor(new Color(0xFF,0xFF,0xFF));
			i = 104-lineHeight;
			for (String t : text10) {
					g.drawString(t, screenCenter-fm10.stringWidth(t)/2-1, i+=lineHeight);
				}
		*/
		}
		else {
			g.drawString("Lines of Code: " + points, screenCenter-200-fm.stringWidth("Lines of Code:" + points)/2, 30);
			g.drawString("Lives: " + lives, screenCenter-fm.stringWidth("Lives:" + lives)/2, 30);
			g.drawString("Force: " + player.getForce(), screenCenter+200-fm.stringWidth("Force:" + player.getForce())/2, 30);
			g.drawString("Current Map: " + currentMap + description, (screen.getWidth()-titleWidth)/2, 75); //screen.getWidth()/2
		}

		if (currentMap == 10) {
			if (counter9 % 5000000 == 0) {
				counter9 = 0;
				resourceManager.addSprite(getMap(), '.', (int) (Math.random() * 42) , 5 );
			}
		}
	}


    /**
        Gets the current map.
    */
    public TileMap getMap() {
        return map;
    }


    /**
        Turns on/off drum playback in the midi music (track 1).
    */
    public void toggleDrumPlayback() {
        Sequencer sequencer = midiPlayer.getSequencer();
        if (sequencer != null) {
            sequencer.setTrackMute(DRUM_TRACK,
                !sequencer.getTrackMute(DRUM_TRACK));
        }
    }


    /**
        Gets the tile that a Sprites collides with. Only the
        Sprite's X or Y should be changed, not both. Returns null
        if no collision is detected.
    */
    public Point getTileCollision(Sprite sprite,
        float newX, float newY)
    {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

        // get the tile locations
        int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
        int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
        int toTileX = TileMapRenderer.pixelsToTiles(
            toX + sprite.getWidth() - 1);
        int toTileY = TileMapRenderer.pixelsToTiles(
            toY + sprite.getHeight() - 1);

        // check each tile for a collision
        for (int x=fromTileX; x<=toTileX; x++) {
            for (int y=fromTileY; y<=toTileY; y++) {
                if (x < 0 || x >= map.getWidth() ||
                    map.getTile(x, y) != null)
                {
                    // collision found, return the tile
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        // no collision found
        return null;
    }


    /**
        Checks if two Sprites collide with one another. Returns
        false if the two Sprites are the same. Returns false if
        one of the Sprites is a Creature that is not alive.
    */
    public boolean isCollision(Sprite s1, Sprite s2) {
        // if the Sprites are the same, return false
        if (s1 == s2) {
            return false;
        }

        // if one of the Sprites is a dead Creature, return false
        if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
            return false;
        }
        if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
            return false;
        }

        // get the pixel location of the Sprites
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        // check if the two sprites' boundaries intersect
        return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
    }


    /**
        Gets the Sprite that collides with the specified Sprite,
        or null if no Sprite collides with the specified Sprite.
    */
    public Sprite getSpriteCollision(Sprite sprite) {

        // run through the list of Sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite otherSprite = (Sprite)i.next();
            if (isCollision(sprite, otherSprite)) {
                // collision found, return the Sprite
                return otherSprite;
            }
        }

        // no collision found
        return null;
    }


    /**
        Updates Animation, position, and velocity of all Sprites
        in the current map.
    */
    public void update(long elapsedTime) {
        Creature player = (Creature)map.getPlayer();


        // player is dead! start map over
        if (player.getState() == Creature.STATE_DEAD) {
			lives--; //removes one live
            map = resourceManager.reloadMap();
            return;
        }

		//If dead restart game to the first map, set also variables to proper values
		if (lives <= 0) {
			resourceManager.setCurrentMap(1);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
			lives = 3;
			points = 0;
			hideText = true;
		}

        // get keyboard/mouse input
        checkInput(elapsedTime);

        // update player
        updateCreature(player, elapsedTime);
        player.update(elapsedTime);

        // update other sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (sprite instanceof Creature) {
                Creature creature = (Creature)sprite;
                if (creature.getState() == Creature.STATE_DEAD) {
                    i.remove();
                }
                else {
                    updateCreature(creature, elapsedTime);
                }
            }
            // normal update
            sprite.update(elapsedTime);
        }
    }


    /**
        Updates the creature, applying gravity for creatures that
        aren't flying, and checks collisions.
    */
    private void updateCreature(Creature creature,
        long elapsedTime)
    {

        // apply gravity
        if (!creature.isFlying()) {
            creature.setVelocityY(creature.getVelocityY() +
                GRAVITY * elapsedTime);
        }

        // change x
        float dx = creature.getVelocityX();
        float oldX = creature.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile =
            getTileCollision(creature, newX, creature.getY());
        if (tile == null) {
            creature.setX(newX);
        }
        else {
            // line up with the tile boundary
            if (dx > 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x) -
                    creature.getWidth());
            }
            else if (dx < 0) {
                creature.setX(
                    TileMapRenderer.tilesToPixels(tile.x + 1));
            }
            creature.collideHorizontal();
        }
        if (creature instanceof Player) {
            checkPlayerCollision((Player)creature, false);
        }

        // change y
        float dy = creature.getVelocityY();
        float oldY = creature.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(creature, creature.getX(), newY);
        if (tile == null) {
            creature.setY(newY);
        }
        else {
            // line up with the tile boundary
            if (dy > 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y) -
                    creature.getHeight());
            }
            else if (dy < 0) {
                creature.setY(
                    TileMapRenderer.tilesToPixels(tile.y + 1));
            }
            creature.collideVertical();
        }
        if (creature instanceof Player) {
            boolean canKill = (oldY < creature.getY());
            checkPlayerCollision((Player)creature, canKill);
        }

    }


    /**
        Checks for Player collision with other Sprites. If
        canKill is true, collisions with Creatures will kill
        them.
    */
    public void checkPlayerCollision(Player player,
        boolean canKill)
    {
        if (!player.isAlive()) {
            return;
        }

        // check for player collision with other sprites
        Sprite collisionSprite = getSpriteCollision(player);
        if (collisionSprite instanceof PowerUp) {
            acquirePowerUp((PowerUp)collisionSprite);
        }
        else if (collisionSprite instanceof Creature) {
            Creature badguy = (Creature)collisionSprite;
            if (canKill) {
                // kill the badguy and make player bounce
                soundManager.play(boopSound);
                badguy.setState(Creature.STATE_DYING);
                player.setY(badguy.getY() - player.getHeight());
                player.jump(true);
                //This is the ugliest part in all the code,
                //it is for final stage, to make the boss harder,
                //I did not had enough time to write it properly.
                //Mankind, forgive me this.
                //This will be certainly refactored afterwards
                //for my peace of mind and sake of human beings.
				if (badguy.getState() == Creature.STATE_NORMAL) {
					int hit = badguy.getForce();
					int separator = 3;
					int mapWidth = getMap().getWidth() - separator*2;
					if (hit == 8) {
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64.0-4.0) % mapWidth + separator), (int) (badguy.getY()/64.0-2.0));
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64.0-6.0) % mapWidth + separator), (int) (badguy.getY()/64.0-1.0));
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64.0-4.0) % mapWidth + separator), (int) (badguy.getY()/64.0));
						resourceManager.addSprite(getMap(), '1', (int) ( (badguy.getX()/64.0-3.0) % mapWidth + separator), (int) (badguy.getY()/64.0-1.0));
						resourceManager.addSprite(getMap(), '1', (int) ( (badguy.getX()/64.0-4.0) % mapWidth + separator), (int) (badguy.getY()/64.0));						
						resourceManager.addSprite(getMap(), '1', (int) ( (badguy.getX()/64.0-5.0) % mapWidth + separator), (int) (badguy.getY()/64.0+1.0));
					}
					/*else if (hit == 5) {
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-3.0) % mapWidth + separator), (int) (badguy.getY()/64+3));
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-4.0) % mapWidth + separator), (int) (badguy.getY()/64+2));
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-5.0) % mapWidth + separator), (int) (badguy.getY()/64+1));
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-5.0) % mapWidth + separator), (int) (badguy.getY()/64));
						//resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-4.0) % mapWidth + separator), (int) (badguy.getY()/64-1.0));
						//resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-3.0) % mapWidth + separator), (int) (badguy.getY()/64-2.0));
					}
					else if (hit == 3) {
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-3.0) % mapWidth + separator), (int) (badguy.getY()/64-3.0));
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64-2.0)% mapWidth + separator), (int) (badguy.getY()/64-2.0));
						resourceManager.addSprite(getMap(), '3', (int) ( (badguy.getX()/64+3.0) % mapWidth + separator), (int) (badguy.getY()/64-3.0));
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64-3.0) % mapWidth + separator), (int) (badguy.getY()/64+3.0));
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64+2.0) % mapWidth + separator), (int) (badguy.getY()/64+2.0));
						resourceManager.addSprite(getMap(), '2', (int) ( (badguy.getX()/64+3.0) % mapWidth + separator), (int) (badguy.getY()/64+3.0));
					}*/
					else if (hit == 1) {
						resourceManager.addSprite(getMap(), '-', (int) ( (badguy.getX()/64-3.0) % mapWidth + separator), (int) (badguy.getY()/64+3));
						resourceManager.addSprite(getMap(), '-', (int) ( (badguy.getX()/64+3.0) % mapWidth + separator), (int) (badguy.getY()/64+3));
					}		
					else {
						int amount = (int) (Math.random() * 4+1);
						for (int i = 0; i < amount; i++)
							resourceManager.addSprite(getMap(), (char) ((int) '1' + (int) (Math.random()*5)), (int) ( (badguy.getX()/64 - i * 2 - 3) %mapWidth + separator), (int) (badguy.getY()/64-(i+1)));
					}
				}
				points++; //when killed baddies update point value
            }
            else {
                // player dies!
                player.setState(Creature.STATE_DYING);
            }
        }
    }


    /**
        Gives the player the speicifed power up and removes it
        from the map.
    */
    public void acquirePowerUp(PowerUp powerUp) {
        // remove it from the map
        map.removeSprite(powerUp);

        if (powerUp instanceof PowerUp.Star) {
            // do something here, like give the player points
            soundManager.play(prizeSound);
			points++; //when collected point update points
        }
        else if (powerUp instanceof PowerUp.Music) {
            // change the music
            soundManager.play(prizeSound);
            toggleDrumPlayback();
        }
		else if (powerUp instanceof PowerUp.Battery) {
			soundManager.play(batterySound);
			lives++;
		}
		else if (powerUp instanceof PowerUp.Force) {
			//soundManager.play(forceSound);
			Player player = (Player)map.getPlayer();
			player.setForce(player.getForce() + 1);
			//resourceManager.addSprite(getMap(), 'o', 1, 1);
		}
		else if (powerUp instanceof PowerUp.Key) {
			//soundManager.play(keySound);
			resourceManager.addSprite(getMap(), '*', 21, 8);
			hideText = true;
		}
		else if (powerUp instanceof PowerUp.KeyRestart) {
			//soundManager.play(keySound);
			points = 0;
			lives = 3;
			hideText = false;
			resourceManager.setCurrentMap(1);
			map = resourceManager.reloadMap();
			changeBackground(resourceManager.getCurrentMap());
		}
        else if (powerUp instanceof PowerUp.Goal) {
            // advance to next map
            soundManager.play(portalSound,
                new EchoFilter(2000, .7f), false);
            map = resourceManager.loadNextMap();
	    	changeBackground(resourceManager.getCurrentMap());
			//changeMusic(resourceManager.getCurrentMap());
            //resourceManager.reloadMap();
        }
		else if (powerUp instanceof PowerUp.StartPortal) {
			soundManager.play(portalBackSound,
				new EchoFilter(2000, .7f), false);
			map = resourceManager.loadPrevMap();
			changeBackground(resourceManager.getCurrentMap());
			//changeMusic(resourceManager.getCurrentMap());
		}
    }

}
