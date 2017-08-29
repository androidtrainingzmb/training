package tcd.training.com.trainingproject.Fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.Fragments.FlexibleUIWithFragments.FlexibleUIWithFragmentsActivity;
import tcd.training.com.trainingproject.Fragments.PagerTabStrip.PagerTabStripWithFragmentsActivity;
import tcd.training.com.trainingproject.Fragments.TabLayout.TabLayoutWithFragmentsActivity;
import tcd.training.com.trainingproject.R;

public class FragmentTopicActivity extends AppCompatActivity {

    public final static String[] mPageTitles = new String[]{"Page 1", "Page 2", "Page 3", "Page 4", "Page 5", "Page 6", "Page 7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. TabLayout with Fragments", TabLayoutWithFragmentsActivity.class);
        topics.put("2. PagerTabStrip with Fragments", PagerTabStripWithFragmentsActivity.class);
        topics.put("3. Flexible UI with Fragments", FlexibleUIWithFragmentsActivity.class);

        ListView fragmentTopicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(topics.keySet());
        fragmentTopicsListView.setAdapter(arrayAdapter);
        fragmentTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(FragmentTopicActivity.this, topics.get(key));
                startActivity(intent);
            }
        });
    }
}
