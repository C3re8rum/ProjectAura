package entity;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Monster extends Entity{

    private final String name;

    public Monster(String name, int maxHealth, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height, movementSpeed, maxHealth);

        this.name = name;
    }

    public abstract void attack();

    public abstract void draw(Canvas canvas, Paint paint);

}
