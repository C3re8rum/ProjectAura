package sound;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.appng.projectaura.R;

public class BackgroundMusicService extends Service{
    MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    private String sharedPrefFile =
            "com.appng.projectaura";

    public static int volume;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("createService", "Entered Service");

        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        int volume = preferences.getInt("SOUNDLEVEL", 25);

        mediaPlayer = MediaPlayer.create(this, R.raw.elwynnforest);
        Log.d("createService", mediaPlayer.toString());

        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume, volume);
    }

    public void updateVolume(){
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        volume = preferences.getInt("SOUNDLEVEL", 25);

        mediaPlayer.setVolume(volume, volume);
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
