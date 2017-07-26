package tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
    private BroadcastReceiver mMessageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flexible_ui_with_fragments);

        tappedIndicatorTextView = findViewById(R.id.tv_button_clicked_indicator);
        if (tappedIndicatorTextView != null) {
            tappedIndicatorTextView.setText(getString(R.string.send_data_to_activity_message));
        }

        // using local broadcast receiver
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int index = intent.getIntExtra(getString(R.string.integer_type), -1);
                String message = index == -1 ? getString(R.string.obtain_index_failed) : getString(R.string.send_using_local_broadcast_receiver);
                updateActivityWithFragmentData(index, message);
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(getString(R.string.send_data_to_activity_intent_name)));
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
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void passDataToActivity(int index) {
        updateActivityWithFragmentData(index, getString(R.string.send_using_interface));
    }

    public void updateIndicatorTextView(int index) {
        updateActivityWithFragmentData(index, getString(R.string.send_using_get_activity));
    }

    private void updateActivityWithFragmentData(int index, String message) {
        if (tappedIndicatorTextView != null) {
            tappedIndicatorTextView.setText("Button from fragment #" + index + " clicked");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
