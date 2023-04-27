package world;

import android.graphics.Bitmap;

public class Tile {
    private Bitmap image;
    private boolean hasCollision = false;

    public Tile(Bitmap image, boolean hasCollision) {
        this.image = image;
        this.hasCollision = hasCollision;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean hasCollection() {
        return hasCollision;
    }

    public void setCollision(boolean hasCollision) {
        this.hasCollision = hasCollision;
    }
}
