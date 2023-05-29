package entity;

import static entity.Direction.UP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

public abstract class Entity extends RectF implements Runnable{

    private int spriteCounter = 0;
    private int spriteNumber = 1;
    private final int spriteInterval = 24;

    private int currentHealth;
    private final int maxHealth;

    private final int movementSpeed;
    public Direction direction = UP;

    public Entity(int startX, int startY, int width, int height, int movementSpeed, int maxHealth) {
        this.left = startX + ((float)width/2);
        this.right = startX + ((float)width*3/2);
        this.top = startY + ((float)height/2);
        this.bottom = startY + ((float)height*3/2);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        this.movementSpeed = movementSpeed;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getSpriteInterval() {
        return spriteInterval;
    }

    public Point getMiddle(){
        int x = (int) (this.left + this.width());
        int y = (int) (this.top + this.height());
        return new Point(x, y);
    }

    public void heal(int amount){
        currentHealth += amount;
        if (currentHealth > maxHealth){
            currentHealth = maxHealth;
        }
    }

    public void damage(int amount){
        currentHealth -= amount;
        if (currentHealth < 0){
            currentHealth = 0;
        }
        Log.i("Entity", "Health: " + currentHealth);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void resetSpriteCounter(){
        spriteCounter = 0;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public void updateSpriteCounter(){
        this.spriteCounter++;

        if (this instanceof Player){
            // Demons need 24 animation states, players only 2
            this.spriteCounter++;
        }

        if (this.spriteCounter == spriteInterval){
            if (this.spriteNumber == 1){
                this.spriteNumber = 2;
            } else {
                this.spriteNumber = 1;
            }
            this.spriteCounter = 0;
        }
    }

    public abstract void update();

    public void updatePosition(){
        switch (direction){
            case LEFT:
                this.left -= this.getMovementSpeed();
                this.right -= this.getMovementSpeed();
                break;
            case RIGHT:
                this.left += this.getMovementSpeed();
                this.right += this.getMovementSpeed();
                break;
            case UP:
                this.top -= this.getMovementSpeed();
                this.bottom -= this.getMovementSpeed();
                break;
            case DOWN:
                this.top += this.getMovementSpeed();
                this.bottom += this.getMovementSpeed();
                break;
        }
    }
}
