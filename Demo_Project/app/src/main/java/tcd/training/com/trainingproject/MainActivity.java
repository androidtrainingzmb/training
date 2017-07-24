package tcd.training.com.trainingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.CommunicateBetweenActivities.CommunicateBetweenActivitiesActivity;
import tcd.training.com.trainingproject.CustomView.CreateCustomViewActivity;
import tcd.training.com.trainingproject.DifferentFlagsTopic.DifferentFlagsTopicActivity1;
import tcd.training.com.trainingproject.FragmentsDemo.FragmentsDemoActivity;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;

public class MainActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Activity starts with different flags", DifferentFlagsTopicActivity1.class);
        topics.put("2. Difference between internal and external activity instantiation", ExternalInternalIntentActivity.class);
        topics.put("3. Create a custom view", CreateCustomViewActivity.class);
        topics.put("4. Persistent storage demo", PersistentStorageActivity.class);
        topics.put("5. Fragments demo", FragmentsDemoActivity.class);
        topics.put("6. Communicate between activities", CommunicateBetweenActivitiesActivity.class);

        topicsListView = (ListView) findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        topicsListView.setAdapter(arrayAdapter);
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, topics.get(key));
                startActivity(intent);
            }
        });
    }
}
