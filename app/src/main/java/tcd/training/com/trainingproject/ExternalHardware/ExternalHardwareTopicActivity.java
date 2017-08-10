package tcd.training.com.trainingproject.ExternalHardware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.ExternalHardware.AudioPlayer.AudioPlayerActivity;
import tcd.training.com.trainingproject.R;

public class ExternalHardwareTopicActivity extends AppCompatActivity {

    private ListView mTopicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. Audio playback with MediaPlayer", AudioPlayerActivity.class);
        topics.put("2. Audio playback with ExoPlayer", AudioPlayerActivity.class);

        mTopicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ExternalHardwareTopicActivity.this, topics.get(key));
                if (key.contains("MediaPlayer")) {
                    intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_MEDIA_PLAYER_METHOD);
                } else if (key.contains("ExoPlayer")) {
                    intent.putExtra(getString(R.string.integer_type), AudioPlayerActivity.USING_EXO_PLAYER_METHOD);
                }
                startActivity(intent);
            }
        });
    }
}
