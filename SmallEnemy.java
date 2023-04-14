import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

public class SmallEnemy extends Enemy {
    private int direction = 0; // 0 = Right, 1 = Left 

    public SmallEnemy(String ImageName, int x, int y, int speed) {
        super(ImageName, x, y, speed);
    }

    public void move(int width, int height) {
        if (direction == 0) {
            if (x < width - rect.width) {
                x += speed;
            } else {
                direction = 1;
            }
        } else {
            if (x > 0) {
                x -= speed;
            } else {
                direction = 0;
            }
        }
        rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Process what should happen after collision with a missile
     * @param game the GamePanel object
     */
    public void processCollision(GamePanel game, ArrayList<Enemy> list, int index) {
        game.removeEnemy(list.get(index));
        game.setSmallEnemyAmount(game.getSmallEnemyAmount() - 1);
        game.setTotalScore(game.getTotalScore() + 150);
    }
}
