package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingGson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.CommunicationBetweenActivitiesActivity;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.DemoObject;
import tcd.training.com.trainingproject.R;

public class ReceiveGsonObjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_intent_data);

        // get the object
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra(getString(R.string.data));
        DemoObject object = gson.fromJson(strObj, DemoObject.class);

        // display its info
        CommunicationBetweenActivitiesActivity.inflateLayout(findViewById(android.R.id.content),
                object.getmInteger(),
                object.ismBoolean(),
                object.getmDouble(),
                object.getmString(),
                object.getmArrayList(),
                object.getmHashMap());
    }
}
