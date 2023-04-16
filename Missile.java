import java.awt.*;
import java.util.ArrayList;

public class Missile extends GameObject {

	/**
	 * {@inheritDoc}
	 */
	public Missile(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);
	}

	/**
	 * Updates missile position every game loop
	 * @param list the list of Missile objects
	 */
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
