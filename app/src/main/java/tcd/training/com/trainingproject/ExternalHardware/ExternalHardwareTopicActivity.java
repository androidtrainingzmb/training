package tcd.training.com.trainingproject.ExternalHardware;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import tcd.training.com.trainingproject.ExternalHardware.Camera.TakePhotoTopicActivity;
import tcd.training.com.trainingproject.ExternalHardware.GPS.ObtainLocationUsingGooglePlayServicesActivity;
import tcd.training.com.trainingproject.ExternalHardware.GPS.ObtainLocationUsingLocationManagerActivity;
import tcd.training.com.trainingproject.ExternalHardware.Gallery.ChooseImageFromGalleryActivity;
import tcd.training.com.trainingproject.ExternalHardware.VideoPlayer.VideoPlayerUsingExoPlayerActivity;
import tcd.training.com.trainingproject.ExternalHardware.VideoPlayer.VideoPlayerUsingMediaPlayerActivity;
import tcd.training.com.trainingproject.R;

public class ExternalHardwareTopicActivity extends AppCompatActivity {

    private static final int RC_READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int RC_FINE_LOCATION_PERMISSION = 2;
    private static final int RC_CAMERA_PERMISSION = 3;
    private static final int RC_WRITE_EXTERNAL_STORAGE_PERMISSION = 4;

    private ListView mTopicsListView;

    final LinkedHashMap<String, Class> mTopicsList = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        initializeTopicsList();
        initializeUiComponents();
    }

    private void initializeTopicsList() {
        mTopicsList.put("1. Audio playback with MediaPlayer", AudioPlayerActivity.class);
        mTopicsList.put("2. Audio playback with ExoPlayer", AudioPlayerActivity.class);
        mTopicsList.put("3. Video playback with MediaPlayer", VideoPlayerUsingMediaPlayerActivity.class);
        mTopicsList.put("4. Video playback with ExoPlayer", VideoPlayerUsingExoPlayerActivity.class);
        mTopicsList.put("5. Choose images from gallery", ChooseImageFromGalleryActivity.class);
        mTopicsList.put("6. Obtain device's location using LocationManager", ObtainLocationUsingLocationManagerActivity.class);
        mTopicsList.put("7. Obtain device's location using Google Play services API", ObtainLocationUsingGooglePlayServicesActivity.class);
        mTopicsList.put("8. Take photo using default camera", TakePhotoTopicActivity.class);
        mTopicsList.put("9. Take photo using Camera API", TakePhotoTopicActivity.class);
    }

    private void initializeUiComponents() {
        mTopicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mTopicsList.keySet().toArray(new String[0]));
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);

                if (!isRequiredPermissionGranted(key)) {
                    return;
                }

                Intent intent = prepareIntent(key);
                startActivity(intent);
            }
        });
    }

    private Intent prepareIntent(String topic) {
        Intent intent = new Intent(this, mTopicsList.get(topic));
        if (topic.contains("MediaPlayer")) {
            intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_MEDIA_PLAYER_METHOD);
        } else if (topic.contains("ExoPlayer")) {
            intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_EXO_PLAYER_METHOD);
        }
        return intent;
    }

    private boolean isRequiredPermissionGranted(String topic) {
        if (topic.contains("playback") || topic.contains("gallery")) {
            return requestReadExternalStoragePermission();
        } else if (topic.contains("location")) {
            return requestFineLocationPermission();
        } else if (topic.contains("camera")) {
            requestWriteExternalStoragePermission();
            return requestCameraPermission();
        }
        return true;
    }

    private boolean requestPermission(final String permission, String errorMessage, final int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);
                    snackbar.setAction(getString(R.string.grant), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ExternalHardwareTopicActivity.this, new String[]{permission}, requestCode);
                        }
                    });
                    snackbar.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission},
                            requestCode);
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean requestReadExternalStoragePermission() {
        return requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                getString(R.string.require_external_storage_permission_error),
                RC_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    private boolean requestFineLocationPermission() {
        return requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                getString(R.string.require_fine_location_permission_error),
                RC_FINE_LOCATION_PERMISSION);
    }

    private boolean requestWriteExternalStoragePermission() {
        return requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                getString(R.string.require_external_storage_permission_error),
                RC_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    private boolean requestCameraPermission() {
        return requestPermission(Manifest.permission.CAMERA,
                getString(R.string.require_camera_permission_error),
                RC_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case RC_READ_EXTERNAL_STORAGE_PERMISSION:
                    requestReadExternalStoragePermission();
                    break;
                case RC_FINE_LOCATION_PERMISSION:
                    requestFineLocationPermission();
                    break;
                case RC_WRITE_EXTERNAL_STORAGE_PERMISSION:
                    requestWriteExternalStoragePermission();
                    break;
                case RC_CAMERA_PERMISSION:
                    requestCameraPermission();
                    break;
            }
        }
    }
}
