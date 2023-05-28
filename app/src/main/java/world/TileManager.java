package world;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.appng.projectaura.R;

import entity.Player;
import view.GameView;

public class TileManager {

    private Tile[] baseTiles;
    private MapFileInterpreter mapFileInterpreter;
    private GameView gameView;

    public TileManager(GameView gameView) {
        this.gameView = gameView;
        baseTiles = new Tile[512];
        mapFileInterpreter = new MapFileInterpreter(gameView);
        initializeTiles();
    }

    private void initializeTiles() {
        // The source of the current tileset is this: https://projectalme.itch.io/16x16-rpg-starter-set
        Bitmap tileSet = BitmapFactory.decodeResource(gameView.getContext().getResources(), R.drawable.tile_set);

        // Image is scaled, for unknown reasons android studio was scaling it up by a factor of 2.625...
        tileSet = Bitmap.createScaledBitmap(tileSet, (int)(tileSet.getWidth()/2.625), (int)(tileSet.getHeight()/2.625), true);

        int maxWidth = tileSet.getWidth()/gameView.BASE_TILE_SIZE;
        int maxHeight = tileSet.getHeight()/gameView.BASE_TILE_SIZE;

        int width = 0;
        int height = 0;
        int index = 1;

        while(width < (maxWidth) && height < (maxHeight)){

            Bitmap tileImage = Bitmap.createBitmap(tileSet,(width )*gameView.BASE_TILE_SIZE , (height)* gameView.BASE_TILE_SIZE, gameView.BASE_TILE_SIZE,gameView.BASE_TILE_SIZE );
            tileImage = Bitmap.createScaledBitmap(tileImage, gameView.TILE_SIZE, gameView.TILE_SIZE, true);
            if (index == 1){
                baseTiles[0] = new Tile(tileImage, false);
            }
            baseTiles[index] = new Tile(tileImage, false);
            // Log.d("Tile", "Added tile " + index + " to list!");

            width++;

            if (width == maxWidth){
                width = 0;
                height++;
            }


            index++;
        }

        Log.d("Tilemanager", "Tileset initialized");
    }


    public void draw(Canvas canvas, Paint paint){

        int worldColumn = 0;
        int worldRow = 0;

        // Log.d("Drawing", "Drawing world");
        while (worldColumn < gameView.WORLD_GRID_WIDTH && worldRow < gameView.WORLD_GRID_HEIGHT){
            // A tile's coordinate in the world
            int worldX = worldColumn*gameView.TILE_SIZE;
            int worldY = worldRow*gameView.TILE_SIZE;

            // The coordinate at which the tile will be drawn
            // On the screen
            int screenX = worldX - gameView.getPlayer().getWorldX() + gameView.getPlayer().getScreenX();
            int screenY = worldY - gameView.getPlayer().getWorldY() + gameView.getPlayer().getScreenY();

            Player player = gameView.getPlayer();
            int playerWorldX = player.getWorldX();
            int playerWorldY = player.getWorldY();
            int playerScreenX = player.getScreenX();
            int playerScreenY = player.getScreenY();

            int tileSize = gameView.TILE_SIZE;

            boolean insideOfScreen = worldX + tileSize > playerWorldX - playerScreenX &&
                                    worldX - tileSize < playerWorldX + playerScreenX &&
                                    worldY + tileSize > playerWorldY - playerScreenY &&
                                    worldY - tileSize < playerWorldY + playerScreenY;

            if (insideOfScreen) {
                int tileNumber = mapFileInterpreter.getCell(worldColumn, worldRow);

                Bitmap image = baseTiles[tileNumber].getImage();

                canvas.drawBitmap(image, screenX, screenY, paint);
            }

            worldColumn++;
            if (worldColumn == gameView.WORLD_GRID_WIDTH) {
                worldColumn = 0;
                worldRow++;

            }
        }
    }
}
