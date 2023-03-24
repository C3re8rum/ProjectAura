package entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public abstract class Entity extends RectF {
    public Entity(int startX, int startY, int width, int height) {
        this.left = startX - width/2;
        this.top = startY + height/2;
    }

    public abstract void draw(Canvas canvas, Paint paint);


}
