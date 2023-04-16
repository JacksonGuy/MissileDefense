import java.awt.*;

public class Turret extends GameObject {
	private int health;
	private int damage;

	/**
	 * {@inheritDoc}
	 */
	public Turret(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);

		health = 100;
		damage = 1;
	}

	/**
	 * Updates the object's position every game loop
	 * @param width the maximum X position
	 * @param height the maximum Y position
	 * @param dir what direction to move in
	 */
	public void move(int width, int height, String dir) {
		// Move the turret horizontally
		if (dir.equals("Right")) {
			// Bounds movement
			if (x + sprite.getWidth() + speed < width) {
				x += speed;
			}
		}
		else {
			// Bounds movement
			if (x + speed > 0) {
				x -= speed;
			}
		}

		rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
	}

	// Getter methods
	public int getDamage() {return damage;}
	public int getHealth() {return health;}

	// Setter methods
	public void setDamage(int damage) {this.damage = damage;}
	public void setHealth(int health) {this.health = health;}
}
