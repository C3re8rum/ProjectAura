package world;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.appng.projectaura.R;

import view.GameView;

public class TileManager {

    private Tile[] baseTiles;
    private int[][] map;
    private GameView gameView;

    public TileManager(GameView gameView) {
        this.gameView = gameView;
        baseTiles = new Tile[512];
        initializeTiles();
    }

    private void initializeTiles() {
        // The source of the current tileset is this: https://projectalme.itch.io/16x16-rpg-starter-set
        Bitmap tileSet = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.tile_set);

        int gridHeight = 0;
        int gridWidth = 0;

        for (int i = 0; i < 512; i++){
            Bitmap tileImage = Bitmap.createBitmap(tileSet,(gridWidth )*gameView.BASE_TILE_SIZE , (gridHeight)* gameView.BASE_TILE_SIZE, gameView.BASE_TILE_SIZE,gameView.BASE_TILE_SIZE );
            tileImage = Bitmap.createScaledBitmap(tileImage, gameView.TILE_SIZE, gameView.TILE_SIZE, true);
            baseTiles[i] = new Tile(tileImage, false);
            Log.d("Tile", "Added tile " + i + " to list!");

            gridWidth++;
            if (gridWidth == 29){
                gridWidth = 0;
                gridHeight++;
            }

        }


    }


    // Add draw method

    public void draw(Canvas canvas, Paint paint){
        /*
        int gridHeight = 0;
        int gridWidth = 0;

        for (int i = 0; i < baseTiles.length; i++) {
            Tile tile = baseTiles[i];

            canvas.drawBitmap(tile.getImage(), gameView.TILE_SIZE*gridWidth, gameView.TILE_SIZE*gridHeight, paint);
            gridWidth++;
            if (gridWidth == 29){
                gridWidth = 0;
                gridHeight++;
            }

            Log.i("Draw_Tile", "Tile_" + i + ": (" + gameView.TILE_SIZE*gridWidth + ", " + gameView.TILE_SIZE*gridHeight + ")");
        }
        */
        Bitmap tileSet = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.tile_set);
        System.out.println("Width: " + tileSet.getWidth() + " Height: " + tileSet.getHeight());

        canvas.drawBitmap(tileSet, 0, 0, paint);

    }
}
