package sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.appng.projectaura.R;

public class BackgroundMusicService extends Service {
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("createService", "Entered Service");

        mediaPlayer = MediaPlayer.create(this, R.raw.elwynnforest);
        Log.d("createService", mediaPlayer.toString());

        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(50, 50);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("createService", "Entered startCommand");
        mediaPlayer.start();
        return startId;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
