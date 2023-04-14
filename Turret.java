import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Turret extends GameObject {
	private int health;
	private int damage;

	public Turret(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);

		health = 100;
		damage = 1;
	}

	public void move(int width, int height) {
		// Move the turret horizontally
		if ((x + speed >= 0) && (x + sprite.getWidth() + speed <= width)) {
			x += speed;
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
