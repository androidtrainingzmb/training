package tcd.training.com.trainingproject.ExternalHardware.AudioPlayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.ArrayList;

import tcd.training.com.trainingproject.R;

public class AudioPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AudioPlayerActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int USING_MEDIA_PLAYER_METHOD = 2;
    public static final int USING_EXO_PLAYER_METHOD = 3;

    private ImageButton mMediaPlayImageButton;
    private TextView mSongNameTextView;
    private TextView mSongArtistTextView;

    private RecyclerView mSongsRecyclerView;
    private SongsAdapter mSongsAdapter;
    private ArrayList<Song> mSongsList;

    private MediaPlayer mMediaPlayer = null;
    private int mCurrentPlayingSongIndex = -1;
    private SimpleExoPlayer mExoPlayer = null;
    private int mUsingMethod = USING_EXO_PLAYER_METHOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        mUsingMethod = getIntent().getIntExtra(getString(R.string.integer_type), USING_MEDIA_PLAYER_METHOD);

        initializeUiComponents();
        initializeExoPlayerComponents();

        fetchPlaylist();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initializeUiComponents() {
        // player's name
        TextView methodName = findViewById(R.id.tv_player_name);
        methodName.setText(mUsingMethod == USING_MEDIA_PLAYER_METHOD ? "MediaPlayer" : "ExoPlayer");

        // playback buttons
        ImageButton mMediaRewindImageButton = findViewById(R.id.ib_media_rew);
        ImageButton mMediaPreviousImageButton= findViewById(R.id.ib_media_previous);
        mMediaPlayImageButton = findViewById(R.id.ib_media_play);
        ImageButton mMediaNextImageButton = findViewById(R.id.ib_media_next);
        ImageButton mMediaFastForwardImageButton = findViewById(R.id.ib_media_ff);
        // and their listener
        mMediaRewindImageButton.setOnClickListener(this);
        mMediaPreviousImageButton.setOnClickListener(this);
        mMediaPlayImageButton.setOnClickListener(this);
        mMediaNextImageButton.setOnClickListener(this);
        mMediaFastForwardImageButton.setOnClickListener(this);

        // currently playing song info
        mSongNameTextView = findViewById(R.id.tv_song_name);
        mSongArtistTextView = findViewById(R.id.tv_song_artist);

        // recycler view
        mSongsRecyclerView = findViewById(R.id.rv_songs_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mSongsRecyclerView.setLayoutManager(mLayoutManager);
        mSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSongsList = new ArrayList<>();
        mSongsAdapter = new SongsAdapter(mSongsList);
        mSongsRecyclerView.setAdapter(mSongsAdapter);
        mSongsAdapter.SetOnItemClickListener(new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentPlayingSongIndex = position;
                playSong();
            }
        });
    }

    private void initializeExoPlayerComponents() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
    }

    private void fetchPlaylist() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, getString(R.string.handling_cursor_error), Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(this, getString(R.string.no_media_error), Toast.LENGTH_SHORT).show();
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);

                Song song = new Song(id, title, artist);
                mSongsList.add(song);
                mSongsAdapter.notifyDataSetChanged();

            } while (cursor.moveToNext());
        }
    }

    private void playSong() {
        Song song = mSongsList.get(mCurrentPlayingSongIndex);

        mMediaPlayImageButton.setImageResource(android.R.drawable.ic_media_pause);
        mSongNameTextView.setText(song.getName());
        mSongArtistTextView.setText(song.getArtist());

        streamSongFromContentResolver(song);

        mSongsRecyclerView.scrollToPosition(mCurrentPlayingSongIndex);
    }

    private void streamSongFromContentResolver(Song song) {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, getString(R.string.handling_cursor_error), Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(this, getString(R.string.no_media_error), Toast.LENGTH_SHORT).show();
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                long id = cursor.getLong(idColumn);
                if (id == song.getId()) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }

                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    if (mUsingMethod == USING_MEDIA_PLAYER_METHOD) {
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
                            mMediaPlayer.prepareAsync();
                            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    mediaPlayer.start();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (mUsingMethod == USING_EXO_PLAYER_METHOD) {
                        // Measures bandwidth during playback. Can be null if not required.
                        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                        // Produces DataSource instances through which media data is loaded.
                        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                                Util.getUserAgent(this, getPackageName()), bandwidthMeter);
                        // Produces Extractor instances for parsing the media data.
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        // This is the MediaSource representing the media to be played.
                        MediaSource mediaSource = new ExtractorMediaSource(contentUri, dataSourceFactory, extractorsFactory, null, null);
                        // Prepare the player with the source.
                        mExoPlayer.prepare(mediaSource);
                        mExoPlayer.setPlayWhenReady(true);
                    }
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onClick(View view) {
        if ((mMediaPlayer == null && mUsingMethod == USING_MEDIA_PLAYER_METHOD)) {
            return;
        }

        switch (view.getId()) {
            case R.id.ib_media_play:
                if (mUsingMethod == USING_MEDIA_PLAYER_METHOD) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mMediaPlayImageButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mMediaPlayer.start();
                        mMediaPlayImageButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                } else {
                    if (mExoPlayer.getPlayWhenReady()) {
                        mExoPlayer.setPlayWhenReady(false);
                        mMediaPlayImageButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mExoPlayer.setPlayWhenReady(true);
                        mMediaPlayImageButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }
                break;
            case R.id.ib_media_next:
                mCurrentPlayingSongIndex++;
                if (mCurrentPlayingSongIndex == mSongsList.size()) {
                    mCurrentPlayingSongIndex = 0;
                }
                playSong();
                break;
            case R.id.ib_media_previous:
                mCurrentPlayingSongIndex--;
                if (mCurrentPlayingSongIndex < 0) {
                    mCurrentPlayingSongIndex = mSongsList.size() - 1;
                }
                playSong();
                break;
            case R.id.ib_media_ff:
                break;
            case R.id.ib_media_rew:
                break;
        }
    }
}
