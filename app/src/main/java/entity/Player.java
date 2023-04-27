package entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.appng.projectaura.MainActivity;
import com.appng.projectaura.R;

import view.GameView;

public class Player extends Entity{

    private Bitmap imageUp1, imageUp2;
    private int movementSpeed;

    private GameView gameView;

    private int screenX = 0;
    private int screenY = 0;

    public Player(GameView gameView, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height);

        this.movementSpeed = movementSpeed;
        this.gameView = gameView;

        this.screenX = MainActivity.getScreenWidth()/2;
        this.screenY = MainActivity.getScreenHeight()/2;

        initializeImages();
    }

    private void initializeImages() {
        this.imageUp1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyup1);
        this.imageUp1 = Bitmap.createScaledBitmap(imageUp1, gameView.TILE_SIZE, gameView.TILE_SIZE, true);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        // canvas.drawRect(this, paint);
        canvas.drawBitmap(imageUp1, screenX, screenY, paint);
    }

}
