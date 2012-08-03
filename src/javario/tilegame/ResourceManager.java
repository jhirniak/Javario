package javario.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import javario.graphics.*;
import javario.tilegame.sprites.*;

/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class ResourceManager {

    private ArrayList tiles;
    private int currentMap;
    private GraphicsConfiguration gc;

    // host sprites used for cloning
    private Sprite playerSprite;
    private Sprite musicSprite;
    private Sprite coinSprite;
    private Sprite goalSprite;
    private Sprite startPortalSprite;
	private Sprite batterySprite;
	private Sprite forceSprite;
	private Sprite keySprite;
	private Sprite keyRestartSprite;
    private Sprite cyberSprite;
    private Sprite rubberSprite;
    private Sprite nyanSprite;
    private Sprite roboSprite;
    private Sprite tomatoSprite;
    private Sprite broccoliSprite;
	private Sprite emperorSprite;
	private Sprite emperorCloneSprite;
	private Sprite deadDuckSprite;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public ResourceManager(GraphicsConfiguration gc) {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }

	//get current map number
	public int getCurrentMap() {
    	return this.currentMap;
	}

	//set current map number
	public void setCurrentMap(int currentMap) {
		this.currentMap = currentMap;
	}

    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }

    public TileMap loadNextMap() {
        TileMap map = null;
        while (map == null) {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) {
                if (currentMap == 1) {
                    // no maps to load!
                    return null;
                }
                currentMap = 0;
                map = null;
            }
        }

        return map;
    }

    public TileMap loadPrevMap() {
        TileMap map = null;
        while (map == null) {
            currentMap--;
	    if (currentMap < 1) { currentMap = 9; }
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt", 14 - (int) (Math.random() * 14) + 2 );
            }
            catch (IOException ex) {
                if (currentMap == 1) {
                    // no maps to load!
                    return null;
                }
                currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() {
        try {
            return loadMap(
                "maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private TileMap loadMap(String filename)
        throws IOException
    {

        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '#') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
				else if (ch == '&') {
		    		addSprite(newMap, startPortalSprite, x, y);
				}
				else if (ch == '@') {
					addSprite(newMap, batterySprite, x, y);
				}
				else if (ch == '~') {
					addSprite(newMap, forceSprite, x, y);
				}
				else if (ch == '=') {
					addSprite(newMap, keySprite, x, y);
				}
				else if (ch == 'r') {
					addSprite(newMap, keyRestartSprite, x, y);
				}
                else if (ch == '1') {
                    addSprite(newMap, cyberSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, rubberSprite, x, y);
                }
                else if (ch == '3') {
                    addSprite(newMap, nyanSprite, x, y);
                }
				else if (ch == '4') {
		    		addSprite(newMap, roboSprite, x, y);
				}
				else if (ch == '5') {
		    		addSprite(newMap, tomatoSprite, x, y);
				}
				else if (ch == '6') {
		    		addSprite(newMap, broccoliSprite, x, y);
				}
				else if (ch == '^') {
					addSprite(newMap, emperorSprite, x, y);
				}
				else if (ch == '.') {
					addSprite(newMap, deadDuckSprite, x, y);
				}
            }
        }

        // add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapRenderer.tilesToPixels(3));
        player.setY(0);
        newMap.setPlayer(player);

        return newMap;
    }

   private TileMap loadMap(String filename, int bias)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }
                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
				else if (ch == '&') {
		    		addSprite(newMap, startPortalSprite, x, y);
				}
				else if (ch == '@') {
					addSprite(newMap, batterySprite, x, y);
				}
				else if (ch == '~') {
					addSprite(newMap, forceSprite, x, y);
				}
				else if (ch == '=') {
					addSprite(newMap, keySprite, x, y);
				}
				else if (ch == 'r') {
					addSprite(newMap, keyRestartSprite, x, y);
				}
                else if (ch == '1') {
                    addSprite(newMap, cyberSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, rubberSprite, x, y);
                }
                else if (ch == '3') {
                    addSprite(newMap, nyanSprite, x, y);
                }
				else if (ch == '4') {
		    		addSprite(newMap, roboSprite, x, y);
				}
				else if (ch == '5') {
		    		addSprite(newMap, tomatoSprite, x, y);
				}
				else if (ch == '6') {
		    		addSprite(newMap, broccoliSprite, x, y);
				}
				else if (ch == '^') {
					addSprite(newMap, emperorSprite, x, y);
				}
				else if (ch == '.') {
					addSprite(newMap, deadDuckSprite, x, y);
				}
				/*else if (ch == 'c') {
		    		addSprite(newMap, duckCorpusSprite, x, y);
				}*/
            }
        }

        // add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapRenderer.tilesToPixels(width-bias));
        player.setY(0);
        newMap.setPlayer(player);
        
        return newMap;
    }

    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapRenderer.tilesToPixels(tileX) +
                (TileMapRenderer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapRenderer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }

