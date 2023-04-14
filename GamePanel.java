import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Font;

/**
 * This class contains the paintable objects such as the enemies,
 * turret, and missile. It also keeps track of the 
 * 
 * @author Dr. Garrett Goodman
 */
public class GamePanel extends JPanel {
	private int totalScore;
	private Turret turret;

	private ArrayList<Enemy> enemyList;
	private ArrayList<Missile> missileList;
	private ArrayList<Powerup> powerups;

	private int maxEnemyAmount;
	private int smallEnemyAmount;
	private int bigEnemyAmount;

	private Random rand;

	private long lastFiredTime;
	private long lastPausePress;
	private long lastHelpPress;

	private long lastPowerup;
	private int powerupTime;

	private int smallEnemySpeed;
	private int bigEnemySpeed;
	private int missileSpeed;

	private final int bigEnemySpawnTime = 3000; // 3 Seconds
	private final int missileShootTime = 500; // 0.5 Seconds

	private boolean gamePaused;
	private boolean showHelp; 

	/**
	 * Initializes all instance variables
	 */
	public GamePanel() {
		gamePaused = false;
		showHelp = false;

		totalScore = 0;

		maxEnemyAmount = 3;
		smallEnemyAmount = 0;
		bigEnemyAmount = 0;

		smallEnemySpeed = 2;
		bigEnemySpeed = 1;
		missileSpeed = 5;

		turret = new Turret("./images/turret.png", 350, 380, 10);
		add(turret);

		enemyList = new ArrayList<Enemy>();
		missileList = new ArrayList<Missile>();
		powerups = new ArrayList<Powerup>();

		rand = new Random();
		lastFiredTime = System.currentTimeMillis();
		lastPausePress = System.currentTimeMillis();
		lastHelpPress = System.currentTimeMillis();

		lastPowerup = System.currentTimeMillis();
		powerupTime = 10000; // 10 Seconds to start

		setVisible(true);
	}
	
	/**
	 * Paints the enemies and missiles when called and also paints
	 * the background of the panel White.
	 */
	@Override
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.setColor(Color.white); 
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.setColor(Color.black);

