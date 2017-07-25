package tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class FlexibleUIWithFragmentsActivity extends AppCompatActivity
        implements DetailsFragment.ICommunicate {

    private TextView tappedIndicatorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible_ui_with_fragments);

        tappedIndicatorTextView = findViewById(R.id.tv_button_clicked_indicator);
        if (tappedIndicatorTextView != null) {
            tappedIndicatorTextView.setText(getString(R.string.send_data_to_activity_message));
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            TitlesFragment titlesFragment = (TitlesFragment) fragmentManager.findFragmentById(R.id.frag_titles);
            if (titlesFragment != null) {
                titlesFragment.updateSelectionState();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void passDataToActivity(int index) {
        if (tappedIndicatorTextView != null) {
            tappedIndicatorTextView.setText("Button from fragment #" + index + " clicked");
            Toast.makeText(this, getString(R.string.send_using_interface), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateIndicatorTextView(int index) {
        if (tappedIndicatorTextView != null) {
            tappedIndicatorTextView.setText("Button from fragment #" + index + " clicked");
            Toast.makeText(this, getString(R.string.send_using_get_activity), Toast.LENGTH_SHORT).show();
        }
    }
}
