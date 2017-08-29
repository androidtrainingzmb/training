package tcd.training.com.trainingproject.ExternalHardware.Camera.CameraAPI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tcd.training.com.trainingproject.R;

public class TakePhotoUsingCameraApiActivity extends AppCompatActivity {

    private static final String TAG = TakePhotoUsingCameraApiActivity.class.getSimpleName();

    private Camera mCamera = null;
    private CameraPreview mCameraPreview;

    private Camera.PictureCallback mPictureCallback;

    private int mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_take_photo_using_camera_api);
        getSupportActionBar().hide();

        initializeUiComponents();

        if (checkCameraHardware()) {
            initializeCustomCamera();
            initializePictureCallback();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void initializeUiComponents() {
        ImageButton captureButton = findViewById(R.id.btn_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });

        ImageButton swapCameraButton = findViewById(R.id.btn_swap_camera);
        swapCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.stopPreview();
                mCamera.release();
                mCurrentCameraId = mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK ?
                        Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
                initializeCustomCamera();
            }
        });
    }

    private void initializeCustomCamera() {
        mCamera = getCameraInstance(mCurrentCameraId);

        mCameraPreview =  new CameraPreview(this, mCamera);
        FrameLayout previewFrameLayout = findViewById(R.id.fl_camera_preview);
        previewFrameLayout.removeAllViews();
        previewFrameLayout.addView(mCameraPreview);
    }

    private boolean checkCameraHardware() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.d(TAG, "Camera available");
            return true;
        } else {
            Log.d(TAG, "Camera unavailable");
            return false;
        }
    }

    private static Camera getCameraInstance(int cameraId) {
        Camera camera = null;
        // Always check for exceptions when using Camera.open()
        // Failing to check for exceptions if the camera is in use or does not exist
        // will cause your application to be shut down by the system.
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e(TAG, "getCameraInstance: ", e);
        }
        return camera;
    }

    private void initializePictureCallback() {
        mPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                // create the picture file
                File pictureFile = getOutputMediaFile();
                if (pictureFile == null) {
                    Log.d(TAG, "onPictureTaken: Failed to create media file.");
                    return;
                }

                Log.d(TAG, "onPictureTaken: ");
                // save picture
                try {
                    FileOutputStream outputStream = new FileOutputStream(pictureFile);
                    outputStream.write(bytes);
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "onPictureTaken: File not found. Error: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "onPictureTaken: Error accessing file. Error" + e.getMessage());
                }

                // return to previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra(getString(R.string.data), pictureFile.getAbsolutePath());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };
    }

    private File getOutputMediaFile() {
        if (!isExternalStorageWritable()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.require_external_storage_permission_error), Snackbar.LENGTH_SHORT).show();
            return null;
        }

        // navigate to the directory (create if it doesn't exist)
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getPackageName());
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdir()) {
                Log.d(TAG, "getOutputMediaFile: Failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