		// Calls Turret's paintComponent method
		turret.paintComponent(g);
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).paintComponent(g);
		}
		for (int i = 0; i < missileList.size(); i++) {
			missileList.get(i).paintComponent(g);
		}

		g2.drawString("Score: " + totalScore, 10, 20);
		g2.drawString("Health: " + turret.getHealth(), 10, 40);

		g2.drawString("Controls:", 550, 20);
		g2.drawString("Move: A/D", 550, 40);
		g2.drawString("Fire Missile: Space", 550, 60);
		g2.drawString("Pause: P", 550, 80);
		g2.drawString("Help: H", 550, 100);

		if (showHelp) {
			g2.drawString("Rules:", 100, 20);
			g2.drawString("1. Kill enemies to gain points.", 100, 40);
			g2.drawString("2. Don't let big enemies reach you.", 100, 60);
			g2.drawString("    If 2 big enemies reach you, the game ends.", 100, 80);
			g2.drawString("3. The game will progressively get harder the more points you earn.", 100, 100);
		}

		// Show game over if player health is 0
		if (turret.getHealth() <= 0) {
			gamePaused = true;
			showHelp = false;
			
			g2.setFont(new Font("Serif", Font.PLAIN, 36));
			g2.setColor(Color.red);
			g2.drawString("Game Over!", 200, 160);
		}
	} 

	/**
	 * Method detects the collision of the missile and all the enemies. This is done by
	 * drawing invisible rectangles around the enemies and missiles, if they intersect, then 
	 * they collide.
	 */
	public void detectCollision() {
		// Create temporary rectangles for every enemy and missile on the screen currently       
		for(int i = 0; i < enemyList.size(); i++) {
			Rectangle enemyRec = enemyList.get(i).getRect().getBounds();
			for(int j = 0; j < missileList.size(); j++) {
				Rectangle missileRec = missileList.get(j).getRect().getBounds();
				if(missileRec.intersects(enemyRec)) {
					// Delete missile
					missileList.remove(j);

					// Process Collision for enemy
					enemyList.get(i).processCollision(this, enemyList, i);
				}
			}
		}
	}

	public void detectPowerups() {
		for (int i = 0; i < powerups.size(); i++) {
			Rectangle pRect = powerups.get(i).rect.getBounds();
			if (pRect.intersects(turret.rect.getBounds())) {
				// Powerup 1: slows all small enemies by 50%
				if (powerups.get(i).getType().equals("slow")) {
					for (int j = 0; i < enemyList.size(); i++) {
						if (enemyList.get(j) instanceof SmallEnemy) {
							enemyList.get(j).speed = 1;
						}
					}
				}
				// Powerup 2: Gives player +50 health
				else if (powerups.get(i).getType().equals("health")) {
					turret.setHealth(turret.getHealth() + 50);
				}
				// Powerup 3: Clears all enemies currently on the screen
				else if (powerups.get(i).getType().equals("clear")) {
					for (int j = 0; j < enemyList.size(); j++) {
						enemyList.remove(j);
					}
				}

				powerups.remove(i);
			}
		}
	}

	/** 
	 * Adds missile to missile list
	 */
	public void addMissile(Missile missile) {
		missileList.add(missile);
	}

	/** 
	 * Updates the position of enemies and missiles
	 */
	public void move() {
		if (gamePaused) {
			return;
		}

		for (int i = 0; i < missileList.size(); i++) {
			missileList.get(i).move(missileList);
		}
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT);
			// Big Enemy reached bottom of the screen
			if (enemyList.get(i).getY() >= Tester.WINDOW_HEIGHT - 128) {
				turret.setHealth(turret.getHealth() - 50);
				enemyList.remove(enemyList.get(i));
				bigEnemyAmount--;
				BigEnemy.lastAlive = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Populates the screen with enemies
	 */
	public void addEnemy() {
		if (gamePaused) {
			return;
		}

		// Always keep enemy count at the maximum
		if (enemyList.size() < maxEnemyAmount) {
			// Prioritize spawning new big enemies
			if (bigEnemyAmount < 1 && System.currentTimeMillis() - BigEnemy.lastAlive > bigEnemySpawnTime) {
				// More than 3 seconds since last BigEnemy died
				int x = 64 + rand.nextInt(436);
				BigEnemy bigboy = new BigEnemy("./images/BigEnemy.png", x, 64, bigEnemySpeed);
				enemyList.add(bigboy);
				bigEnemyAmount++;
			}
			// If there's already a big enemy on screen, spawn a small enemy 
			else if (smallEnemyAmount < maxEnemyAmount-1) {
				int x = 64 + rand.nextInt(436);
				int y = 64 + rand.nextInt(250);
				SmallEnemy smallboy = new SmallEnemy("./images/SmallEnemy.png", x, y, smallEnemySpeed);
				enemyList.add(smallboy);
				smallEnemyAmount++;
			}
		}
	}

	public void addPowerup() {
		if (gamePaused) {
			return;
		}

		if (System.currentTimeMillis() - lastPowerup > powerupTime) {
			int x = 64 + rand.nextInt(436);
		}
	}

	public void removeEnemy(Enemy enemy) {
		enemyList.remove(enemy);
	}

	/**
	 * Handles player keyboard input
	 */
	public void input(KeyboardListener kblistener) {
		ArrayList<Integer> keys = kblistener.getKeys();
		// A
		if (keys.contains(65) && !gamePaused) {
			turret.move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT);
		}

		// D
		if (keys.contains(68) && !gamePaused) {
			turret.move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT);
		}

		// Space
		if (keys.contains(32)) {
			if (System.currentTimeMillis() - lastFiredTime > missileShootTime) {
				addMissile(new Missile("./images/missile.png", 
					turret.getX() + turret.getRect().width/2 - 8, 
					turret.getY(), 
					missileSpeed
				));
				lastFiredTime = System.currentTimeMillis();
			}
		}

		// P
		if (keys.contains(80) &&
			System.currentTimeMillis() - lastPausePress > 500) {
			gamePaused = !gamePaused;
			lastPausePress = System.currentTimeMillis();
		}

		// H
		if (keys.contains(72) &&
			System.currentTimeMillis() - lastHelpPress > 500) {
				showHelp = !showHelp;
				lastHelpPress = System.currentTimeMillis();
			}
	}

	// Getters
	public int getTotalScore() {return totalScore;}
	public int getSmallEnemyAmount() {return smallEnemyAmount;}
	public int getBigEnemyAmount() {return bigEnemyAmount;}

	// Setters
	public void setTotalScore(int score) {this.totalScore = score;}
	public void setSmallEnemyAmount(int amount) {this.smallEnemyAmount = amount;}
	public void setBigEnemyAmount(int amount) {this.bigEnemyAmount = amount;}
}
