package entity;

import static entity.Direction.UP;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public abstract class Entity extends RectF {

    private int spriteCounter = 0;
    private int spriteNumber = 1;
    private final int spriteInterval = 24;

    private int movementSpeed;
    public Direction direction = UP;

    public Entity(int startX, int startY, int width, int height, int movementSpeed) {
        this.left = startX - (float) (width/2);
        this.right = startX + (float)( width/2);
        this.top = startY + (float) (height/2);
        this.bottom = startY - (float)(height/2);

        this.movementSpeed = movementSpeed;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public abstract void draw(Canvas canvas, Paint paint);

    public int getSpriteCounter() {
        return spriteCounter;
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
