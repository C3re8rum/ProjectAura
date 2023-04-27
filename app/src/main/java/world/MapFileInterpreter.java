package world;

import android.util.Log;

import com.appng.projectaura.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import view.GameView;

public class MapFileInterpreter {
    private int[][] map;

    GameView gameView;

    public MapFileInterpreter(GameView gameView) {
        this.gameView = gameView;

        createMapSize();
        initializeMap();

        Log.d("MAP", "Created MapFileInterpreter");
    }

    private void createMapSize() {
        InputStream mapFileInput = gameView.getContext().getResources().openRawResource(R.raw.maptest);
        Scanner sc = new Scanner(mapFileInput);

        int rows = 0;
        int columns = 0;
        while (sc.hasNextLine()) {
            if (rows == 0) {
                String line = sc.nextLine();
                System.out.println(line);

                columns = line.trim().split(",").length;
                rows++;
            }
            sc.nextLine();
            rows++;
        }
        this.map = new int[columns][rows];
        System.out.println(rows + " " + columns);
        System.out.println(map.length + " " + map[0].length);
    }


    public void initializeMap() {
        InputStream mapFileInput = gameView.getContext().getResources().openRawResource(R.raw.maptest);
        Scanner sc = new Scanner(mapFileInput);

        int row = 0;
        while (sc.hasNextLine() && row <= map.length) {
            String line = sc.nextLine().trim();
            String[] lineArray = line.split(",");

            int[] numberLine = new int[lineArray.length];
            int columnIndex = 0;
            for (String number : lineArray) {
                numberLine[columnIndex++] = Integer.valueOf(number);
            }

            map[row] = numberLine;

            row++;
        }


    }

    public int[] getMapSize() {
        int[] size = new int[2];
        size[0] = map[0].length;
        size[1] = map.length;

        return size;
    }


    public int getCell(int column, int row) {
        return this.map[column][row];
    }

}
