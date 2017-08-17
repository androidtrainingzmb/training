package tcd.training.com.trainingproject.Sensors;

import android.graphics.Camera;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class RotationVectorSensor extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = RotationVectorSensor.class.getSimpleName();

    private ImageView mImageView;
    private TextView mRotationVectorSensorTextView;

    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;

    private float mCurrentDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_scope);

        initializeUiComponents();
        initializeSensorComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initializeUiComponents() {
        mImageView = findViewById(R.id.iv_image_demo);

        mRotationVectorSensorTextView = findViewById(R.id.tv_sensor_explanation);
        mRotationVectorSensorTextView.setText(getString(R.string.rotation_vector_sensor_explanation));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void initializeSensorComponents() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeRotationVectorSensor();

        mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initializeRotationVectorSensor() {
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (mRotationVectorSensor == null) {
            Log.e(TAG, "RotationVectorSensor not available.");
            Toast.makeText(getApplicationContext(), getString(R.string.gyroscope_not_available), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(
                rotationMatrix, sensorEvent.values);

        // Remap coordinate system
        float[] remappedRotationMatrix = new float[16];
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);

        // Convert to orientations
        float[] orientations = new float[3];
        SensorManager.getOrientation(remappedRotationMatrix, orientations);

        for(int i = 0; i < 3; i++) {
            orientations[i] = (float)(Math.toDegrees(orientations[i]));
        }

        rotateImage(orientations[2]);
    }

    private void rotateImage(float angle) {

        RotateAnimation animation = new RotateAnimation(
                mCurrentDegree,
                -angle,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(250);
        animation.setFillAfter(true);

        mImageView.startAnimation(animation);

        mCurrentDegree = -angle;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
