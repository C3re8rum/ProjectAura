package entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.appng.projectaura.MainActivity;
import com.appng.projectaura.R;

import ui.MovementController;
import exceptions.SpellLevelException;
import object.Firebolt;
import object.Item;
import view.GameView;

public class Player extends Entity{

    private final GameView gameView;

    // Movement
    private final MovementController movementController;

    // Graphics
    private Bitmap imageUp1, imageUp2, imageLeft1, imageLeft2, imageRight1, imageRight2, imageDown1, imageDown2;
    private final int screenX;
    private final int screenY;

    // Combat
    private final Item[] abilities;
    private final static int globalCoolDownMilliSeconds = 1000;
    private long timeSinceLastUsedAbility = 0;


    public Player(GameView gameView, int maxHealth, int startX, int startY, int width, int height, int movementSpeed) {
        super(startX, startY, width, height, movementSpeed, maxHealth);

        this.gameView = gameView;
        this.movementController = gameView.getMovementController();
        this.screenX = MainActivity.getScreenWidth()/2;
        this.screenY = MainActivity.getScreenHeight()/2;

        this.abilities = new Item[4];

        initializeImages();

        try {
            this.addAbility(new Firebolt(gameView, "Firebolt", 5, 8));
        } catch (SpellLevelException e) {
            e.printStackTrace();
        }

    }

    public void addAbility(Item item){
        int index = 0;
        // Find the first empty spot in list
        while(this.abilities[index] != null && index < 4){
            index++;
        }
        // TODO: Implement behaviour for full ability-list;
        if (index == 4 ){
            return;
        }

        this.abilities[index] = item;

    }

    public void useAbility(int index){
        long currentTime = System.currentTimeMillis();

        if (currentTime - timeSinceLastUsedAbility < globalCoolDownMilliSeconds){
            return;
        }

        Item item = abilities[index];
        item.activate();
        Log.i("PlayerAbility", item.toString());
        timeSinceLastUsedAbility = currentTime;
    }

    public Item getAbility(int index){
        return abilities[index];
    }

    public int getWorldX() {
        return (int) (this.left - width()/2);
    }

    public int getWorldY() {
        return (int) (this.top - height()/2);
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

        this.imageUp2 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyup2);
        this.imageUp2 = Bitmap.createScaledBitmap(imageUp2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageDown1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boydown1);
        this.imageDown1 = Bitmap.createScaledBitmap(imageDown1, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageDown2 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boydown2);
        this.imageDown2 = Bitmap.createScaledBitmap(imageDown2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageLeft1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyleft1);
        this.imageLeft1 = Bitmap.createScaledBitmap(imageLeft1, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageLeft2 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyleft2);
        this.imageLeft2 = Bitmap.createScaledBitmap(imageLeft2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageRight1 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyright1);
        this.imageRight1 = Bitmap.createScaledBitmap(imageRight1, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

        this.imageRight2 = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.boyright2);
        this.imageRight2 = Bitmap.createScaledBitmap(imageRight2, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

    }

    @Override
    public void update(){
        if (movementController == null) {
            return;
        }

        if (movementController.isDownPressed() || movementController.isLeftPressed() || movementController.isRightPressed() || movementController.isUpPressed()){
            this.updateSpriteCounter();
            this.updateDirection();
            this.updatePosition();

        }


    }

    private void updateDirection() {
        boolean left = movementController.isLeftPressed();
        boolean right = movementController.isRightPressed();
        boolean up = movementController.isUpPressed();
        boolean down = movementController.isDownPressed();

        if (left){
            direction = Direction.LEFT;
        } else if (right){
            direction = Direction.RIGHT;
        } else if (up) {
            direction = Direction.UP;
        } else if (down){
            direction = Direction.DOWN;
        }

    }

    public Point getPositionRelativeToPlayer(int objectWorldX, int objectWorldY){

        int objectScreenX = objectWorldX - getWorldX() + screenX;
        int objectScreenY = objectWorldY - getWorldY() + screenY;
        return new Point(objectScreenX, objectScreenY);
    }

    public double angleToPlayer(RectF object) {
        Point objectDrawPoint = getPositionRelativeToPlayer((int) object.left, (int) object.top);

        float distanceX = (screenX + gameView.TILE_SIZE / 2) - (objectDrawPoint.x + this.width());
        // negative to revert the y axis for correct mathematical coordinate system
        float distanceY = -((screenY + gameView.TILE_SIZE / 2) - (objectDrawPoint.y - this.height()));

        double angleRadians = Math.atan(distanceY / distanceX);

        Log.d("DemonDistance", distanceX + " " + distanceY);
        double angleDegrees = (angleRadians * ((double) 180 / Math.PI));

        // Positive x and positive y => 0 < angle < 90
        // Negative x and positive y => 90 < angle < 180
        // Negative x and negative y => -180 < angle < -90
        // Positive x and negative y => -90 < angle < 0

        // Arctan can only result in results from -90->+90 degrees

        // 2nd quadrant
        if (distanceX < 0 && distanceY > 0) {
            angleDegrees += 180;
        }
        // 3rd quadrant
        if (distanceX < 0 && distanceY < 0) {
            angleDegrees += 180;
        }
        // 4th quadrant
        if (distanceX > 0 && distanceY < 0) {
            angleDegrees += 360;
        }


        return angleDegrees;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        // canvas.drawRect(this, paint);

        Bitmap playerImage = null;

        switch (direction){
            case LEFT:
                if (this.getSpriteNumber() == 1){
                    playerImage = this.imageLeft1;
                } else if(this.getSpriteNumber() == 2){
                    playerImage = this.imageLeft2;
                }
                break;
            case RIGHT:
                if (this.getSpriteNumber() == 1){
                    playerImage = this.imageRight1;
                } else if(this.getSpriteNumber() == 2){
                    playerImage = this.imageRight2;
                }
                break;
            case UP:
                if (this.getSpriteNumber() == 1){
                    playerImage = this.imageUp1;
                } else if(this.getSpriteNumber() == 2){
                    playerImage = this.imageUp2;
                }
                break;
            case DOWN:
                if (this.getSpriteNumber() == 1){
                    playerImage = this.imageDown1;
                } else if(this.getSpriteNumber() == 2){
                    playerImage = this.imageDown2;
                }
                break;
        }

        canvas.drawBitmap(playerImage, screenX, screenY, paint);
    }

    @Override
    public void run() {

    }
}
