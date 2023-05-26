package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import controller.MovementController;
import entity.Player;
import world.TileManager;

public class GameView extends SurfaceView implements Runnable{

    // Graphics components
    private Thread paintThread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;

    private MovementController movementController;
    private Player player;

    // Tile constants
    public final int BASE_TILE_SIZE = 16;
    private int scaleFactor = 8;
    public final int TILE_SIZE = BASE_TILE_SIZE*scaleFactor;

    public final int WORLD_GRID_HEIGHT = 64;
    public final int WORLD_GRID_WIDTH = 64;

    // World
    private TileManager tileManager;

    public GameView(Context context) {
        super(context);
        this.movementController = new MovementController(this);
        this.player = new Player(this,250, 250, TILE_SIZE, TILE_SIZE, 6);

        surfaceHolder = getHolder();
        paint = new Paint();

        tileManager = new TileManager(this);

        paintThread = new Thread(this);
        paintThread.start();


    }



    @Override
    public void run() { 
        while (!paintThread.isInterrupted()){

            player.update();
            draw();
        }
    }

    public MovementController getMovementController() {
        return movementController;
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

        this.player.draw(canvas, paint);

        this.movementController.draw(canvas, paint);

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

        return true;
    }

    public Player getPlayer() {
        return player;
    }
}
