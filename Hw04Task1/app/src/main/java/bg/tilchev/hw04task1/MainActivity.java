package bg.tilchev.hw04task1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        RecycleViewSelectedElement,
        MediaController.MediaPlayerControl {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Intent mMusicServiceIntent;
    private MusicService mMusicService;
    private MusicController mController;
    private ServiceConnection mMusicConnection;

    private List<Song> mSongs;
    private Song mSelectedSong;

    private boolean mPaused;
    private boolean mPlaybackPaused;
    private boolean mMusicBound;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);

        Toolbar toolbar = this.getToolbar();
        if(toolbar == null) {
            return;
        }
        toolbar.setTitle(this.getTitle());
        toolbar.setNavigationIcon(R.drawable.settings_spotify_cut);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.color_text));
        this.setSupportActionBar(toolbar);

        this.mMusicConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "MusicService connected!");
                MusicService.MusicServiceBinder binder = (MusicService.MusicServiceBinder) service;
                MainActivity.this.mMusicService = binder.getService();
                MainActivity.this.mMusicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "MusicService disconnected!");
                MainActivity.this.mMusicBound = false;
            }
        };

        this.initData();
        this.mSelectedSong = this.mSongs.get(this.mPosition);

        RecyclerView mRecyclerView = (RecyclerView) this.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, 1, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapter(this.mSongs, this);
        mRecyclerView.setAdapter(mAdapter);
        // NOTE: Decoration is kinda useless after image setting was moved to RV adapter
//        RecyclerViewCustomDecoration mDecoration = new RecyclerViewCustomDecoration(this);
//        mRecyclerView.addItemDecoration(mDecoration);

        this.setUpController();
    }

    private Toolbar getToolbar() {
        View view = this.findViewById(R.id.tool_bar);
        if (view instanceof Toolbar) {
            return (Toolbar) view;
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.mMusicServiceIntent == null) {
            this.mMusicServiceIntent = new Intent(this, MusicService.class);
        }
        this.bindService(this.mMusicServiceIntent, this.mMusicConnection, Context.BIND_AUTO_CREATE);
        this.startService(this.mMusicServiceIntent);
        this.setUpController();
    }

    @Override
    protected void onDestroy() {
        if (this.mMusicService == null) {
            this.mMusicServiceIntent = new Intent(this, MusicService.class);
        }
        this.unbindService(this.mMusicConnection);
        this.stopService(this.mMusicServiceIntent);
        this.mMusicService = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mPaused) {
            this.setUpController();
            this.mPaused = false;
        }
    }

    @Override
    protected void onStop() {
        this.mController.hide();
        super.onStop();
    }

    @Override
    public void onSongSelected(int position) {
        this.mSelectedSong = this.mSongs.get(position);
        this.mPosition = position;
        Toolbar toolbar = this.getToolbar();
        if(toolbar == null) {
            return;
        }
        toolbar.setTitle(this.mSelectedSong.getSongName());
        this.mMusicService.stopSelf();
        this.mMusicService.selectSong(this.mSelectedSong.getResID());
        if(this.mPlaybackPaused) {
            this.setUpController();
            this.mPlaybackPaused = false;
        }
        this.mController.show(0);
    }

    @Override
    public void start() {
        this.mMusicService.play();
        super.onStart();
    }

    @Override
    public void pause() {
        this.mPlaybackPaused = true;
        this.mMusicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if (this.mMusicService != null && this.mMusicBound) {
            return this.mMusicService.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (this.mMusicService != null && this.mMusicBound) {
            return this.mMusicService.getPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        this.mMusicService.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if (this.mMusicService != null && this.mMusicBound) {
            return this.mMusicService.isPlaying();
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setUpController() {
        if (this.mController == null) {
            this.mController = new MusicController(this);
        } else {
            this.mController.invalidate();
        }
        this.mController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.playPrev();
            }
        });
        this.mController.setMediaPlayer(this);
        this.mController.setAnchorView(this.findViewById(R.id.recycler_view));
        this.mController.setEnabled(true);
    }

    private void playPrev() {
        this.mPosition--;
        if(this.mPosition < 0) {
            this.mPosition = this.mSongs.size() - 1;
        }
        this.onSongSelected(this.mPosition);
        this.start();
    }

    private void playNext() {
        this.mPosition++;
        if(this.mPosition >= this.mSongs.size()) {
            this.mPosition = 0;
        }
        this.onSongSelected(this.mPosition);
        this.start();
    }

    // data is faked for testing
    private void initData() {
        String[] songTitles = new String[] {
                "Disturbed - Ten Thousand Fists",
                "Disturbed - Another Way To Die",
                "Disturbed - Asylum",
                "Disturbed - Awaken",
                "Disturbed - Believe",
                "Disturbed - Bound",
                "Disturbed - Conflict",
                "Disturbed - Criminal",
                "Disturbed - Darkness",
                "Disturbed - Decadence",
                "Disturbed - Deceiver",
                "Disturbed - Down With The Sickness",
                "Disturbed - Droppin' Plates",
                "Disturbed - Enough",
                "Disturbed - Facade",
                "Disturbed - Fear",
                "Disturbed - Fire It Up",
                "Disturbed - God Of The Mind",
                "Disturbed - Indestructible",
                "Disturbed - Warrior"
        };
        int[] sondIDs = new int[] {
                R.raw.disturbed_ten_thousand_fists,
                R.raw.disturbed_another_way_to_die,
                R.raw.disturbed_asylum,
                R.raw.disturbed_awaken,
                R.raw.disturbed_believe,
                R.raw.disturbed_bound,
                R.raw.disturbed_conflict,
                R.raw.disturbed_criminal,
                R.raw.disturbed_darkness,
                R.raw.disturbed_decadence,
                R.raw.disturbed_deceiver,
                R.raw.disturbed_down_with_the_sickness,
                R.raw.disturbed_droppin_plates,
                R.raw.disturbed_enough,
                R.raw.disturbed_facade,
                R.raw.disturbed_fear,
                R.raw.disturbed_fire_it_up,
                R.raw.disturbed_god_of_the_mind,
                R.raw.disturbed_indestructible,
                R.raw.disturbed_warrior
        };
        this.mSongs = new ArrayList<>();
        for (int i = 0; i < songTitles.length; i++) {
            Song song = new Song(songTitles[i], sondIDs[i]);
            this.mSongs.add(song);
        }
    }
}
