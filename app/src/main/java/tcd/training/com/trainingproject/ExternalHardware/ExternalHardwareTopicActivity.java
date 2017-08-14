package tcd.training.com.trainingproject.ExternalHardware;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.ExternalHardware.AudioPlayer.AudioPlayerActivity;
import tcd.training.com.trainingproject.ExternalHardware.Camera.TakePhotoUsingDefaultCameraActivity;
import tcd.training.com.trainingproject.ExternalHardware.GPS.ObtainLocationUsingGooglePlayServicesActivity;
import tcd.training.com.trainingproject.ExternalHardware.GPS.ObtainLocationUsingLocationManagerActivity;
import tcd.training.com.trainingproject.ExternalHardware.Gallery.ChooseImageFromGalleryActivity;
import tcd.training.com.trainingproject.ExternalHardware.VideoPlayer.VideoPlayerUsingExoPlayerActivity;
import tcd.training.com.trainingproject.ExternalHardware.VideoPlayer.VideoPlayerUsingMediaPlayerActivity;
import tcd.training.com.trainingproject.R;

public class ExternalHardwareTopicActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private ListView mTopicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Audio playback with MediaPlayer", AudioPlayerActivity.class);
        topics.put("2. Audio playback with ExoPlayer", AudioPlayerActivity.class);
        topics.put("3. Video playback with MediaPlayer", VideoPlayerUsingMediaPlayerActivity.class);
        topics.put("4. Video playback with ExoPlayer", VideoPlayerUsingExoPlayerActivity.class);
        topics.put("5. Choose images from gallery", ChooseImageFromGalleryActivity.class);
        topics.put("6. Obtain device's location using LocationManager", ObtainLocationUsingLocationManagerActivity.class);
        topics.put("7. Obtain device's location using Google Play services API", ObtainLocationUsingGooglePlayServicesActivity.class);
        topics.put("8. Take photo using default camera", TakePhotoUsingDefaultCameraActivity.class);

        mTopicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isReadExternalStoragePermissionGranted()) {
                    return;
                }
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ExternalHardwareTopicActivity.this, topics.get(key));
                if (key.contains("MediaPlayer")) {
                    intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_MEDIA_PLAYER_METHOD);
                } else if (key.contains("ExoPlayer")) {
                    intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_EXO_PLAYER_METHOD);
                }
                startActivity(intent);
            }
        });
    }

    private boolean isReadExternalStoragePermissionGranted() {
        // check if image already exists
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.external_storage_access_error), Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.grant), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(ExternalHardwareTopicActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
                snackbar.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    isReadExternalStoragePermissionGranted();
                }
                return;
            }
        }
    }
}
