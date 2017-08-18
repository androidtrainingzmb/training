package tcd.training.com.trainingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.CommunicationBetweenActivitiesActivity;
import tcd.training.com.trainingproject.CustomView.CreateCustomViewActivity;
import tcd.training.com.trainingproject.ExternalHardware.ExternalHardwareTopicActivity;
import tcd.training.com.trainingproject.ExternalInternalIntent.ExternalInternalIntentActivity;
import tcd.training.com.trainingproject.IntentFlags.IntentFlagsActivity;
import tcd.training.com.trainingproject.Fragments.FragmentTopicActivity;
import tcd.training.com.trainingproject.ImagesProcessing.ImagesProcessingActivity;
import tcd.training.com.trainingproject.Networking.NetworkingActivity;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;
import tcd.training.com.trainingproject.Sensors.SensorTopicActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.ServiceTopicActivity;

public class MainActivity extends AppCompatActivity {

    private ListView mTopicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Activity starts with different flags", IntentFlagsActivity.class);
        topics.put("2. Difference between internal and external activity instantiation", ExternalInternalIntentActivity.class);
        topics.put("3. Create a custom view", CreateCustomViewActivity.class);
        topics.put("4. Persistent storage", PersistentStorageActivity.class);
        topics.put("5. Fragments", FragmentTopicActivity.class);
        topics.put("6. Communication between activities", CommunicationBetweenActivitiesActivity.class);
        topics.put("7. Services and Threads", ServiceTopicActivity.class);
        topics.put("8. Images processing", ImagesProcessingActivity.class);
        topics.put("9. Sensors", SensorTopicActivity.class);
        topics.put("10. External Hardware", ExternalHardwareTopicActivity.class);
        topics.put("11. Networking", NetworkingActivity.class);

        mTopicsListView = findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, topics.get(key));
                startActivity(intent);
            }
        });
    }
}
