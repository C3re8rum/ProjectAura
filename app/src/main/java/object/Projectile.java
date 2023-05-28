package object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import view.GameView;

public class Projectile extends RectF implements Runnable {

    private int worldX, worldY, projectileSpeed, angle, damage;
    private GameView gameView;
    private Item source;

    private Thread positionThread;

    public Projectile(GameView gameView, Item source, int worldX, int worldY, int projectileSpeed, int angle, int damage) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.projectileSpeed = projectileSpeed;
        this.angle = angle;
        this.damage = damage;
        this.gameView = gameView;
        this.source = source;

        this.left = worldX;
        this.top = worldY;
        this.right = worldX + gameView.TILE_SIZE/2;
        this.bottom = worldY + gameView.TILE_SIZE/2;

        this.positionThread = new Thread(this);
        this.positionThread.start();
    }

    public void updateProjectile() {
        worldX -= projectileSpeed * Math.cos((double) angle*(Math.PI/180));
        worldY -= projectileSpeed * Math.sin((double) angle*(Math.PI/180));

        this.left = worldX;
        this.top = worldY;
        this.right = worldX + gameView.TILE_SIZE/2;
        this.bottom = worldY + gameView.TILE_SIZE/2;

        Log.i("Projectile", "X: " + this.worldX + " Y: " + this.worldY);

    }
    public void draw(Canvas canvas, Paint paint){

        Point point = gameView.getPlayer().getPositionRelativeToPlayer(worldX, worldY);

        canvas.drawBitmap(source.getImage(), point.x, point.y, paint);

    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            updateProjectile();

            try {
                Thread.sleep((long)(1000/gameView.getCurrentFrameRate()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDamage(){
        return this.damage;
    }
}
