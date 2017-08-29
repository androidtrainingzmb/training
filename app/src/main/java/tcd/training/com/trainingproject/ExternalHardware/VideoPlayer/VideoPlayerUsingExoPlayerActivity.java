package tcd.training.com.trainingproject.ExternalHardware.VideoPlayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import tcd.training.com.trainingproject.R;

public class VideoPlayerUsingExoPlayerActivity extends AppCompatActivity {

    private SimpleExoPlayer mExoPlayer = null;
    private long mCurrentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player_using_exo_player);
        getSupportActionBar().hide();

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getLong(getString(R.string.current_position));
        }

        initializeExoPlayerComponents();
        initializeUiComponents();

        playVideo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mExoPlayer != null) {
            outState.putLong(getString(R.string.current_position), mExoPlayer.getCurrentPosition());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void playVideo() {
        Uri contentUri = getUriFromContentResolver();
        if (contentUri != null) {
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, getPackageName()), null);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(contentUri, dataSourceFactory, extractorsFactory, null, null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            if (mCurrentPosition > -1) {
                mExoPlayer.seekTo(mCurrentPosition);
            }
        }
    }

    private void initializeUiComponents() {
        SimpleExoPlayerView simpleExoPlayerView = findViewById(R.id.exo_player_view);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(mExoPlayer);
    }

    private void initializeExoPlayerComponents() {
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
    }

    private Uri getUriFromContentResolver() {
        // check permission for accessing content resolver
        Uri resultUri = null;
        // read songs list from content resolver
        ContentResolver contentResolver = getContentResolver();
        Uri externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(externalContentUri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, getString(R.string.handling_cursor_error), Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(this, getString(R.string.no_media_error), Toast.LENGTH_SHORT).show();
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            // get the first video
            long id = cursor.getLong(idColumn);
            resultUri = ContentUris.withAppendedId(externalContentUri, id);
        }
        assert cursor != null;
        cursor.close();

        return resultUri;
    }
}
