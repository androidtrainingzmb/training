package tcd.training.com.trainingproject.ExternalHardware.Camera;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import tcd.training.com.trainingproject.ExternalHardware.Camera.CameraAPI.TakePhotoUsingCameraApiActivity;
import tcd.training.com.trainingproject.ExternalHardware.Gallery.ViewPagerAdapter;
import tcd.training.com.trainingproject.R;

public class TakePhotoTopicActivity extends AppCompatActivity {

    private static final String TAG = TakePhotoTopicActivity.class.getSimpleName();

    private static final int RC_TAKE_PHOTO_DEFAULT_CAMERA = 1;
    private static final int RC_TAKE_PHOTO_CAMERA_API = 2;
    private static final int RC_TAKE_PHOTO_CAMERA_2_API = 3;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<String> mImagesPathList;

    private Uri mTakenPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_topic);

        initializeUiComponents();

        initializeViewPagerComponents();
    }

    private void initializeUiComponents() {
        Button takePhotoUsingDefaultCameraButton = findViewById(R.id.btn_take_photo_default_camera);
        takePhotoUsingDefaultCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeDefaultCamera();
            }
        });

        Button takePhotoUsingCameraApiButton = findViewById(R.id.btn_take_photo_camera_api);
        takePhotoUsingCameraApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeCameraApi();
            }
        });

        Button takePhotoUsingCamera2ApiButton = findViewById(R.id.btn_take_photo_camera2_api);
        takePhotoUsingCamera2ApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeCamera2Api();
            }
        });
    }

    private void initializeViewPagerComponents() {
        mImagesPathList = new ArrayList<>();
        mViewPagerAdapter = new ViewPagerAdapter(this, mImagesPathList);
        mViewPager = findViewById(R.id.vp_slide_show);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initializeDefaultCamera() {
        mTakenPhotoUri = Uri.fromFile(new File(getExternalFilesDir(null), "IMG_" + mImagesPathList.size() + ".jpg"));
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakenPhotoUri);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(cameraIntent, getString(R.string.complete_action_using_label)), RC_TAKE_PHOTO_DEFAULT_CAMERA);
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.no_app_handle_intent_error), Snackbar.LENGTH_LONG).show();
        }
    }

    private void initializeCameraApi() {
        Intent intent = new Intent(this, TakePhotoUsingCameraApiActivity.class);
        startActivityForResult(intent, RC_TAKE_PHOTO_CAMERA_API);
    }

    private void initializeCamera2Api() {
        Intent intent = new Intent(this, TakePhotoUsingCamera2ApiActivity.class);
        startActivityForResult(intent, RC_TAKE_PHOTO_CAMERA_2_API);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_TAKE_PHOTO_DEFAULT_CAMERA:
                if (resultCode == RESULT_OK) {
                    mImagesPathList.add(mTakenPhotoUri.toString());
                    mViewPagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mImagesPathList.size() - 1);
                }
                break;
            case RC_TAKE_PHOTO_CAMERA_API: case RC_TAKE_PHOTO_CAMERA_2_API:
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getStringExtra(getString(R.string.data));
                    if (imagePath != null) {
                        mImagesPathList.add(imagePath);
                    }
                    mViewPagerAdapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(mImagesPathList.size() - 1);
                }
                break;
        }
    }
}
