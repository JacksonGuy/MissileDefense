import java.awt.*;
import java.util.ArrayList;

public class SmallEnemy extends Enemy {
    private int direction = 0; // 0 = Right, 1 = Left 

    /**
     * {@inheritDoc}
     */
    public SmallEnemy(String ImageName, int x, int y, int speed) {
        super(ImageName, x, y, speed);
    }

    /**
     * Updates the position each game loop
     * @param width the maximum X position
     * @param height the maximum Y position
     */
    public void move(int width, int height) {
        // Moving right
        if (direction == 0) {
            // Keep moving right until at the edge of the screen
            if (x < width - rect.width) {
                x += speed;
            } else {
                // At the edge of the screen, so reverse direction
                // and move down
                direction = 1;
                y += 128;
            }
        } 
        // Moving left
        else {
            if (x > 0) {
                // Keep moving left until at the edge of the screen
                x -= speed;
            } else {
                // At the edge of the screen, so reverse direction
                // and move down
                direction = 0;
                y += 128;
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

        game.increaseDifficulty();;
    }
}
