import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public class Powerup extends GameObject {
    private String type;

    public Powerup(String ImageName, int x, int y, int speed, String type) {
        super(ImageName, x, y, speed);
        this.type = type;
    }

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
