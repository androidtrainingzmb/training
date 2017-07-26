package tcd.training.com.trainingproject.ServicesDemo;

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
import tcd.training.com.trainingproject.ExternalInternalIntentActivity;
import tcd.training.com.trainingproject.FragmentsDemo.FragmentsDemoActivity;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;
import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.BackgroundTaskIntentServiceActivity;

public class ServicesDemoActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_demo);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Background services with IntentService", BackgroundTaskIntentServiceActivity.class);
        topics.put("2. Defining custom services", ExternalInternalIntentActivity.class);
        topics.put("3. Registering the Service", CreateCustomViewActivity.class);
        topics.put("4. Threading within the Service", PersistentStorageActivity.class);
        topics.put("5. Running Tasks in the Service", FragmentsDemoActivity.class);
        topics.put("6. Communicating with the Service", CommunicationBetweenActivitiesActivity.class);
        topics.put("7. Stopping the Service", this.getClass());
        topics.put("8. Bound Services", this.getClass());

        topicsListView = (ListView) findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        topicsListView.setAdapter(arrayAdapter);
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ServicesDemoActivity.this, topics.get(key));
                startActivity(intent);
            }
        });
    }
}
