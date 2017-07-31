package tcd.training.com.trainingproject.ServicesDemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.AsyncTaskDemo.AsyncTaskDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.BoundService.BindServiceWithBinderClassDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.BoundService.BindServiceWithMessengerDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.CustomService.CustomServiceDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.ThreadPoolExecutorDemo.ThreadPoolExecutorDemoActivity;

public class ServicesDemoActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_demo);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. IntentService with ResultReceiver", IntentServiceDemoActivity.class);
        topics.put("2. IntentService with LocalBroadcastManager", IntentServiceDemoActivity.class);
        topics.put("3. Defining custom services", CustomServiceDemoActivity.class);
        topics.put("4. Bind service by extending the Binder class", BindServiceWithBinderClassDemoActivity.class);
        topics.put("5. Bind service by using Messenger", BindServiceWithMessengerDemoActivity.class);
        topics.put("6. AsyncTask demo", AsyncTaskDemoActivity.class);
        topics.put("7. ThreadPoolExecutor demo", ThreadPoolExecutorDemoActivity.class);

        topicsListView = (ListView) findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        topicsListView.setAdapter(arrayAdapter);
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ServicesDemoActivity.this, topics.get(key));
                if (key.contains("ResultReceiver")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceDemoActivity.RESULT_RECEIVER_METHOD);
                } else if (key.contains("LocalBroadcastManager")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceDemoActivity.BROADCAST_RECEIVER_METHOD);
                }
                startActivity(intent);
            }
        });
    }
}
