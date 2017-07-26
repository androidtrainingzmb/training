package tcd.training.com.trainingproject.ServicesDemo.IntentService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

import tcd.training.com.trainingproject.R;

public class BackgroundTaskIntentServiceActivity extends AppCompatActivity {

    public static final int RESULT_RECEIVER_METHOD = 0;
    public static final int BROADCAST_RECEIVER_METHOD = 1;

    private MyResultReceiver resultReceiver;
    private BroadcastReceiver broadcastReceiver;
    private int currentMethod = RESULT_RECEIVER_METHOD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_task_intent_service);

        setupServiceResultReceiver();
//        scheduleAlarm();
//        startIntentService(RESULT_RECEIVER_METHOD);
    }

    // for ResultReceiver method
    private void startIntentService() {
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra(getString(R.string.integer_type), currentMethod);
        if (currentMethod == RESULT_RECEIVER_METHOD) {
            intent.putExtra("receiver", resultReceiver);
        }
        startService(intent);
    }

    private void setupServiceResultReceiver() {
        // result receiver
        resultReceiver = new MyResultReceiver(new Handler());
        resultReceiver.setReceiver(new MyResultReceiver.Receiver() {
            @Override
            public void onReceivedResult(int resultCode, Bundle resultData) {
                if (resultCode == Activity.RESULT_OK) {
                    String message = resultData.getString(getString(R.string.string_type));
                    Toast.makeText(BackgroundTaskIntentServiceActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        // broadcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra(getString(R.string.string_type));
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    // for Broadcast Receiver method
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(MyIntentService.INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    // for alarm
    private void scheduleAlarm() {
        // prepare intent
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra(getString(R.string.integer_type), currentMethod);
        if (currentMethod == RESULT_RECEIVER_METHOD) {
            intent.putExtra("receiver", resultReceiver);
        }
        // schedule alarm
        PendingIntent operation = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(
                AlarmManager.RTC,
                Calendar.getInstance().getTimeInMillis(),
                1000L,
                operation
        );
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.rb_broadcast_receiver:
                    currentMethod = BROADCAST_RECEIVER_METHOD;
                    break;
                case R.id.rb_result_receiver:
                    currentMethod = RESULT_RECEIVER_METHOD;
                    break;
            }
            startIntentService();
        }
    }
}
