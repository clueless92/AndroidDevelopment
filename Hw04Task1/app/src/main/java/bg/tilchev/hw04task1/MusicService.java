package bg.tilchev.hw04task1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {

    private static final String TAG = MusicService.class.getSimpleName();

    public class MusicServiceBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }
    }

    private IBinder mBinder;
    private MediaPlayer mPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public void onCreate() {
        this.mPlayer = new MediaPlayer();
        this.mBinder = new MusicServiceBinder();
        this.initMediaPlayerPreferences();
    }

    public void selectSong(int resID) {
        this.mPlayer.reset();
        Context context = this.getApplicationContext();
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(resID);
        if (afd == null) {
            return;
        }
        try {
            this.mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            this.mPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void initMediaPlayerPreferences() {
        this.mPlayer.setWakeMode(this.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        this.mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public int getPosition(){
        return this.mPlayer.getCurrentPosition();
    }

    public int getDuration(){
        return this.mPlayer.getDuration();
    }

    public boolean isPlaying(){
        return this.mPlayer.isPlaying();
    }

    public void pausePlayer(){
        this.mPlayer.pause();
    }

    public void seekTo(int position){
        this.mPlayer.seekTo(position);
    }

    public void play(){
        this.mPlayer.start();
    }
}
