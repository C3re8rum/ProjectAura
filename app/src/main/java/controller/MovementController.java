package controller;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import view.GameView;

public class MovementController {

    private final RectF up, down, left, right;

    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    RectF temp;
    private GameView gameView;
    public MovementController(GameView gameView) {
        this.gameView = gameView;
        up = new RectF();
        down = new RectF();
        left = new RectF(gameView.TILE_SIZE*2, (float) (gameView.TILE_SIZE*5), (float)(gameView.TILE_SIZE*3), (float)(gameView.TILE_SIZE*6));
        right = new RectF();
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void resetDirections(){
        this.leftPressed = false;
        this.rightPressed = false;
        this.downPressed = false;
        this.upPressed = false;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.BLACK);

        // Log.i("Controller", "" + this.left.left + " " + this.left.top);

        temp = new RectF(gameView.TILE_SIZE*2, (float) (gameView.TILE_SIZE*5), (float)(gameView.TILE_SIZE*3), (float)(gameView.TILE_SIZE*6));
        canvas.drawRect(temp, paint);
    }

    public void checkCollision(int touchX, int touchY, boolean touchDown){
        RectF touchPoint = new RectF(touchX, touchY, touchX+1, touchY+1);
        if (this.temp.intersect(touchPoint)){

            if (touchDown) {
                this.leftPressed = true;
            } else {
                this.leftPressed = false;
            }

            Log.d("CONTROLLER", "LEFT PRESSED");
        }
    }

}

