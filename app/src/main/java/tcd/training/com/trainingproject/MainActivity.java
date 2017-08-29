package tcd.training.com.trainingproject;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.StrictMode;
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
import tcd.training.com.trainingproject.ImageProcessing.ImageProcessingActivity;
import tcd.training.com.trainingproject.Networking.NetworkingActivity;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;
import tcd.training.com.trainingproject.Sensors.SensorTopicActivity;
import tcd.training.com.trainingproject.ServicesAndThreads.ServiceTopicActivity;

public class MainActivity extends AppCompatActivity implements ComponentCallbacks2 {

    private static final boolean DEVELOPER_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Activity starts with different flags", IntentFlagsActivity.class);
        topics.put("2. Difference between internal and external activity instantiation", ExternalInternalIntentActivity.class);
        topics.put("3. Create a custom view", CreateCustomViewActivity.class);
        topics.put("4. Persistent storage", PersistentStorageActivity.class);
        topics.put("5. Fragments", FragmentTopicActivity.class);
        topics.put("6. Communication between activities", CommunicationBetweenActivitiesActivity.class);
        topics.put("7. Services and Threads", ServiceTopicActivity.class);
        topics.put("8. Images processing", ImageProcessingActivity.class);
        topics.put("9. Sensors", SensorTopicActivity.class);
        topics.put("10. External Hardware", ExternalHardwareTopicActivity.class);
        topics.put("11. Networking", NetworkingActivity.class);

        ListView topicsListView = findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
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

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:

                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }
}
