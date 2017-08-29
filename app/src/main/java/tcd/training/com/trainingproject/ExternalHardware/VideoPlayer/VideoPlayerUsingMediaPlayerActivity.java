package tcd.training.com.trainingproject.ExternalHardware.VideoPlayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import tcd.training.com.trainingproject.R;

public class VideoPlayerUsingMediaPlayerActivity extends AppCompatActivity
        implements SurfaceHolder.Callback,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {

    private final static String TAG = VideoPlayerUsingMediaPlayerActivity.class.getSimpleName();

    private SurfaceView mPlayerSurfaceView;
    private SurfaceHolder mHolder;

    private MediaPlayer mMediaPlayer;
    private int mPlayingPosition = -1;
    private boolean mIsVideoReadyToBePlayed = false;
    private boolean mIsVideoSizeKnown;
    private int mVideoWidth;
    private int mVideoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player_using_media_player);
        getSupportActionBar().hide();

        if (savedInstanceState != null) {
            mPlayingPosition = savedInstanceState.getInt(getString(R.string.integer_type));
        }

        mPlayerSurfaceView = findViewById(R.id.sv_video_player);
        mHolder = mPlayerSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPlayerSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mPlayingPosition = mMediaPlayer.getCurrentPosition();
                } else {
                    mMediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMediaPlayer != null) {
            outState.putInt(getString(R.string.integer_type), mMediaPlayer.getCurrentPosition());
        } else {
            outState.putInt(getString(R.string.integer_type), mPlayingPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        if (mMediaPlayer != null) {
            mPlayingPosition = mMediaPlayer.getCurrentPosition();
        }
        super.onPause();
        cleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanUp();
    }

    private void playVideo() {
        cleanUp();
        Uri contentUri = getUriFromContentResolver();
        if (contentUri != null) {
            try {

                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
                mMediaPlayer.setDisplay(mHolder);
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnBufferingUpdateListener(this);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnVideoSizeChangedListener(this);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri getUriFromContentResolver() {
        // check permission for accessing content resolver
        Uri resultUri = null;
        // read songs list from content resolver
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            Toast.makeText(this, getString(R.string.handling_cursor_error), Toast.LENGTH_SHORT).show();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(this, getString(R.string.no_media_error), Toast.LENGTH_SHORT).show();
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            // get the first found video
            long id = cursor.getLong(idColumn);
            resultUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        }
        assert cursor != null;
        cursor.close();

        return resultUri;
    }

    private void cleanUp() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceCreated: ");
        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        Log.d(TAG, "onBufferingUpdate: ");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion: ");
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared: ");
        mIsVideoReadyToBePlayed = true;
        startVideo();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        if (width == 0 || height == 0) {
            Log.e(TAG, "onVideoSizeChanged: invalid video width (" + width + ") or height (" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        startVideo();
    }

    private void startVideo() {
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            setVideoSize();
            mHolder.setFixedSize(mVideoWidth, mVideoHeight);
            mMediaPlayer.start();
            if (mPlayingPosition > -1) {
                mMediaPlayer.seekTo(mPlayingPosition);
            }
        }
    }

    private void setVideoSize() {

        // // Get the dimensions of the video
        int videoWidth = mVideoWidth;
        int videoHeight = mVideoHeight;
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        if (videoProportion > screenProportion) {
            mVideoWidth = screenWidth;
            mVideoHeight = (int) ((float) screenWidth / videoProportion);
        } else {
            mVideoWidth = (int) (videoProportion * (float) screenHeight);
            mVideoHeight = screenHeight;
        }
    }
}
