package ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import view.GameView;

public class MovementController {

    // Touch functionality
    private final RectF up, down, left, right;

    private boolean rightPressed = false;
    private boolean leftPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;

    // Visuals
    private final RectF horizontalCross, leftVerticalCross, rightVerticalCross;

    private final GameView gameView;

    public MovementController(GameView gameView) {
        this.gameView = gameView;
        down = new RectF((float) (gameView.TILE_SIZE * 1.5), (float) (gameView.TILE_SIZE * 6.25), (float) (gameView.TILE_SIZE * 2.25), (float) (gameView.TILE_SIZE * 7.25));
        up = new RectF((float) (gameView.TILE_SIZE * 1.5), (float) (gameView.TILE_SIZE * 4.5), (float) (gameView.TILE_SIZE * 2.25), (float) (gameView.TILE_SIZE * 5.5));
        left = new RectF((float) (gameView.TILE_SIZE * 0.5), (float) (gameView.TILE_SIZE * 5.5), (float) (gameView.TILE_SIZE * 1.5), (float) (gameView.TILE_SIZE * 6.25));
        right = new RectF((float) (gameView.TILE_SIZE * 2.25), (float) (gameView.TILE_SIZE * 5.5), (float) (gameView.TILE_SIZE * 3.25), (float) (gameView.TILE_SIZE * 6.25));

        horizontalCross = new RectF((float) (gameView.TILE_SIZE * 0.5), (float) (gameView.TILE_SIZE * 5.5), (float) (gameView.TILE_SIZE * 3.25), (float) (gameView.TILE_SIZE * 6.25));
        leftVerticalCross = new RectF((float) (gameView.TILE_SIZE * 1.5), (float) (gameView.TILE_SIZE * 4.5), (float) (gameView.TILE_SIZE * 2.25), (float) (gameView.TILE_SIZE * 5.5));
        rightVerticalCross = new RectF((float) (gameView.TILE_SIZE * 1.5), (float) (gameView.TILE_SIZE * 6.25), (float) (gameView.TILE_SIZE * 2.25), (float) (gameView.TILE_SIZE * 7.25));

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

    public void resetDirections() {
        this.leftPressed = false;
        this.rightPressed = false;
        this.downPressed = false;
        this.upPressed = false;
    }

    public void draw(Canvas canvas, Paint paint) {

/*
        paint.setColor(Color.BLUE);
        canvas.drawRect(this.left, paint);
        canvas.drawRect(this.right, paint);
        canvas.drawRect(this.up, paint);
        canvas.drawRect(this.down, paint);
*/
        int color = Color.argb(100, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRect(this.horizontalCross, paint);
        canvas.drawRect(this.leftVerticalCross, paint);
        canvas.drawRect(this.rightVerticalCross, paint);

        // Resets the alpha of the canvas, otherwise entire map goes dark-ish
        paint.setColor(Color.BLACK);
    }

    public void checkCollision(int touchX, int touchY, boolean touchDown) {
        RectF touchPoint = new RectF(touchX, touchY, touchX + 1, touchY + 1);

        // Log.i("MovementController", "Checking movement");
        if (this.left.contains(touchPoint)) {
            this.leftPressed = touchDown;

            Log.i("MovementController", "Left");
        } else if (this.right.contains(touchPoint)) {
            this.rightPressed = touchDown;

            Log.i("MovementController", "Right");
        } else if (this.up.contains(touchPoint)) {
            this.upPressed = touchDown;
            Log.i("MovementController", "Up");

        } else if (this.down.contains(touchPoint)) {
            this.downPressed = touchDown;
            Log.i("MovementController", "Down");

        }

        if (!touchDown) {
            resetDirections();
        }
    }

}

