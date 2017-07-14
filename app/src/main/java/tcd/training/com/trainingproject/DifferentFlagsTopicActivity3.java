package tcd.training.com.trainingproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DifferentFlagsTopicActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_flags_topic3);

        getSupportActionBar().setTitle(getLocalClassName());
    }
}
