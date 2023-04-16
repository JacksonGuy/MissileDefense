import java.util.ArrayList;

abstract class Enemy extends GameObject {
	/**
	 * {@inheritDoc}
	 */
	public Enemy(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);
	}

	public abstract void move(int width, int height);
	public abstract void processCollision(GamePanel game, ArrayList<Enemy> list, int index);
}
