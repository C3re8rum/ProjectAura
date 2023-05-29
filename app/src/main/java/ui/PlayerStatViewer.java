package ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import view.GameView;

public class PlayerStatViewer {

    private GameView gameView;
    private final RectF healthBar;

    public PlayerStatViewer(GameView gameView) {
        this.gameView = gameView;
        healthBar = new RectF(gameView.TILE_SIZE/2, gameView.TILE_SIZE/2, gameView.TILE_SIZE*6, gameView.TILE_SIZE);
    }

    public void draw(Canvas canvas, Paint paint){
        // Draw health bar
        paint.setColor(Color.argb(100, 0,0,0));
        canvas.drawRect(healthBar, paint);

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(healthBar, paint);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        double healthPercentage = (double)gameView.getPlayer().getCurrentHealth()/(double)gameView.getPlayer().getMaxHealth();
        int newWidth = (int) (healthPercentage * healthBar.width());
        canvas.drawRect(healthBar.left, healthBar.top, healthBar.left + newWidth, healthBar.bottom, paint);

        // Draw kills
    }

}
