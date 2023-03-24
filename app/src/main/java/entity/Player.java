package entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.appng.projectaura.R;

public class Player extends Entity{

    private Bitmap playerImage;
    private int movementSpeed;

    public Player(Context context, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height);

        this.movementSpeed = movementSpeed;
        this.playerImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.temp_image);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        // canvas.drawRect(this, paint);
        canvas.drawBitmap(playerImage, this.left, this.top, paint);
    }

}
