package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingOnlyIntent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.CommunicationBetweenActivitiesActivity;
import tcd.training.com.trainingproject.R;

public class ReceiveIntentDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_intent_data);

        if (getIntent() != null) {
            Intent intent = getIntent();
            int intVal = intent.getIntExtra(getString(R.string.integer_type), -1);
            double doubleVal = intent.getDoubleExtra(getString(R.string.double_type), -1);
            String stringVal = intent.getStringExtra(getString(R.string.string_type));
            boolean boolVal = intent.getBooleanExtra(getString(R.string.boolean_type), false);
            ArrayList<String> arrayList = intent.getStringArrayListExtra(getString(R.string.array_list_type));
            HashMap<String, String> hashMap = (HashMap<String, String>) intent.getSerializableExtra(getString(R.string.hash_map_type));

            CommunicationBetweenActivitiesActivity.inflateLayout(findViewById(android.R.id.content), intVal, boolVal, doubleVal, stringVal, arrayList, hashMap);
        }
    }
}
