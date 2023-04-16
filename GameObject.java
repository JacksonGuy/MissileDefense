import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

abstract class GameObject extends JComponent {
    protected BufferedImage sprite;
    protected Rectangle rect;
    protected int x;
    protected int y;
    protected int speed;

    /**
	 * Initializes instance variables
	 * @param ImageName the sprite image
	 * @param x x position on screen
	 * @param y y position on screen
	 * @param speed how much to move by
	 */
    public GameObject(String ImageName, int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        try {
            sprite = ImageIO.read(new File(ImageName));
            rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the sprite on the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(sprite, x, y, null);
    }

    // Getter methods
	public int getX() {return x;}
	public int getY() {return y;}
	public int getSpeed() {return speed;}
	public Rectangle getRect() {return rect;}
	public BufferedImage getSprite() {return sprite;}

	// Setter methods
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setSpeed(int speed) {this.speed = speed;}
	public void setRect(Rectangle rect) {this.rect = rect;}
}
