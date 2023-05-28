package entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.appng.projectaura.R;

import java.util.ArrayList;
import java.util.Random;

import view.GameView;

public class Demon extends Monster {

    private Bitmap currentImage, sourceImage;
    private final GameView gameView;

    public final static int IDLE = 0;
    public final static int ATTACKING = 1;

    private int status = IDLE;

    // Timing
    private final long timeBetweenMovesMilliSeconds = 2000;
    private long lastMoveTime = 0;

    public Demon(GameView gameView, String name, int maxHealth, int startX, int startY, int width, int height, int movementSpeed) {
        super(name, maxHealth, startX, startY, width, height, movementSpeed);
        this.gameView = gameView;

        this.sourceImage = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.demon);
        // Making sure the dimensions are correct
        sourceImage = Bitmap.createScaledBitmap(sourceImage, 6144, 4352, true);

        getImage();
        gameView.addNonPlayerCharacter(this);
    }

    private void getImage() {
        // Log.d("Demon", "Attempting to get image");

        int baseImageSize = 256;

        int row = 0;

        // East Idle - Row 9
        // North Idle - Row 10
        // West idle - Row 16
        // South idle - Row 13
        if (status == IDLE) {
            switch (this.direction) {
                case LEFT:
                    row = 16;
                    break;
                case RIGHT:
                    row = 9;
                    break;
                case UP:
                    row = 10;
                    break;

                case DOWN:
                    row = 13;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.direction);
            }
        }
        // East Attack - Row 0
        // North Attack - Row 2
        // West Attack - Row 4
        // South Attack - Row 5
        else if (status == ATTACKING) {
            switch (this.direction) {
                case LEFT:
                    row = 4;
                    break;
                case RIGHT:
                    row = 0;
                    break;
                case UP:
                    row = 2;
                    break;

                case DOWN:
                    row = 5;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + this.direction);
            }
        }

        int imageX = this.getSpriteCounter() * baseImageSize;
        // Log.d("Demon", "Source: " + sourceImage.getWidth() + ", " + sourceImage.getHeight());

        if (status == IDLE) {
            imageX /= 2;
        }

        int imageY = row * baseImageSize;

        currentImage = Bitmap.createBitmap(sourceImage, imageX, imageY, baseImageSize, baseImageSize);
        currentImage = Bitmap.createScaledBitmap(currentImage, gameView.TILE_SIZE * 2, gameView.TILE_SIZE * 2, true);
        // Log.d("Demon", "Image acquired");
    }

    @Override
    public void attack() {
        status = ATTACKING;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        getImage();

        Point drawCoordinates = gameView.getPlayer().getPositionRelativeToPlayer((int) this.left, (int) this.top);
        // Log.d("Demon", "X: " + drawCoordinates.x + " Y: " + drawCoordinates.y);
        // Maybe needs to optimise if outside of screen, shouldn't be needed
        canvas.drawBitmap(currentImage, drawCoordinates.x, drawCoordinates.y, paint);
    }

    @Override
    public void update() {
        this.updateSpriteCounter();

        long currentTime = System.currentTimeMillis();

        Log.d("Demon", "" + (currentTime-lastMoveTime));

        if (currentTime - lastMoveTime > timeBetweenMovesMilliSeconds){
            generateNextMove();
            lastMoveTime = currentTime;
        }



        this.updatePosition();
        }

    private void generateNextMove() {
        Point drawCoordinates = gameView.getPlayer().getPositionRelativeToPlayer((int) this.left, (int) this.top);

        // TODO: Implement logic to move towards player
        // Just an estimation, calculating distance between rectangles is a bit complicated
        double playerDistance = Math.sqrt(Math.pow(drawCoordinates.x - this.left, 2) + Math.pow(drawCoordinates.y - this.top, 2));

        // TODO: Implement enemy moving

        Random rnd = new Random();
        int directionNumber = rnd.nextInt(4);

        switch (directionNumber) {
            case 0:
                this.direction = Direction.LEFT;
                break;
            case 1:
                this.direction = Direction.RIGHT;
                break;
            case 2:
                this.direction = Direction.UP;
                break;
            case 3:
                this.direction = Direction.DOWN;
                break;
        }


    }
}
