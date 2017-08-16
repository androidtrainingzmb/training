package tcd.training.com.trainingproject.ExternalHardware.Camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;

import tcd.training.com.trainingproject.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TakePhotoUsingCamera2ApiActivity extends AppCompatActivity {

    private final static String TAG = TakePhotoUsingCamera2ApiActivity.class.getSimpleName();
    private static final int RC_CAMERA_PERMISSION = 1;

    private TextureView mCameraTextureView;
    private TextureView.SurfaceTextureListener mTextureListener;

    private String mCurrentCameraId;
    private android.util.Size mImageDimension = null;
    private CameraDevice.StateCallback mStateCallback = null;
    private CameraDevice mCameraDevice = null;
    private ImageReader mImageReader = null;
    private CaptureRequest.Builder mCaptureRequestBuilder = null;
    private CameraCaptureSession mCameraCaptureSessions = null;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo_using_camera2_api);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Toast.makeText(getApplicationContext(), getString(R.string.require_lollipop_above_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        initializeUiComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        // start background thread
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        if (mCameraTextureView.isAvailable()) {
            openCamera();
        } else {
            mCameraTextureView.setSurfaceTextureListener(mTextureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        closeCamera();
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    private void initializeUiComponents() {
        Log.d(TAG, "initializeUiComponents: ");
        // texture view
        mCameraTextureView = findViewById(R.id.texture_view_camera);
        if (mTextureListener == null) {
            initializeSurfaceTextureListener();
        }
        mCameraTextureView.setSurfaceTextureListener(mTextureListener);

        // buttons
        ImageButton captureButton = findViewById(R.id.btn_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    private void initializeSurfaceTextureListener() {
        Log.d(TAG, "initializeSurfaceTextureListener: ");
        mTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.d(TAG, "onSurfaceTextureAvailable: ");
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
                Log.d(TAG, "onSurfaceTextureSizeChanged: ");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                Log.d(TAG, "onSurfaceTextureDestroyed: ");
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                Log.d(TAG, "onSurfaceTextureUpdated: ");
            }
        };
    }

    private void openCamera() {
        Log.d(TAG, "openCamera: ");
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            mCurrentCameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCurrentCameraId);
            StreamConfigurationMap configurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mImageDimension = configurationMap.getOutputSizes(SurfaceTexture.class)[0];


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        RC_CAMERA_PERMISSION);
                return;
            }
            if (mStateCallback == null) {
                initializeStateCallback();
            }
            cameraManager.openCamera(mCurrentCameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void initializeStateCallback() {
        Log.d(TAG, "initializeStateCallback: ");
        mStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                Log.d(TAG, "onOpened: ");
                mCameraDevice = cameraDevice;
                createCameraPreview();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                Log.d(TAG, "onDisconnected: ");
                mCameraDevice.close();
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int error) {
                Log.d(TAG, "onError: ");
                mCameraDevice.close();
                mCameraDevice = null;
            }
        };
    }

    private void createCameraPreview() {
        Log.d(TAG, "createCameraPreview: ");
        try {
            SurfaceTexture texture = mCameraTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mImageDimension.getWidth(), mImageDimension.getHeight());
            Surface surface = new Surface(texture);
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession mCameraCaptureSession) {
                    //The camera is already closed
                    if (mCameraDevice == null) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    mCameraCaptureSessions = mCameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession mCameraCaptureSession) {
                    Toast.makeText(TakePhotoUsingCamera2ApiActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        Log.d(TAG, "updatePreview: ");
        if(null == mCameraDevice) {
            Log.d(TAG, "updatePreview error, return");
        }
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            mCameraCaptureSessions.setRepeatingRequest(mCaptureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        Log.d(TAG, "takePicture: ");
        if(null == mCameraDevice) {
            Log.d(TAG, "cameraDevice is null");
            return;
        }
        try {
            int imageSize[] = getJpegImageSize();
            int width = imageSize[0];
            int height = imageSize[1];
            
            mImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(mImageReader.getSurface());
            outputSurfaces.add(new Surface(mCameraTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            // create the image file
            File mediaFile = getOutputMediaFile();

            // image available listener
            ImageReader.OnImageAvailableListener readerListener = initializeImageAvailableListener(mediaFile);
            mImageReader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            
            // capture callback
            final CameraCaptureSession.CaptureCallback captureListener = initializeCaptureCallback(mediaFile);
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    
    private int[] getJpegImageSize() {
        int[] imageSize = new int[2];
        try {
            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
            android.util.Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            imageSize[0] = 640;
            imageSize[1] = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                imageSize[0] = jpegSizes[0].getWidth();
                imageSize[1] = jpegSizes[0].getHeight();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return imageSize;
    }
    
    private ImageReader.OnImageAvailableListener initializeImageAvailableListener(final File mediaFile) {
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = null;
                try {
                    image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.capacity()];
                    buffer.get(bytes);
                    save(bytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }
            }
            private void save(byte[] bytes) throws IOException {
                OutputStream output = null;
                try {
                    output = new FileOutputStream(mediaFile);
                    output.write(bytes);
                } finally {
                    if (null != output) {
                        output.close();
                    }
                }
            }
        };
        return readerListener;
    }
    
    private CameraCaptureSession.CaptureCallback initializeCaptureCallback(final File mediaFile) {
        // initialize callback
        CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                Log.d(TAG, "onCaptureCompleted: " + mediaFile.getAbsolutePath());

                closeCamera();

                // return to previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra(getString(R.string.data), mediaFile.getAbsolutePath());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };
        return captureListener;
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
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
