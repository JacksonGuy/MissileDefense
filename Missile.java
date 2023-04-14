import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public class Missile extends GameObject {

	public Missile(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);
	}

	public void move(ArrayList<Missile> list) {
		y -= speed;
		rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());

		// Despawn missile after it leaves the screen
		if (y < 0) {
			list.remove(this);
		}
	}

	// Getters
	public Rectangle getRect() {return rect;}
}
