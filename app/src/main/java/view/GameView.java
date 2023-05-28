package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import controller.AbilityController;
import controller.MovementController;
import entity.Demon;
import entity.Entity;
import entity.Player;
import object.Projectile;
import world.TileManager;

public class GameView extends SurfaceView implements Runnable{

    // Graphics components
    private final Thread paintThread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private final Paint paint;

    // User interface
    private final MovementController movementController;
    private final AbilityController abilityController;

    // Tile constants
    public final int BASE_TILE_SIZE = 16;
    private final int scaleFactor = 8;
    public final int TILE_SIZE = BASE_TILE_SIZE*scaleFactor;

    public final int WORLD_GRID_HEIGHT = 64;
    public final int WORLD_GRID_WIDTH = 64;

    // World
    private final TileManager tileManager;
    private final Player player;
    private final ArrayList<Projectile> activeProjectiles;
    private final ArrayList<Entity> nonPlayerCharacters;


    // Timing
    public static final int FRAME_RATE = 60;
    private int currentFrameRate = FRAME_RATE;

    public GameView(Context context) {
        super(context);
        this.activeProjectiles = new ArrayList<>();
        this.nonPlayerCharacters = new ArrayList<>();

        this.movementController = new MovementController(this);

        this.player = new Player(this,TILE_SIZE*32, TILE_SIZE*32, TILE_SIZE, TILE_SIZE, 15);
        
        surfaceHolder = getHolder();
        paint = new Paint();
        tileManager = new TileManager(this);
        
        initializeMonsters();

        this.abilityController = new AbilityController(this);

        paintThread = new Thread(this);
        paintThread.start();
        
    }

    private void initializeMonsters() {
        Demon demon = new Demon(this, "Demon", 250, TILE_SIZE*32, TILE_SIZE*36, TILE_SIZE, TILE_SIZE, 2);
        addNonPlayerCharacter(demon);
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / (double) FRAME_RATE; // 0.0166 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (!paintThread.isInterrupted()){
            currentTime = System.nanoTime();
            timer += (currentTime - lastTime);
            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                update();

                delta--;
                drawCount++;

            }
            // If timer >= 1 second
            if (timer >= 1_000_000_000){
                Log.i("FPS", String.valueOf(drawCount));
                this.currentFrameRate = drawCount;
                drawCount = 0;
                timer = 0;
            }

        }
    }

    private void update() {
        player.update();
        draw();

        // Optimization so projectiles outside of the world map are deleted
        removeOutOfBoundsProjectiles();

        // TODO: Add logic for projectiles
        for (Entity entity: nonPlayerCharacters) {
            entity.update();
        }


    }

    private void removeOutOfBoundsProjectiles() {
        activeProjectiles.removeIf(projectile -> projectile.right < 0 || projectile.left > TILE_SIZE*WORLD_GRID_WIDTH
        || projectile.bottom < 0 || projectile.top > TILE_SIZE*WORLD_GRID_HEIGHT);

    }

    public void addProjectile(Projectile projectile){
        this.activeProjectiles.add(projectile);
    }

    public void addNonPlayerCharacter(Entity entity){
        nonPlayerCharacters.add(entity);
    }

    public int getCurrentFrameRate() {
        return currentFrameRate;
    }

    public MovementController getMovementController() {
        return movementController;
    }

    public Player getPlayer() {
        return player;
    }

    private void draw() {
        try {
            canvas = surfaceHolder.lockCanvas();
        } catch(Exception e){
            Log.e("Error", e.getMessage());
        }
        this.surfaceHolder = getHolder();

        if (canvas == null){
            return;
        }

        this.tileManager.draw(canvas, paint);

        for (Projectile projectile: this.activeProjectiles) {
            projectile.draw(canvas, paint);
        }
        for (Entity entity: nonPlayerCharacters) {
            entity.draw(canvas, paint);
        }

        this.player.draw(canvas, paint);

        this.movementController.draw(canvas, paint);
        this.abilityController.draw(canvas, paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        boolean touchDown = true;

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            touchDown = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            touchDown = false;
        }
        movementController.checkCollision(touchX, touchY, touchDown);
        abilityController.checkCollision(touchX, touchY, touchDown);

        return true;
    }


}
