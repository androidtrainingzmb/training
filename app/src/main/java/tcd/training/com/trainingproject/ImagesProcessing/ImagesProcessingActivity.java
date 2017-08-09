package tcd.training.com.trainingproject.ImagesProcessing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import tcd.training.com.trainingproject.ImagesProcessing.CacheImages.CacheImageUsingDiskActivity;
import tcd.training.com.trainingproject.ImagesProcessing.CacheImages.CacheImageUsingExternalLibraryActivity;
import tcd.training.com.trainingproject.ImagesProcessing.CacheImages.CacheImageUsingMemoryActivity;
import tcd.training.com.trainingproject.ImagesProcessing.LoadImages.LoadImageFromSdCardActivity;
import tcd.training.com.trainingproject.ImagesProcessing.LoadImages.LoadImageFromUrlActivity;
import tcd.training.com.trainingproject.R;

public class ImagesProcessingActivity extends AppCompatActivity {

    private ListView mTopicsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);

        final LinkedHashMap<String, Class> topics = new LinkedHashMap<>();
        topics.put("1. ImageView scaleType demo", ScaleTypeActivity.class);
        topics.put("2. Load images from SD card demo", LoadImageFromSdCardActivity.class);
        topics.put("3. Load images from Url demo", LoadImageFromUrlActivity.class);
        topics.put("4. Cache images using Memory demo", CacheImageUsingMemoryActivity.class);
        topics.put("5. Cache images using Disk demo", CacheImageUsingDiskActivity.class);
        topics.put("6. Cache images using Glide demo", CacheImageUsingExternalLibraryActivity.class);
        topics.put("7. Cache images using Picasso demo", CacheImageUsingExternalLibraryActivity.class);

        mTopicsListView = findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, topics.keySet().toArray(new String[0]));
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ImagesProcessingActivity.this, topics.get(key));
                if (key.contains("Glide")) {
                    intent.putExtra(getString(R.string.integer_type), CacheImageUsingExternalLibraryActivity.GLIDE_METHOD);
                } else if (key.contains("Picasso")) {
                    intent.putExtra(getString(R.string.integer_type), CacheImageUsingExternalLibraryActivity.PICASSO_METHOD);
                }
                startActivity(intent);
            }
        });
    }
}
