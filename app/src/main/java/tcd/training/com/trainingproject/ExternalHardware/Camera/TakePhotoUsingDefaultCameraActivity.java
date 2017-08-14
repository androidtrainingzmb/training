package tcd.training.com.trainingproject.ExternalHardware.Camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import tcd.training.com.trainingproject.R;

public class TakePhotoUsingDefaultCameraActivity extends AppCompatActivity {

    private ImageView mTakenPhotoImageView;

    private static final int RC_PERMISSION_CAMERA = 1;
    private static final int RC_TAKE_PHOTO = 2;
    private Uri mTakenPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_using_default_camera);

        initializeUiComponents();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, RC_PERMISSION_CAMERA);
                } else {
                    initializeCamera();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeUiComponents() {
        mTakenPhotoImageView = findViewById(R.id.iv_taken_photo);
    }

    private void initializeCamera() {
        mTakenPhotoUri = Uri.fromFile(new File(getExternalFilesDir(null), "image.jpg"));
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakenPhotoUri);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(cameraIntent, getString(R.string.complete_action_using_label)), RC_TAKE_PHOTO);
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_app_handle_intent_error), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Glide.with(this).load(mTakenPhotoUri).into(mTakenPhotoImageView);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeCamera();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.require_camera_permission_error), Snackbar.LENGTH_LONG).show();
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
