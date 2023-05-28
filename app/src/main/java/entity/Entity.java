package entity;

import static entity.Direction.UP;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Entity extends RectF {

    private int spriteCounter = 0;
    private int spriteNumber = 1;
    private final int spriteInterval = 12;

    private int movementSpeed;
    public Direction direction = UP;

    public Entity(int startX, int startY, int width, int height, int movementSpeed) {
        this.left = startX - width/2;
        this.right = startX + width/2;
        this.top = startY + height/2;
        this.bottom = startY - height/2;

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

        if (this.spriteCounter == spriteInterval){
            if (this.spriteNumber == 1){
                this.spriteNumber = 2;
            } else {
                this.spriteNumber = 1;
            }
            this.spriteCounter = 0;
        }
    }
}
