package view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.appng.projectaura.YouDiedActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import ui.AbilityController;
import ui.MovementController;
import entity.Demon;
import entity.Entity;
import entity.Player;
import object.Projectile;
import ui.PlayerStatViewer;
import world.TileManager;

public class GameView extends SurfaceView implements Runnable {

    // Graphics components
    private final Thread paintThread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private final Paint paint;

    // User interface
    private final MovementController movementController;
    private final AbilityController abilityController;
    private final PlayerStatViewer playerStatViewer;

    // Tile constants
    public final int BASE_TILE_SIZE = 16;
    private final int scaleFactor = 8;
    public final int TILE_SIZE = BASE_TILE_SIZE * scaleFactor;

    public final int WORLD_GRID_HEIGHT = 64;
    public final int WORLD_GRID_WIDTH = 64;

    // World
    private final TileManager tileManager;
    private final Player player;
    private ArrayList<Projectile> activeProjectiles;
    private final ArrayList<Projectile> projectilesToRemove;
    private ArrayList<Entity> nonPlayerCharacters;

    // Options
    private int difficultyConstant;
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.appng.projectaura";
    // Timing
    public static final int FRAME_RATE = 60;
    private int currentFrameRate = FRAME_RATE;

    // Multi-touch
    private static final int INVALID_POINTER_ID = -1;
    private int activePointerId = INVALID_POINTER_ID;

    public GameView(Context context, String difficulty) {
        super(context);

        switch (difficulty) {
            case "NORMAL":
                difficultyConstant = 2;
                break;
            case "HARD":
                difficultyConstant = 3;
                break;
            default:
                difficultyConstant = 1;
        }

        this.activeProjectiles = new ArrayList<>();
        this.projectilesToRemove = new ArrayList<>();
        this.nonPlayerCharacters = new ArrayList<>();

        this.movementController = new MovementController(this);

        this.player = new Player(this, 400, TILE_SIZE * 32, TILE_SIZE * 32, TILE_SIZE, TILE_SIZE, 15);

        surfaceHolder = getHolder();
        paint = new Paint();
        tileManager = new TileManager(this);

        initializeMonsters();

        this.abilityController = new AbilityController(this);
        this.playerStatViewer = new PlayerStatViewer(this);

        paintThread = new Thread(this);
        paintThread.start();

    }

    private void initializeMonsters() {

        int numberOfDemons = 4 * difficultyConstant;
        Random rnd = new Random();
        for (int i = 0; i <= numberOfDemons; i++) {
            int gridX = rnd.nextInt(55) + 1;
            int gridY = rnd.nextInt(55) + 1;
            int movementSpeed = rnd.nextInt(3) + 1;
            int level = rnd.nextInt(3) + 1;

            Demon demon = new Demon(this, "Demon", 100 * difficultyConstant, TILE_SIZE * gridX, TILE_SIZE * gridY, TILE_SIZE, TILE_SIZE, movementSpeed * difficultyConstant, level * difficultyConstant);

            addNonPlayerCharacter(demon);
        }
        Log.i("GameView", "Monsters created");

    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / (double) FRAME_RATE; // 0.0166 seconds
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (!paintThread.isInterrupted()) {
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
            if (timer >= 1_000_000_000) {
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
        handleProjectileCollisions();
        checkForCharacterDeaths();

        if (player.getCurrentHealth() == 0){
            paintThread.interrupt();
            Thread.currentThread().interrupt();

            activeProjectiles = new ArrayList<>();
            nonPlayerCharacters = new ArrayList<>();

            Intent intent = new Intent(getContext(), YouDiedActivity.class);
            getContext().startActivity(intent);

        }

    }

    private void checkForCharacterDeaths() {
        synchronized (nonPlayerCharacters) {
            nonPlayerCharacters.removeIf(entity -> entity.getCurrentHealth() == 0);
        }

    }

    private void handleProjectileCollisions() {
        synchronized (nonPlayerCharacters) {
            for (Entity entity : nonPlayerCharacters) {
                synchronized (activeProjectiles) {
                    for (Iterator<Projectile> projectiles = activeProjectiles.iterator(); projectiles.hasNext(); ) {
                        Projectile projectile = projectiles.next();
                        String e = "(X: " + entity.left + "-" + entity.right + ", Y: " + entity.top + "-" + entity.bottom + ")";
                        // Log.d("Collision", "Entity: " + e);
                        String p = "(X: " + projectile.left + "-" + projectile.right + ", Y: " + projectile.top + "-" + projectile.bottom + ")";
                        // Log.d("Collision", "Projectile" + p);
                        if (RectF.intersects(projectile, entity)) {
                            Log.i("Collision", "Damage dealt: " + projectile.getDamage());

                            entity.damage(projectile.getDamage());
                            projectile.projectileUsed();
                            projectilesToRemove.add(projectile);
                        }
                    }
                }
            }
        }
        synchronized (activeProjectiles){
            for (Projectile p : projectilesToRemove) {
                activeProjectiles.remove(p);
            }
        }

    }

    public ArrayList<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    private void removeOutOfBoundsProjectiles() {
        synchronized (activeProjectiles) {

            activeProjectiles.removeIf(projectile -> projectile.right < 0 || projectile.left > TILE_SIZE * WORLD_GRID_WIDTH
                    || projectile.bottom < 0 || projectile.top > TILE_SIZE * WORLD_GRID_HEIGHT);

        }
    }

    public void addProjectile(Projectile projectile) {
        this.activeProjectiles.add(projectile);
    }

    public void addNonPlayerCharacter(Entity entity) {
        synchronized (nonPlayerCharacters){
            nonPlayerCharacters.add(entity);
        }

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
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        this.surfaceHolder = getHolder();

        if (canvas == null) {
            return;
        }

        this.tileManager.draw(canvas, paint);

        synchronized (activeProjectiles) {
            for (Projectile projectile : this.activeProjectiles) {
                projectile.draw(canvas, paint);
            }
        }
        synchronized (nonPlayerCharacters) {
            for (Entity entity : nonPlayerCharacters) {
                entity.draw(canvas, paint);
            }
        }

        this.player.draw(canvas, paint);

        this.movementController.draw(canvas, paint);
        this.abilityController.draw(canvas, paint);
        this.playerStatViewer.draw(canvas, paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }


    public boolean onTouchEvent(MotionEvent event) {
        int eventType = event.getActionMasked();
        boolean touchDown;

        int touchX, touchY;

        switch (eventType) {
            case MotionEvent.ACTION_DOWN:
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                touchDown = true;

                movementController.checkCollision(touchX, touchY, touchDown);
                abilityController.checkCollision(touchX, touchY, touchDown);

                break;
            case MotionEvent.ACTION_UP:
                touchDown = false;
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                movementController.checkCollision(touchX, touchY, touchDown);
                abilityController.checkCollision(touchX, touchY, touchDown);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                touchDown = true;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    touchX = (int) event.getX(i);
                    touchY = (int) event.getY(i);
                    movementController.checkCollision(touchX, touchY, touchDown);
                    abilityController.checkCollision(touchX, touchY, touchDown);

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                touchDown = false;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    touchX = (int) event.getX(i);
                    touchY = (int) event.getY(i);
                    movementController.checkCollision(touchX, touchY, touchDown);
                    abilityController.checkCollision(touchX, touchY, touchDown);
                }
                break;
        }


        return true;
    }


}
