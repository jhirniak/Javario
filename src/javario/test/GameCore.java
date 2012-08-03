package javario.test;

import java.awt.*;
import javax.swing.ImageIcon;

import javario.graphics.ScreenManager;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCore {

    protected static final int FONT_SIZE = 24;
    
    private static final DisplayMode POSSIBLE_MODES[] = {
    	getSystemDisplay(),
    	new DisplayMode(1366, 768, 32, 0),	//added to run game on mine laptop screen native resolution (1366x768 px)
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),
    };

    private boolean isRunning;
    private boolean isPaused = false;
  
    protected ScreenManager screen;

    public void tooglePause() {
	if (isPaused == false) {
	    isPaused = true;
	}
	else {
	    isPaused = false;
	}
    }

    public boolean getIsPaused() {
	return this.isPaused;
    }

    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }


    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
            init();
            gameLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }


    /**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
    */
    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() {
        screen = new ScreenManager();
        DisplayMode displayMode =
            screen.findFirstCompatibleMode(POSSIBLE_MODES);
        screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.blue);
        window.setForeground(Color.white);

        isRunning = true;
    }


    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

	/**
	Tries to read the system screen sizes, then uses
	the parameters of the display 0.
	 */    
	//My Ubuntu returns -1 for getBitDepth, why?
	public static DisplayMode getSystemDisplay()
	{
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();
	    DisplayMode dm = gs[0].getDisplayMode();
	    System.out.println("Screen resolution: "+dm.getWidth()+"x"+dm.getHeight()+"px"+", depth: "+dm.getBitDepth());
	    return dm;
	}    
    
    /**
        Runs through the game loop until stop() is called.
    */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
	while (!isPaused && isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
	
            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
	}
	}        
    }


    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update(long elapsedTime) {
        // do nothing
    }


    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);
}
