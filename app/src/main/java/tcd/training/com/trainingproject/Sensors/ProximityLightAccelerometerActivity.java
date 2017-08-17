package tcd.training.com.trainingproject.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import tcd.training.com.trainingproject.R;

public class ProximityLightAccelerometerActivity extends AppCompatActivity {

    private static final String TAG = ProximityLightAccelerometerActivity.class.getSimpleName();
    private static final int NORMAL_LIGHT_MAGNITUDE = 50;

    private TextView mProximityDistanceTextView;
    private TextView mAccelerometerTextView;
    private TextView mAccelerometerXTextView;
    private TextView mAccelerometerYTextView;
    private TextView mAccelerometerZTextView;
    private TextView mLightMagnitudeTextView;

    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private Sensor mAccelerometer;
    private Sensor mLightSensor;
    private SensorEventListener mProximityAccelerometerEventListener;
    private SensorEventListener mLightEventListener;

    private boolean mIsSomethingNearby = false;
    private boolean mIsFacingDown = false;
    private AudioManager mAudioManager;
    private int mLastRingerMode;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);

        initializeUiComponents();
        initializeSensorComponents();

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mLastRingerMode = mAudioManager.getRingerMode();
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    private void initializeUiComponents() {
        mProximityDistanceTextView = findViewById(R.id.tv_proximity_distance);

        mAccelerometerTextView = findViewById(R.id.tv_accelerometer);
        mAccelerometerXTextView = findViewById(R.id.tv_accelerometer_x);
        mAccelerometerYTextView = findViewById(R.id.tv_accelerometer_y);
        mAccelerometerZTextView = findViewById(R.id.tv_accelerometer_z);

        mLightMagnitudeTextView = findViewById(R.id.tv_light_magnitude);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mProximityAccelerometerEventListener);
        mSensorManager.unregisterListener(mLightEventListener);
    }

    private void initializeSensorComponents() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeSensorEventListener();

        initializeProximitySensor();
        initializeAccelerometer();
        initializeLightSensor();

        registerSensorListener();
    }

    private void registerSensorListener() {
        int samplingPeriodUs = SensorManager.SENSOR_DELAY_UI;
        if (mProximitySensor != null) {
            mSensorManager.registerListener(mProximityAccelerometerEventListener, mProximitySensor, samplingPeriodUs);
        }
        if (mAccelerometer != null) {
            mSensorManager.registerListener(mProximityAccelerometerEventListener, mAccelerometer, samplingPeriodUs);
        }
        if (mLightSensor != null) {
            mSensorManager.registerListener(mLightEventListener, mLightSensor, samplingPeriodUs);
        }
    }

    private void initializeSensorEventListener() {
        mProximityAccelerometerEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                
                    switch (sensorEvent.sensor.getType()) {

                        case Sensor.TYPE_PROXIMITY:
                            float range = sensorEvent.values[0];
                            float maximumRange = mProximitySensor.getMaximumRange();

                            if (range < maximumRange) {
                                mProximityDistanceTextView.setText(String.valueOf(range));
                                mIsSomethingNearby = true;
                            } else {
                                mProximityDistanceTextView.setText(getString(R.string.nothing_nearby));
                                mIsSomethingNearby = false;
                            }

                            break;

                        case Sensor.TYPE_ACCELEROMETER:
                            float x = sensorEvent.values[0];
                            float y = sensorEvent.values[1];
                            float z = sensorEvent.values[2];

                            mAccelerometerXTextView.setText(String.valueOf(x));
                            mAccelerometerYTextView.setText(String.valueOf(y));
                            mAccelerometerZTextView.setText(String.valueOf(z));

                            if (z < -9.0) {
                                mIsFacingDown = true;
                                mAccelerometerTextView.setText(getString(R.string.screen_face_down));
                            } else {
                                mIsFacingDown = false;
                                if (z > 9.0) {
                                    mAccelerometerTextView.setText(getString(R.string.screen_face_up));
                                } else {
                                    mAccelerometerTextView.setText(getString(R.string.phone_stand_up));
                                }
                            }
                            break;
                    }

                    if (mIsFacingDown && mIsSomethingNearby) {
                        if (mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                            mLastRingerMode = mAudioManager.getRingerMode();
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            mVibrator.vibrate(150);
                            Log.d(TAG, "onSensorChanged: vibrate");
                        }
                    } else if (mAudioManager.getRingerMode() != mLastRingerMode) {
                        mAudioManager.setRingerMode(mLastRingerMode);
                        Log.d(TAG, "onSensorChanged: " + (mIsFacingDown ? "nothing is nearby" : "Facing up"));
                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        mLightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                try {
                    float lightMagnitude = sensorEvent.values[0];
                    mLightMagnitudeTextView.setText(String.valueOf(lightMagnitude));

                    int brightnessMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
                    if (brightnessMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    }
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                    layoutParams.screenBrightness = lightMagnitude / NORMAL_LIGHT_MAGNITUDE;
                    getWindow().setAttributes(layoutParams);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void initializeProximitySensor() {
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProximitySensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            mProximityDistanceTextView.setText(getString(R.string.sensor_not_available));
        }
    }

    private void initializeAccelerometer() {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometer == null) {
            Log.e(TAG, "Accelerometer not available.");
            mAccelerometerTextView = findViewById(R.id.tv_accelerometer);
            mAccelerometerTextView.setText(getString(R.string.sensor_not_available));
        }
    }

    private void initializeLightSensor() {
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLightSensor == null) {
            Log.e(TAG, "Light sensor not available.");
            mLightMagnitudeTextView.setText(getString(R.string.sensor_not_available));
        }
    }
}
