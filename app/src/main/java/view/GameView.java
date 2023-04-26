package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import entity.Player;
import world.TileManager;

public class GameView extends SurfaceView implements Runnable{

    // Graphics components
    private Thread paintThread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;

    // Tile constants
    public final int BASE_TILE_SIZE = 16;
    private int scaleFactor = 4;
    public final int TILE_SIZE = BASE_TILE_SIZE*scaleFactor;

    public final int WORLD_GRID_HEIGHT = 64;
    public final int WORLD_GRID_WIDTH = 64;

    // World
    private TileManager tileManager;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

        tileManager = new TileManager(this);

        paintThread = new Thread(this);
        paintThread.start();
    }

    @Override
    public void run() { 
        while (!paintThread.isInterrupted()){
            draw();
        }
    }

    private void draw() {
        canvas = surfaceHolder.lockCanvas();
        this.surfaceHolder = getHolder();

        if (canvas == null){
            return;
        }

        // Player p = new Player(getContext(),250, 250, 25, 25, 5);
        // p.draw(canvas, paint);

        this.tileManager.draw(canvas, paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
