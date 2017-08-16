package tcd.training.com.trainingproject.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class CompassWithMagneticFieldSensorActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = CompassWithMagneticFieldSensorActivity.class.getSimpleName();

    private ImageView mNorthDirectionImageView;

    private SensorManager mSensorManager;
    private Sensor mMagneticFieldSensor;
    private Sensor mAccelerometer;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_with_magnetic_field_sensor);

        initializeUiComponents();
        initializeSensorComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensorListener();
    }

    private void initializeUiComponents() {
        mNorthDirectionImageView = findViewById(R.id.iv_north_direction);
    }

    private void initializeSensorComponents() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeAccelerometer();
        initializeMagneticFieldSensor();

        registerSensorListener();
    }

    private void registerSensorListener() {
        int samplingPeriodUs = SensorManager.SENSOR_DELAY_UI;
        mSensorManager.registerListener(this, mAccelerometer, samplingPeriodUs);
        mSensorManager.registerListener(this, mMagneticFieldSensor, samplingPeriodUs);
    }

    private void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

    private void initializeAccelerometer() {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null) {
            Log.e(TAG, "Accelerometer not available.");
            Toast.makeText(getApplicationContext(), getString(R.string.sensor_not_available), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeMagneticFieldSensor() {
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagneticFieldSensor == null) {
            Log.e(TAG, "Magnetic field sensor not available.");
            Toast.makeText(getApplicationContext(), getString(R.string.sensor_not_available), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(sensorEvent.values, 0, mLastAccelerometer, 0, sensorEvent.values.length);
                mLastAccelerometerSet = true;
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(sensorEvent.values, 0, mLastMagnetometer, 0, sensorEvent.values.length);
                mLastMagnetometerSet = true;
                break;
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            mNorthDirectionImageView.startAnimation(ra);
            mCurrentDegree = -azimuthInDegrees;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
