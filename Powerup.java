import java.awt.*;

public class Powerup extends GameObject {
    private String type;

    /**
     * Initializes instance variables
     * @param ImageName the image name
     * @param x the X position on the screen
     * @param y the Y position on the screen
     * @param speed the amount to move by each game loop
     * @param type which powerup the object is
     */
    public Powerup(String ImageName, int x, int y, int speed, String type) {
        super(ImageName, x, y, speed);
        this.type = type;
    }

    /**
     * Update the position each game loop
     * @param width the maximum X position
     * @param height the maximum Y position
     */
    public void move(int width, int height) {
        if (y < height) {
            y += speed;
        }
        rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    // Getters
    public String getType() {return type;}

    // Setters
    public void setType(String type) {this.type = type;}
}
