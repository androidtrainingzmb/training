package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingParcelableInterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.CommunicationBetweenActivitiesActivity;
import tcd.training.com.trainingproject.R;

public class ReceiveObjectWithParcelableInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_intent_data);

        ParcelableObject object = getIntent().getParcelableExtra(getString(R.string.data));
        CommunicationBetweenActivitiesActivity.inflateLayout(findViewById(android.R.id.content),
                object.getInteger(),
                object.isBoolean(),
                object.getDouble(),
                object.getString(),
                object.getArrayList(),
                object.getHashMap());
    }
}
