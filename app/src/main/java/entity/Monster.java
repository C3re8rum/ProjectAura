package entity;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Monster extends Entity{
    private int currentHealth, maxHealth;
    private String name;

    public Monster(String name, int maxHealth, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height, movementSpeed);
        this.maxHealth = maxHealth;
        this.name = name;
    }

    public void heal(int amount){
        this.currentHealth += amount;
    }

    public void damage(int amount){
        this.currentHealth -= amount;
    }

    public abstract void attack();

    public abstract void draw(Canvas canvas, Paint paint);

}
