package controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.appng.projectaura.MainActivity;

import entity.Player;
import view.GameView;

public class AbilityController {

    // To make sure the ability is only triggered once per press
    private boolean usedAbility = false;
    private GameView gameView;
    private Player player;

    private RectF mainAbility;

    public AbilityController(GameView gameView) {
        this.gameView = gameView;
        this.player = gameView.getPlayer();

        int bigRadius = gameView.TILE_SIZE;
        int drawX = MainActivity.getScreenWidth() - gameView.TILE_SIZE*3;
        int drawY = MainActivity.getScreenHeight() - gameView.TILE_SIZE*4;


        this.mainAbility = new RectF(drawX, drawY, drawX + bigRadius*2, drawY + bigRadius*2);

        Log.d("AbilityController", this.mainAbility.toString());
    }

    public void draw(Canvas canvas, Paint paint){

        paint.setColor(Color.argb(100, 0, 0, 0));

        canvas.drawOval(mainAbility, paint);

        // Resets the alpha-value of paint
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(player.getAbility(0).getImage(), mainAbility.left + mainAbility.width()/4, mainAbility.top + mainAbility.height()/4, paint);


    }

    public void checkCollision(int touchX, int touchY, boolean touchDown){
        RectF touchPoint = new RectF(touchX, touchY, touchX + 1, touchY + 1);
        Log.i("AbilityController", "Checking Ability");
        if (mainAbility.contains(touchPoint)){
            player.useAbility(0);
            Log.i("AbilityController", "Using main Ability");
        }

    }

}
