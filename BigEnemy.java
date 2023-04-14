import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public class BigEnemy extends Enemy {
    public static long lastAlive;
    private int health;
    
    public BigEnemy(String ImageName, int x, int y, int speed) {
        super(ImageName, x, y, speed);
        health = 100;
        lastAlive = System.currentTimeMillis();
    }
    
    public void move(int width, int height) {
        if (y < height) {
            y += speed;
        }
        rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());   
    }
    
    public void processCollision(GamePanel game, ArrayList<Enemy> list, int index) {
        health -= 50;

        if (health <= 0) {
            game.removeEnemy(list.get(index));
            game.setBigEnemyAmount(game.getBigEnemyAmount() - 1);
            game.setTotalScore(game.getTotalScore() + 100);
            lastAlive = System.currentTimeMillis();
        }
    }
}
