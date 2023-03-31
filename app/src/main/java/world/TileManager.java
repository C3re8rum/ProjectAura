package world;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        Bitmap tileSet = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.tile_set);

        int gridHeight = 0;
        int gridWidth = 0;
        for (int i = 0; i < 510; i++){
            Bitmap tileImage = Bitmap.createBitmap(tileSet,0 , 0, (gridWidth +1 )*gameView.BASE_TILE_SIZE, (gridHeight + 1)* gameView.BASE_TILE_SIZE);
            baseTiles[i] = new Tile(tileImage, false);
            gridWidth++;
            if (gridWidth == 30){
                gridWidth = 0;
                gridHeight++;
            }
            Log.d("Debug", "Added tile to list!");

        }


    }


    // Add draw method
}
