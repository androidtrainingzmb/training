package tcd.training.com.trainingproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String[] topics = new String[] {"Activity starts with different flags",
//                                        "Difference between internal and external activity instantiation"};
        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Activity starts with different flags", DifferentFlagsTopicActivity1.class);
        topics.put("2. Difference between internal and external activity instantiation", ExternalInternalIntentActivity.class);

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
