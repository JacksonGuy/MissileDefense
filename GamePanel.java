import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * This class contains the paintable objects such as the enemies,
 * turret, and missile. It also keeps track of the 
 * 
 * @author Dr. Garrett Goodman
 */
public class GamePanel extends JPanel {
	BufferedImage background;

	private int totalScore;
	private Turret turret;

	// Lists of different GameObjects
	private ArrayList<Enemy> enemyList;
	private ArrayList<Missile> missileList;
	private ArrayList<Powerup> powerups;

	// Controls how many enemies are on the screen
	private int maxEnemyAmount;
	private int smallEnemyAmount;
	private int maxBigEnemy;
	private int bigEnemyAmount;

	// Used to generate random numbers
	private Random rand;

	// Keeps track of time between inputs. Mostly here to stop bugs
	private long lastFiredTime;
	private long lastPausePress;
	private long lastHelpPress;

	// Powerup Spawns
	private long lastPowerup;
	private int powerupTime;

	// GameObject speeds
	private int smallEnemySpeed;
	private int bigEnemySpeed;
	private int missileSpeed;
	private int powerupSpeed;

	// How much health each BigEnemy spawns with
	private int bigEnemyHealth;

	// Time Constants
	private final int bigEnemySpawnTime = 3000; // 3 Seconds
	private final int missileShootTime = 500; // 0.5 Seconds

	// Menus
	private boolean gamePaused;
	private boolean showHelp; 

	/**
	 * Initializes all instance variables
	 */
	public GamePanel() {
		try {
            background = ImageIO.read(new File("./images/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

		gamePaused = false;
		showHelp = false;

		totalScore = 0;

		maxEnemyAmount = 3;
		smallEnemyAmount = 0;
		bigEnemyAmount = 0;
		maxBigEnemy = 1;

		smallEnemySpeed = 2;
		bigEnemySpeed = 1;
		missileSpeed = 5;
		powerupSpeed = 3;

		bigEnemyHealth = 100;

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
		g.drawImage(background, 0, 0, null);

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setFont(new Font("Serif", Font.PLAIN, 15));
		g2.setColor(Color.GRAY);

		// Calls Turret's paintComponent method
		turret.paintComponent(g);

		// Draw all GameObjects
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).paintComponent(g);
		}
		for (int i = 0; i < missileList.size(); i++) {
			missileList.get(i).paintComponent(g);
		}
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).paintComponent(g);
		}

		// Draw UI text
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

	/**
	 * Looks for collisions between powerups and the player 
	 */
	public void detectPowerups() {
		for (int i = 0; i < powerups.size(); i++) {
			Rectangle pRect = powerups.get(i).rect.getBounds();
			if (pRect.intersects(turret.rect.getBounds())) {

				// Powerup 1: slows all small enemies by 50%
				if (powerups.get(i).getType().equals("slow")) {
					for (int j = 0; j < enemyList.size(); j++) {
						if (enemyList.get(j) instanceof SmallEnemy) {
							enemyList.get(j).speed = 1;
						}
					}
				}
				// Powerup 2: Gives player +50 health
				else if (powerups.get(i).getType().equals("health")) {
					turret.setHealth(turret.getHealth() + 50);
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

		// Move missiles
		for (int i = 0; i < missileList.size(); i++) {
			missileList.get(i).move(missileList);
		}

		// Move Enemies
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT);

			// Big Enemy reached bottom of the screen
			if (enemyList.get(i).getY() >= Tester.WINDOW_HEIGHT - 128) {
				// Substract health
				turret.setHealth(turret.getHealth() - 50);
				
				// Despawn enemy and adjust variables
				if (enemyList.get(i) instanceof BigEnemy) {
					bigEnemyAmount--;
					BigEnemy.lastAlive = System.currentTimeMillis();
				} else {
					smallEnemyAmount--;
				}
				enemyList.remove(i);
			}
		}

		// Move powerups
		for (int i = 0; i < powerups.size(); i++) {
			powerups.get(i).move(Tester.WINDOW_WIDTH, Tester.WINDOW_WIDTH);

			// Remove powerup once off screen
			if (powerups.get(i).getY() >= Tester.WINDOW_HEIGHT) {
				powerups.remove(i);
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
			if (bigEnemyAmount < maxBigEnemy && System.currentTimeMillis() - BigEnemy.lastAlive > bigEnemySpawnTime) {
				// More than 3 seconds since last BigEnemy died
				int x = 64 + rand.nextInt(436);
				BigEnemy bigboy = new BigEnemy("./images/BigEnemy.png", x, 64, bigEnemySpeed, bigEnemyHealth);
				enemyList.add(bigboy);
				bigEnemyAmount++;
			}
			// If there's already the maximum number of big enemies on screen, spawn a small enemy 
			else if (smallEnemyAmount < maxEnemyAmount-1) {
				int x = 64 + rand.nextInt(436);
				int y = 64 + rand.nextInt(250);
				SmallEnemy smallboy = new SmallEnemy("./images/SmallEnemy.png", x, y, smallEnemySpeed);
				enemyList.add(smallboy);
				smallEnemyAmount++;
			}
		}
	}

	/**
	 * Spawns a random powerup every (powerupTime) milliseconds  
	 */
	public void addPowerup() {
		if (gamePaused) {
			return;
		}

		// Enough time has passed to spawn new powerup
		if (System.currentTimeMillis() - lastPowerup > powerupTime) {
			int x = 64 + rand.nextInt(436);
			int type = rand.nextInt(2);

			if (type == 0) {
				Powerup p = new Powerup("./images/slow.png", x, 64, powerupSpeed, "slow");
				powerups.add(p);
				lastPowerup = System.currentTimeMillis();
			}
			else if (type == 1) {
				Powerup p = new Powerup("./images/health.png", x, 64, powerupSpeed, "health");
				powerups.add(p);
				lastPowerup = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Removes enemy from game
	 * @param enemy the enemy object to remove
	 */
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
			turret.move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT, "Left");
		}

		// D
		if (keys.contains(68) && !gamePaused) {
			turret.move(Tester.WINDOW_WIDTH, Tester.WINDOW_HEIGHT, "Right");
		}

		// Space
		if (keys.contains(32)) {
			// Limits the number of missiles that can be fired
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

	/**
	 * Periodically increases how hard the game is every 1000 points earned
	 */
	public void increaseDifficulty() {
		if (totalScore < 1000) {
			return;
		}

		if ((totalScore % 1000) == 0 || (totalScore % 1000) == 50 || (totalScore % 1000) == 100) {
			int factor = totalScore / 1000;
			
			// Increase enemy count
			if (maxEnemyAmount % 2 != 0) {
				maxBigEnemy++;
			} 
			maxEnemyAmount++;

			// Increase health
			bigEnemyHealth = 100 + (25 * factor);
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
