package tcd.training.com.trainingproject.ServicesAndThreads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesAndThreads.AsyncTask.AsyncTaskActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.BoundService.BindServiceWithBinderClassActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.BoundService.BindServiceWithMessengerActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.CustomService.CustomServiceActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.HandlerThread.HandlerThreadActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.IntentService.IntentServiceActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.ThreadPoolExecutor.ThreadPoolExecutorUsingRunnableActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.ThreadPoolExecutor.ThreadPoolExecutorUsingCallableActivity;

public class ServiceTopicActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. IntentService with ResultReceiver", IntentServiceActivity.class);
        topics.put("2. IntentService with LocalBroadcastManager", IntentServiceActivity.class);
        topics.put("3. IntentService with EventBus", IntentServiceActivity.class);
        topics.put("4. IntentService with RxJava", IntentServiceActivity.class);
        topics.put("5. Custom services with HandlerThread", CustomServiceActivity.class);
        topics.put("6. Bound service using extended Binder class", BindServiceWithBinderClassActivity.class);
        topics.put("7. Bound service using Messenger", BindServiceWithMessengerActivity.class);
        topics.put("8. AsyncTask demo", AsyncTaskActivity.class);
        topics.put("9. HandlerThread demo", HandlerThreadActivity.class);
        topics.put("10. ThreadPoolExecutor using Runnable demo", ThreadPoolExecutorUsingRunnableActivity.class);
        topics.put("11. ThreadPoolExecutor using Callable demo", ThreadPoolExecutorUsingCallableActivity.class);

        topicsListView = (ListView) findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        topicsListView.setAdapter(arrayAdapter);
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ServiceTopicActivity.this, topics.get(key));
                if (key.contains("ResultReceiver")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceActivity.RESULT_RECEIVER_METHOD);
                } else if (key.contains("LocalBroadcastManager")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceActivity.BROADCAST_RECEIVER_METHOD);
                } else if (key.contains("EventBus")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceActivity.EVENT_BUS_METHOD);
                } else if (key.contains("RxJava")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceActivity.RX_JAVA_METHOD);
                }
                startActivity(intent);
            }
        });
    }
}
