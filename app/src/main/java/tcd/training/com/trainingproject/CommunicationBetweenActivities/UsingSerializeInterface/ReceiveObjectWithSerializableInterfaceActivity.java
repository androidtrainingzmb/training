package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingSerializeInterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.CommunicationBetweenActivitiesActivity;
import tcd.training.com.trainingproject.R;

public class ReceiveObjectWithSerializableInterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_intent_data);

        SerializableObject object = (SerializableObject) getIntent().getSerializableExtra(getString(R.string.data));
        CommunicationBetweenActivitiesActivity.inflateLayout(findViewById(android.R.id.content),
                object.getmInteger(),
                object.ismBoolean(),
                object.getmDouble(),
                object.getmString(),
                object.getmArrayList(),
                object.getmHashMap());
    }
}
