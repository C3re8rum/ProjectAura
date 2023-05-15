package entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.appng.projectaura.MainActivity;
import com.appng.projectaura.R;

import controller.MovementController;
import view.GameView;

public class Player extends Entity{

    private Bitmap imageUp1, imageUp2, imageLeft1, imageLeft2, imageRight1, imageRight2, imageDown1, imageDown2;
    private int movementSpeed;

    private GameView gameView;
    private MovementController movementController;

    private int screenX = 0;
    private int screenY = 0;

    private int worldX, worldY;

    public Direction direction = Direction.UP;

    public Player(GameView gameView, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height);

        this.movementSpeed = movementSpeed;
        this.gameView = gameView;
        this.movementController = gameView.getMovementController();
        this.screenX = MainActivity.getScreenWidth()/2;
        this.screenY = MainActivity.getScreenHeight()/2;

        initializeImages();

        this.worldX = gameView.TILE_SIZE*32;
        this.worldY = gameView.TILE_SIZE*32;

    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    private void initializeImages() {
        this.imageUp1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyup1);
        this.imageUp1 = Bitmap.createScaledBitmap(imageUp1, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageUp1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyup2);
        this.imageUp2 = Bitmap.createScaledBitmap(imageUp2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageUp1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyup2);
        this.imageUp2 = Bitmap.createScaledBitmap(imageUp2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

    }

    public void update(){
        if (movementController == null) {
            return;
        }

        boolean left = movementController.isLeftPressed();
        boolean right = movementController.isRightPressed();
        boolean up = movementController.isUpPressed();
        boolean down = movementController.isDownPressed();

        // Log.d("Player", "Updating");

        if ( left || right || up || down ) {

                if (movementController.isLeftPressed()){
                    this.worldX += -movementSpeed;
                }

                // Log.i("Player", "Moving");

        }


    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        // canvas.drawRect(this, paint);
        canvas.drawBitmap(imageUp1, screenX, screenY, paint);
    }

}
