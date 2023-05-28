package object;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.appng.projectaura.R;

import exceptions.SpellLevelException;
import view.GameView;

public class Firebolt extends Spell{

    // Magic constants
    private int projectileSpeed;
    private static final int DAMAGE_CONSTANT = 20;

    private Bitmap image;

    private GameView gameView;

    // TODO: Make it so you can shoot multiple fireballs
    // Maybe make general placeholder projectile that takes in speed, image and damage?
    public Firebolt(GameView gameView, String name, int projectileSpeed, int level) throws SpellLevelException {
        super(name, level);
        this.projectileSpeed = projectileSpeed;
        this.gameView = gameView;

        this.image = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.firebolt);
        this.image = Bitmap.createScaledBitmap(image, gameView.TILE_SIZE, gameView.TILE_SIZE, true);

    }

    @Override
    public int getDamage(){
        return Firebolt.DAMAGE_CONSTANT*this.getSpellLevel();
    }

    public Bitmap getImage() {
        return image;
    }


    public int getProjectileSpeed() {
        return this.projectileSpeed;
    }

    // TODO: Implement way for player to use abilities
    @Override
    public void activate() {
        int angle = 0;

        switch (gameView.getPlayer().direction){
            case UP:
                angle = 90;
                break;
            case DOWN:
                angle = 270;
                break;
            case LEFT:
                angle = 0;
                break;
            case RIGHT:
                angle = 180;
                break;

        }

        Log.d("Firebolt", "Activated");

        Projectile projectile = new Projectile(gameView, this, (int) gameView.getPlayer().left-gameView.TILE_SIZE/2, (int) gameView.getPlayer().top+gameView.TILE_SIZE/2, this.projectileSpeed, angle, getDamage());
        gameView.addProjectile(projectile);

    }

    @Override
    public String toString() {
        return "Firebolt";
    }
}
