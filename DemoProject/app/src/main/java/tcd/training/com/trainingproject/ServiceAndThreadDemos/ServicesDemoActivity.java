package tcd.training.com.trainingproject.ServiceAndThreadDemos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.AsyncTaskDemo.AsyncTaskDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.BoundService.BindServiceWithBinderClassDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.BoundService.BindServiceWithMessengerDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.CustomService.CustomServiceDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.HandlerThreadDemo.HandlerThreadDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.IntentServiceDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.ThreadPoolExecutorDemo.ThreadPoolExecutorUsingRunnableDemoActivity;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.ThreadPoolExecutorDemo.ThreadPoolExecutorUsingCallableDemoActivity;

public class ServicesDemoActivity extends AppCompatActivity {

    private ListView topicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. IntentService with ResultReceiver", IntentServiceDemoActivity.class);
        topics.put("2. IntentService with LocalBroadcastManager", IntentServiceDemoActivity.class);
        topics.put("3. IntentService with EventBus", IntentServiceDemoActivity.class);
        topics.put("4. IntentService with RxJava", IntentServiceDemoActivity.class);
        topics.put("5. Custom services with HandlerThread", CustomServiceDemoActivity.class);
        topics.put("6. Bound service using extended Binder class", BindServiceWithBinderClassDemoActivity.class);
        topics.put("7. Bound service using Messenger", BindServiceWithMessengerDemoActivity.class);
        topics.put("8. AsyncTask demo", AsyncTaskDemoActivity.class);
        topics.put("9. HandlerThread demo", HandlerThreadDemoActivity.class);
        topics.put("10. ThreadPoolExecutor using Runnable demo", ThreadPoolExecutorUsingRunnableDemoActivity.class);
        topics.put("11. ThreadPoolExecutor using Callable demo", ThreadPoolExecutorUsingCallableDemoActivity.class);

        topicsListView = (ListView) findViewById(R.id.list_view);
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
                } else if (key.contains("EventBus")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceDemoActivity.EVENT_BUS_METHOD);
                } else if (key.contains("RxJava")) {
                    intent.putExtra(getString(R.string.integer_type), IntentServiceDemoActivity.RX_JAVA_METHOD);
                }
                startActivity(intent);
            }
        });
    }
}