public void addSprite(TileMap newMap, char ch, int x, int y) {
                if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
				else if (ch == '&') {
		    		addSprite(newMap, startPortalSprite, x, y);
				}
				else if (ch == '@') {
					addSprite(newMap, batterySprite, x, y);
				}
				else if (ch == '~') {
					addSprite(newMap, forceSprite, x, y);
				}
				else if (ch == '=') {
					addSprite(newMap, keySprite, x, y);
				}
				else if (ch == 'r') {
					addSprite(newMap, keyRestartSprite, x, y);
				}
                else if (ch == '1') {
                    addSprite(newMap, cyberSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, rubberSprite, x, y);
                }
                else if (ch == '3') {
                    addSprite(newMap, nyanSprite, x, y);
                }
				else if (ch == '4') {
		    		addSprite(newMap, roboSprite, x, y);
				}
				else if (ch == '5') {
		    		addSprite(newMap, tomatoSprite, x, y);
				}
				else if (ch == '6') {
		    		addSprite(newMap, broccoliSprite, x, y);
				}
				else if (ch == '^') {
					addSprite(newMap, emperorSprite, x, y);
				}
				else if (ch == '-') {
					addSprite(newMap, emperorCloneSprite, x, y);
				}
				else if (ch == '.') {
					addSprite(newMap, deadDuckSprite, x, y);
				}
				/*else if (ch == 'c') {
		    		addSprite(newMap, duckCorpusSprite, x, y);
				}*/
    }

    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages() {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        while (true) {
            String name = "tile_" + ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) {
                break;
            }
            tiles.add(loadImage(name));
            ch++;
        }
    }


    public void loadCreatureSprites() {

        Image[][] images = new Image[4][];

        // load left-facing images
        images[0] = new Image[] {
            loadImage("duke0.png"),
            loadImage("duke1.png"),
            loadImage("duke2.png"),
            loadImage("duke3.png"),
            loadImage("rubber0.png"),
            loadImage("rubber1.png"),
            loadImage("rubber2.png"),
            loadImage("rubber3.png"),
            loadImage("cyber0.png"),
            loadImage("cyber1.png"),
            loadImage("cyber2.png"),
            loadImage("cyber3.png"),
            loadImage("nyan0.png"),
            loadImage("nyan1.png"),
            loadImage("nyan2.png"),
            loadImage("nyan3.png"),
            loadImage("nyan4.png"),
            loadImage("nyan5.png"),
            loadImage("robo0.png"),
            loadImage("robo1.png"),
            loadImage("robo2.png"),
            loadImage("robo3.png"),
			loadImage("tomato0.png"),
			loadImage("tomato1.png"),
			loadImage("tomato2.png"),
			loadImage("tomato3.png"),
			loadImage("broccoli0.png"),
			loadImage("broccoli1.png"),
			loadImage("broccoli2.png"),
			loadImage("emperor0.png"),
			loadImage("emperor1.png"),
			loadImage("duke0sup.png"),
			loadImage("duke1sup.png"),
			loadImage("duke2sup.png"),
			loadImage("duke3sup.png"),
			loadImage("deadDuck0.png"),
			loadImage("deadDuck1.png"),
			loadImage("bakedDuck0.png"),
			loadImage("bakedDuck1.png")
        };

        images[1] = new Image[images[0].length];
        images[2] = new Image[images[0].length];
        images[3] = new Image[images[0].length];
        for (int i=0; i<images[0].length; i++) {
            // right-facing images
            images[1][i] = getMirrorImage(images[0][i]);
            // left-facing "dead" images
            images[2][i] = getFlippedImage(images[0][i]);
            // right-facing "dead" images
            images[3][i] = getFlippedImage(images[1][i]);
        }

        // create creature animations
        Animation[] playerAnim = new Animation[4];
		Animation[] playerSupAnim = new Animation[4];
        Animation[] rubberAnim = new Animation[4];
        Animation[] cyberAnim = new Animation[4];
        Animation[] nyanAnim = new Animation[6];
		Animation[] roboAnim = new Animation[4];
		Animation[] tomatoAnim = new Animation[4];
		Animation[] broccoliAnim = new Animation[4];
		Animation[] emperorAnim = new Animation[4];
		Animation[] deadDuckAnim = new Animation[4];
        for (int i=0; i<4; i++) {
            playerAnim[i] = createPlayerAnim(
                images[i][0], images[i][1], images[i][2], images[i][3]);
            rubberAnim[i] = createRubberAnim(
                images[i][4], images[i][5], images[i][6], images[i][7]);
            cyberAnim[i] = createCyberAnim(
                images[i][8], images[i][9], images[i][10], images[i][11]);
            nyanAnim[i] = createNyanAnim(
                images[i][12], images[i][13], images[i][14], images[i][15], images[i][16], images[i][17] );
	    	roboAnim[i] = createRoboAnim(
				images[i][18], images[i][19], images[i][20], images[i][21]);
	    	tomatoAnim[i] = createTomatoAnim(
				images[i][22], images[i][23], images[i][24], images[i][25]);
	    	broccoliAnim[i] = createBroccoliAnim(
				images[i][26], images[i][27], images[i][28], images[i][26]);
			emperorAnim[i] = createEmperorAnim(
				images[i][29], images[i][30]);
			playerSupAnim[i] = createPlayerAnim(
				images[i][31], images[i][32], images[i][33], images[i][34]);
			deadDuckAnim[i] = createDeadDuckAnim(
				images[i][35], images[i][36], images[i][37], images[i][38]);
        }

        // create creature sprites
        playerSprite = new Player(playerAnim[0], playerAnim[1],
            playerAnim[2], playerAnim[3]);
        rubberSprite = new Rubber(rubberAnim[0], rubberAnim[1],
            rubberAnim[2], rubberAnim[3]);
        cyberSprite = new Cyber(cyberAnim[0], cyberAnim[1],
            cyberAnim[2], cyberAnim[3]);
        nyanSprite = new Nyan(nyanAnim[0], nyanAnim[1],
            nyanAnim[2], nyanAnim[3]);
		roboSprite = new Robo(roboAnim[0], roboAnim[1],
			roboAnim[2], roboAnim[3]);
		tomatoSprite = new Tomato(tomatoAnim[0], tomatoAnim[1],
			tomatoAnim[2], tomatoAnim[3]);
		broccoliSprite = new Broccoli(broccoliAnim[0], broccoliAnim[1],
			broccoliAnim[2], broccoliAnim[3]);
		emperorSprite = new Emperor(emperorAnim[0], emperorAnim[1],
			emperorAnim[2], emperorAnim[3]);
		emperorCloneSprite = new EmperorClone(emperorAnim[0], emperorAnim[1],
			emperorAnim[2], emperorAnim[3]);
		deadDuckSprite = new DeadDuck(deadDuckAnim[0], deadDuckAnim[1],
			deadDuckAnim[2], deadDuckAnim[3]);
    }


    private Animation createPlayerAnim(Image duke0,
        Image duke1, Image duke2, Image duke3)
    {
        Animation anim = new Animation();
        anim.addFrame(duke0, 300);
        anim.addFrame(duke1, 100);
        anim.addFrame(duke2, 100);
        anim.addFrame(duke3, 100);
        return anim;
    }


    private Animation createRubberAnim(Image img1, Image img2,
        Image img3, Image img4)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 200);
        anim.addFrame(img2, 200);
        anim.addFrame(img3, 200);
        anim.addFrame(img4, 200);
        return anim;
    }


    private Animation createCyberAnim(Image img1, Image img2, Image img3, Image img4) {
        Animation anim = new Animation();
        anim.addFrame(img1, 200);
        anim.addFrame(img2, 200);
        anim.addFrame(img3, 200);
        anim.addFrame(img4, 200);
        return anim;
    }

    private Animation createNyanAnim(Image img1, Image img2, Image img3, Image img4, Image img5, Image img6) {
        Animation anim = new Animation();
        anim.addFrame(img1, 200);
        anim.addFrame(img2, 200);
        anim.addFrame(img3, 200);
        anim.addFrame(img4, 200);
        anim.addFrame(img5, 200);
        anim.addFrame(img6, 200);
        return anim;
    }


    private Animation createRoboAnim(Image img1, Image img2, Image img3, Image img4) {
        Animation anim = new Animation();
        anim.addFrame(img1, 200);
        anim.addFrame(img2, 200);
        anim.addFrame(img3, 200);
        anim.addFrame(img4, 200);
        return anim;
    }

    private Animation createTomatoAnim(Image img1, Image img2, Image img3, Image img4) {
	Animation anim = new Animation();
	anim.addFrame(img1, 200);
	anim.addFrame(img2, 200);
	anim.addFrame(img3, 200);
	anim.addFrame(img4, 200);
	return anim;
    }

    private Animation createBroccoliAnim(Image img1, Image img2, Image img3, Image img4) {
	Animation anim = new Animation();
	anim.addFrame(img1, 200);
	anim.addFrame(img2, 200);
	anim.addFrame(img3, 200);
	return anim;
    }

    private Animation createEmperorAnim(Image img1, Image img2) {
	Animation anim = new Animation();
	anim.addFrame(img1, 200);
	anim.addFrame(img2, 200);
	return anim;
    }

    private Animation createDeadDuckAnim(Image img1, Image img2, Image img3, Image img4) {
	Animation anim = new Animation();
	anim.addFrame(img1, 500);
	anim.addFrame(img2, 500);
	anim.addFrame(img3, 500);
	anim.addFrame(img4, 500);
	return anim;
    }

    private void loadPowerUpSprites() {
        // create "final portal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("finalPortal0.png"), 150);
        anim.addFrame(loadImage("finalPortal1.png"), 150);
        anim.addFrame(loadImage("finalPortal2.png"), 150);
        anim.addFrame(loadImage("finalPortal1.png"), 150);
        goalSprite = new PowerUp.Goal(anim);

		// create "start portal" sprite
		anim = new Animation();
		anim.addFrame(loadImage("startPortal0.png"), 150);
		anim.addFrame(loadImage("startPortal1.png"), 150);
		anim.addFrame(loadImage("startPortal2.png"), 150);
		anim.addFrame(loadImage("startPortal1.png"), 150);
		startPortalSprite = new PowerUp.StartPortal(anim);
	
		// create "battery" sprite
		anim = new Animation();
		anim.addFrame(loadImage("battery0.png"), 150);
		anim.addFrame(loadImage("battery1.png"), 150);
		anim.addFrame(loadImage("battery2.png"), 150);
		anim.addFrame(loadImage("battery1.png"), 150);
		batterySprite = new PowerUp.Battery(anim);

		// create "java force" sprite
		anim = new Animation();
		anim.addFrame(loadImage("javaForce0.png"), 150);
		anim.addFrame(loadImage("javaForce1.png"), 150);
		anim.addFrame(loadImage("javaForce2.png"), 150);
		anim.addFrame(loadImage("javaForce3.png"), 150);
		anim.addFrame(loadImage("javaForce4.png"), 150);
		forceSprite = new PowerUp.Force(anim);

		// create "key" sprite
		anim = new Animation();
		anim.addFrame(loadImage("key0.png"), 200);
		anim.addFrame(loadImage("key1.png"), 200);
		keySprite = new PowerUp.Key(anim);	

		// create "key restart" sprite from key sprite
		anim = new Animation();
		anim.addFrame(loadImage("key0.png"), 200);
		anim.addFrame(loadImage("key1.png"), 200);
		keyRestartSprite = new PowerUp.KeyRestart(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("star1.png"), 100);
        anim.addFrame(loadImage("star2.png"), 100);
        anim.addFrame(loadImage("star3.png"), 100);
        anim.addFrame(loadImage("star4.png"), 100);
        coinSprite = new PowerUp.Star(anim);

        // create "music" sprite
        anim = new Animation();
        anim.addFrame(loadImage("music1.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        anim.addFrame(loadImage("music3.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        musicSprite = new PowerUp.Music(anim);
    }

}
