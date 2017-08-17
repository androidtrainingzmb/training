package tcd.training.com.trainingproject.Sensors;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class GyroscopeActivity extends AppCompatActivity implements  SensorEventListener{

    private static final String TAG = GyroscopeActivity.class.getSimpleName();

    private ImageView mImageView;
    private TextView mGyroscopeExplanationTextView;

    private Matrix mMatrix;
    private float MAX_ROTATE_ANGLE = 20;
    private float mCurrentAngle = 0f;
    int mImageHeight, mImageWidth;
    int mScreenHeight, mScreenWidth;

    private SensorManager mSensorManager;
    private Sensor mGyroscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_scope);

        initializeUiComponents();

        initializeSizeComponents();

        initializeSensorComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void initializeSizeComponents() {
        mMatrix = new Matrix();

        mImageWidth = mImageView.getDrawable().getIntrinsicWidth();
        mImageHeight = mImageView.getDrawable().getIntrinsicHeight();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;

        RectF drawableRect = new RectF(0, 0, mImageWidth, mImageHeight);
        RectF viewRect = new RectF(0, 0, mScreenWidth, mScreenHeight);
        mMatrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
    }

    private void initializeUiComponents() {
        mImageView = findViewById(R.id.iv_image_demo);

        mGyroscopeExplanationTextView = findViewById(R.id.tv_sensor_explanation);
        mGyroscopeExplanationTextView.setText(getString(R.string.gyroscope_explanation));
    }

    private void initializeSensorComponents() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeGyroscope();

        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    private void initializeGyroscope() {
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyroscope == null) {
            Log.e(TAG, "Proximity sensor not available.");
            Toast.makeText(getApplicationContext(), getString(R.string.gyroscope_not_available), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void rotateImage(float rotateAngle) {
        if (Math.abs(mCurrentAngle + rotateAngle) > MAX_ROTATE_ANGLE) {
            return;
        }

        mCurrentAngle += rotateAngle;

        Camera camera = new Camera();
        camera.save();
        camera.rotateY(mCurrentAngle);
        camera.getMatrix(mMatrix);
        camera.restore();

        mMatrix.preTranslate(-mScreenWidth / 2, 0);
        mMatrix.postTranslate(mScreenWidth / 2, 0);

        mImageView.setImageMatrix(mMatrix);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float axisY = sensorEvent.values[1];
        if (axisY > 0.25f) {
            rotateImage(1);
        } else if (axisY < -0.25f) {
            rotateImage(-1);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
