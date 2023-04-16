import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;

/**
 * The driver class for Project 4. 
 * 
 * @author Dr. Garrett Goodman
 */
@SuppressWarnings("serial")
public class Tester extends JFrame {
	public static final int WINDOW_WIDTH = 700;
	public static final int WINDOW_HEIGHT = 500;
	private GamePanel panel;
	private KeyboardListener kblistener;

	/**
	 * Default constructor to control the game.
	 */
	public Tester() {
		// Setup the initial JFrame elements
		setTitle("Gaem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		setLayout(new BorderLayout());

		panel = new GamePanel();
		add(panel, BorderLayout.CENTER);

		centerFrame(this);
		setVisible(true);

		// Add the JLabel for the score
		// scoreLabel = new JLabel();
		// add(scoreLabel, BorderLayout.NORTH);

		kblistener = new KeyboardListener();
		addKeyListener(kblistener);
	}
	
	/**
	 * This method is called to start the video game which then
	 * calls the infinite game loop for the game.
	 */
	public void start() {
		gameLoop();
	}
	
	/**
	 * Method contains the game loop to move enemies, manage score,
	 * and check if the game is finished.
	 */
	public void gameLoop() {
		// Game loop
		while(true) {
			pauseGame();
			panel.input(kblistener); 
			panel.detectCollision();
			panel.detectPowerups();
			panel.move();
			panel.repaint();
			panel.addEnemy();
			panel.addPowerup();
		}  
	}

	/**
	 * Pauses the thread for 30ms to control the 
	 * speed of the animations.
	 */
	public void pauseGame() {
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method centers the frame in the middle of the screen.
	 * 
	 * @param frame to center with respect to the users screen dimensions.
	 */
	public void centerFrame(JFrame frame) {    
		int width = frame.getWidth();
		int height = frame.getHeight();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = ge.getCenterPoint();

		int xPosition = center.x - width/2, yPosition = center.y - height/2;
		frame.setBounds(xPosition, yPosition, width, height);
		frame.validate();
	}

	/**
	 * The main method to execute the program.
	 * 
	 * @param args Any inputs to the program when it starts.
	 */
	public static void main(String[] args) {
		Tester main = new Tester();
		main.start();
	}

}
