package tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tcd.training.com.trainingproject.R;

public class FlexibleUIWithFragmentsActivity extends AppCompatActivity
        implements DetailsFragment.ICommunicate {

    private TextView buttonClickedIndicatorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible_ui_with_fragments);

        buttonClickedIndicatorTextView = findViewById(R.id.tv_button_clicked_indicator);
        if (buttonClickedIndicatorTextView != null) {
            buttonClickedIndicatorTextView.setText("Tap the button in any fragment screen to send data to this activity.\nThis demonstrates the sending data from fragment to activity process");
        }
    }

    @Override
    public void passDataToActivity(int index) {
        if (buttonClickedIndicatorTextView != null) {
            buttonClickedIndicatorTextView.setText("Button from fragment #" + index + " clicked");
        }
    }
}
