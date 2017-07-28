package tcd.training.com.trainingproject.ServicesDemo.IntentService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.tech.IsoDep;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class IntentServiceDemoActivity extends AppCompatActivity {

    public static final String TAG = IntentServiceDemoActivity.class.getSimpleName();

    public static final int RESULT_RECEIVER_METHOD = 0;
    public static final int BROADCAST_RECEIVER_METHOD = 1;

    private TextView integerValueTextView;
    private int mInteger = 0;

    private MyResultReceiver mResultReceiver;
    private BroadcastReceiver mBroadcastReceiver;
    private int mCurrentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service_demo);

        integerValueTextView = findViewById(R.id.tv_integer_value);

        mCurrentMethod = getIntent().getIntExtra(getString(R.string.integer_type), RESULT_RECEIVER_METHOD);
        if (mCurrentMethod == BROADCAST_RECEIVER_METHOD) {
            ((RadioButton)findViewById(R.id.rb_broadcast_receiver)).setChecked(true);
        }

        setupServiceResultReceiver();
        launchService();
    }

    private void setupServiceResultReceiver() {
        // result receiver
        mResultReceiver = new MyResultReceiver(new Handler());
        mResultReceiver.setReceiver(new MyResultReceiver.Receiver() {
            @Override
            public void onReceivedResult(int resultCode, Bundle resultData) {
                if (resultCode == Activity.RESULT_OK) {
                    mInteger = resultData.getInt(getString(R.string.integer_type));
                    integerValueTextView.setText(String.valueOf(mInteger));
                }
            }
        });
        // broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mInteger = intent.getIntExtra(getString(R.string.integer_type), -1);
                integerValueTextView.setText(String.valueOf(mInteger));
            }
        };
    }

    // register and unregister broadcast receiver
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(IntentServiceWithBroadcastReceiver.INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    private void launchService() {
        Intent serviceIntent = new Intent();
        if (mCurrentMethod == RESULT_RECEIVER_METHOD) {
            serviceIntent.setClass(this, IntentServiceWithResultReceiver.class);
            serviceIntent.putExtra(getString(R.string.result_receiver), mResultReceiver);
        } else {
            serviceIntent.setClass(this, IntentServiceWithBroadcastReceiver.class);
        }
        serviceIntent.putExtra(getString(R.string.integer_type), mInteger);
        startService(serviceIntent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.rb_broadcast_receiver:
                    mCurrentMethod = BROADCAST_RECEIVER_METHOD;
                    break;
                case R.id.rb_result_receiver:
                    mCurrentMethod = RESULT_RECEIVER_METHOD;
                    break;
            }
            launchService();
        }
    }
}
