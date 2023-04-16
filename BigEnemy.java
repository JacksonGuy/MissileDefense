import java.awt.*;
import java.util.ArrayList;

public class BigEnemy extends Enemy {
    // Controls how often new BigEnemies are spawned in
    public static long lastAlive;
    
    private int health;
    
    /**
     * Initializes instance variables
     * @param ImageName the image name
     * @param x the x position of the screen
     * @param y the y position of the screen
     * @param speed the amount to move by
     * @param health amount of health to start at
     */
    public BigEnemy(String ImageName, int x, int y, int speed, int health) {
        super(ImageName, x, y, speed);
        this.health = health;
        lastAlive = System.currentTimeMillis();
    }
    
    /**
     * Updates the position each game loop
     * @param width the maximum X position
     * @param height the maximum Y position
     */
    public void move(int width, int height) {
        if (y < height) {
            y += speed;
        }
        rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());   
    }
    
    /**
     * Decides what to do when the object collides with a missile
     * @param game the GamePanel object
     * @param list the list of enemies belonging to GamePanel
     * @param index the index of the enemy in the list
     */
    public void processCollision(GamePanel game, ArrayList<Enemy> list, int index) {
        health -= 50;

        // Enemy Killed
        if (health <= 0) {
            game.removeEnemy(list.get(index));
            game.setBigEnemyAmount(game.getBigEnemyAmount() - 1);
            game.setTotalScore(game.getTotalScore() + 100);

            game.increaseDifficulty();

            lastAlive = System.currentTimeMillis();
        }
    }
}
