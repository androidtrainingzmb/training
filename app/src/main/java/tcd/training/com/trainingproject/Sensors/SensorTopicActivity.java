package tcd.training.com.trainingproject.Sensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.R;

public class SensorTopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Proximity sensor, accelerometer, light sensor", ProximityLightAccelerometerActivity.class);
        topics.put("2. Compass with magnetic field sensor and accelerometer", CompassWithMagneticFieldSensorActivity.class);
        topics.put("3. Gyroscope", GyroscopeActivity.class);
        topics.put("4. Rotation vector sensor", RotationVectorSensor.class);
        topics.put("5. Pedometer using Step counter sensor", PedometerActivity.class);

        ListView topicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        topicsListView.setAdapter(arrayAdapter);
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(SensorTopicActivity.this, topics.get(key));
                startActivity(intent);
            }
        });
    }
}
