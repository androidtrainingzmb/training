package tcd.training.com.trainingproject.Sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = PedometerActivity.class.getSimpleName();

    private TextView mNumberOfStepsTextView;

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        initializeUiComponents();

        initializeSensorComponents();
    }

    private void initializeUiComponents() {
        mNumberOfStepsTextView = findViewById(R.id.tv_number_of_steps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void initializeSensorComponents() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        initializeStepCounterSensor();

        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_UI);
    }

    private void initializeStepCounterSensor() {
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mStepCounterSensor == null) {
            Log.e(TAG, "Proximity sensor not available.");
            Toast.makeText(getApplicationContext(), getString(R.string.step_counter_not_available), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mNumberOfStepsTextView.setText(String.valueOf(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
