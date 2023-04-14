import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

abstract class Enemy extends GameObject {
	public Enemy(String ImageName, int x, int y, int speed) {
		super(ImageName, x, y, speed);
	}

	public abstract void move(int width, int height);
	public abstract void processCollision(GamePanel game, ArrayList<Enemy> list, int index);
}
