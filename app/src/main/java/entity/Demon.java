package entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.appng.projectaura.R;

import java.util.ArrayList;
import java.util.Random;

import view.GameView;

public class Demon extends Monster {

    private static boolean firstInstance = true;
    private static Bitmap sourceImage;

    private Bitmap currentImage;
    private final GameView gameView;
    private RectF range;

    public final static int IDLE = 0;
    public final static int ATTACKING = 1;
    public final static int DYING = 2;

    private int status = IDLE;

    private boolean moving = true;
    private boolean updatedSpriteCounterLastTime = false;

    // Timing
    private final long timeBetweenMovesMilliSeconds = 2000;
    private long lastMoveTime = 0;
    private final Thread updateThread;

    // Combat
    private final int damageConstant = 10;
    private final int level;



    public Demon(GameView gameView, String name, int maxHealth, int startX, int startY, int width, int height, int movementSpeed, int level) {
        super(name, maxHealth, startX, startY, width, height, movementSpeed);
        this.gameView = gameView;

        this.level = level;

        if (firstInstance){
            sourceImage = BitmapFactory.decodeResource(this.gameView.getContext().getResources(), R.drawable.demon_animation);
            // Making sure the dimensions are correct
            sourceImage = Bitmap.createScaledBitmap(sourceImage, 6144, 4352, true);
            firstInstance = false;
        }
        this.updateThread = new Thread(this);

        getImage();
        gameView.addNonPlayerCharacter(this);
        this.updateThread.start();
    }



    private void getImage() {
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
                    row = 1;
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
        } else if (status == DYING){
            resetSpriteCounter();
            row = 0;
        }

        if (status == IDLE && this.getSpriteCounter() >= 11){
            this.resetSpriteCounter();
        }

        int imageX = this.getSpriteCounter() * baseImageSize;

        int imageY = row * baseImageSize;

        currentImage = Bitmap.createBitmap(sourceImage, imageX, imageY, baseImageSize, baseImageSize);
        currentImage = Bitmap.createScaledBitmap(currentImage, gameView.TILE_SIZE * 2, gameView.TILE_SIZE * 2, true);
        // Log.d("Demon", "Image acquired");
    }

    @Override
    public void attack() {
        if (status == IDLE){
            this.resetSpriteCounter();
        }
        status = ATTACKING;
        moving = false;

        gameView.getPlayer().damage(this.damageConstant*level);

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        getImage();

        Point drawCoordinates = gameView.getPlayer().getPositionRelativeToPlayer((int) this.left, (int) this.top);
        // Log.d("Demon", "X: " + drawCoordinates.x + " Y: " + drawCoordinates.y);
        // Maybe needs to optimise if outside of screen, shouldn't be needed

        // Log.d("Demon", "DrawCords: " + drawCoordinates.toString());

        canvas.drawBitmap(currentImage, drawCoordinates.x, drawCoordinates.y, paint);

        // Health bar
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        RectF healthBar = new RectF(drawCoordinates.x, drawCoordinates.y, drawCoordinates.x + this.width(), drawCoordinates.y + this.height()/5);
        healthBar.top += (float) gameView.TILE_SIZE/4;
        healthBar.bottom += (float)gameView.TILE_SIZE/4;
        healthBar.left += (float)gameView.TILE_SIZE/2;
        healthBar.right += (float)gameView.TILE_SIZE/2;

        canvas.drawRect(healthBar, paint);

        double widthPercentage = (double)getCurrentHealth()/ (double) getMaxHealth();

        healthBar.right -= (healthBar.width() - healthBar.width()*widthPercentage);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawRect(healthBar, paint);

        // Line from monster to player
        // DEBUG: Makes it easier to find the monsters for demoing purposes
        paint.setColor(Color.BLACK);
        canvas.drawLine(drawCoordinates.x + this.width(), drawCoordinates.y + this.height(), gameView.getPlayer().getScreenX() + gameView.TILE_SIZE/2, gameView.getPlayer().getScreenY() + gameView.TILE_SIZE/2, paint);

        // Non walking zone
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        playerWithinRange();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        // DEBUG: Drawing hitbox
        //RectF hitBox = new RectF(drawCoordinates.x + (this.width()/2), drawCoordinates.y + (this.height()/2), drawCoordinates.x + this.width()*3/2, drawCoordinates.y + this.height()*3/2);
        // canvas.drawRect(hitBox, paint);
    }

    @Override
    public void update() {
        this.updateSpriteCounter();

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastMoveTime > timeBetweenMovesMilliSeconds){
            generateNextMove();
            lastMoveTime = currentTime;
        }

        if (moving){
            this.updatePosition();
        }
    }

    private void generateNextMove() {
        Player p = gameView.getPlayer();

        // TODO: Implement logic to move towards player
        Point demonCords = this.getMiddle();
        Point playerCords = p.getMiddle();

        int distanceX = demonCords.x - playerCords.x;
        int distanceY = demonCords.y - playerCords.y;

        double playerDistance = Math.sqrt(Math.pow(distanceX,2 ) + Math.pow(distanceY,2));
        // Log.d("Demon", "Distance to player: " + playerDistance);
        if (playerDistance > 800){
            generateRandomMove();
            return;
        }

        double playerAngle = gameView.getPlayer().angleToPlayer(this);

        if (playerAngle >= 315 || playerAngle <= 45){
            direction = Direction.RIGHT;
        } else if (playerAngle >= 45 && playerAngle <= 135){
            direction = Direction.UP;
        } else if (playerAngle >= 135 && playerAngle <= 225){
            direction = Direction.LEFT;
        } else if (playerAngle >= 225){
            direction = Direction.DOWN;
        }

        // Log.d("Demon", "Angle: " + playerAngle + " Direction: " + direction);

        if (playerWithinRange()){
            attack();
        } else {
            moving = true;
            status = IDLE;
        }

    }

    private boolean playerWithinRange() {

        Point playerPoint = gameView.getPlayer().getMiddle();
        Point demonPoint = this.getMiddle();

        int deltaX = playerPoint.x - demonPoint.x;
        int deltaY = playerPoint.y - demonPoint.y;
        // Log.d("DemonDistance", deltaX + " " + deltaY);

        int range = gameView.TILE_SIZE*2;

        boolean inRangeLeft = deltaX > -range;
        boolean inRangeRight = deltaX < range;
        boolean inRangeTop = deltaY > -range;
        boolean inRangeBottom = deltaY < range;

        return inRangeLeft && inRangeRight && inRangeTop && inRangeBottom;

    }

    private void generateRandomMove() {
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

    @Override
    public void run() {
        while (!Thread.interrupted()){
            int FPS = gameView.getCurrentFrameRate();

            update();

            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
