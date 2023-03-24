package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import entity.Player;

public class GameView extends SurfaceView implements Runnable{

    // Graphics components
    private Thread paintThread;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

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

        Player p = new Player(getContext(),250, 250, 25, 25, 5);
        p.draw(canvas, paint);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }
}
