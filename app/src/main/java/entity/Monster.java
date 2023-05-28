package entity;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Monster extends Entity{
    private int currentHealth;
    private final int maxHealth;
    private final String name;

    public Monster(String name, int maxHealth, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height, movementSpeed);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth/2;
        this.name = name;
    }

    public void heal(int amount){
        this.currentHealth += amount;
    }

    public void damage(int amount){
        this.currentHealth -= amount;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public abstract void attack();

    public abstract void draw(Canvas canvas, Paint paint);

}
