package object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import view.GameView;

public class Projectile extends RectF implements Runnable {

    private int worldX, worldY, damage;
    private final int projectileSpeed, angle;
    private final GameView gameView;
    private final Item source;

    private Thread positionThread;
    private boolean killThread = false;

    public Projectile(GameView gameView, Item source, int worldX, int worldY, int projectileSpeed, int angle, int damage) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.projectileSpeed = projectileSpeed;
        this.angle = angle;
        this.damage = damage;
        this.gameView = gameView;
        this.source = source;

        RectF temp = gameView.getPlayer();
        this.left = temp.left;
        this.top = temp.top;
        this.right = temp.right;
        this.bottom = temp.bottom;

        this.positionThread = new Thread(this);
        this.positionThread.start();
    }

    public void updateProjectile() {
        worldX -= projectileSpeed * Math.cos((double) angle*(Math.PI/180));
        worldY -= projectileSpeed * Math.sin((double) angle*(Math.PI/180));

        this.left = worldX;
        this.top = worldY;
        this.right = worldX + gameView.TILE_SIZE;
        this.bottom = worldY + gameView.TILE_SIZE;

        // Log.i("Projectile", "X: " + this.worldX + " Y: " + this.worldY);

    }
    public void draw(Canvas canvas, Paint paint){

        Point point = gameView.getPlayer().getPositionRelativeToPlayer(worldX, worldY);

        canvas.drawBitmap(source.getImage(), point.x, point.y, paint);

        /*
        RectF hitbox = (RectF)this;
        hitbox.left = point.x;
        hitbox.top = point.y;
        hitbox.right = point.x + gameView.TILE_SIZE;
        hitbox.bottom = point.y + gameView.TILE_SIZE;

        paint.setColor(Color.RED);
        canvas.drawRect(hitbox, paint);
        */
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            updateProjectile();

            try {
                Thread.sleep((1000/gameView.getCurrentFrameRate()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void interruptThread(){
        positionThread.interrupt();
    }

    public int getDamage(){
        return this.damage;
    }

    public void projectileUsed(){
        this.damage = 0;
    }

}
