package tcd.training.com.trainingproject.ServicesAndThreads.CustomService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

import static tcd.training.com.trainingproject.ServicesAndThreads.CustomService.MyCustomService.NOTIFICATION_EXTRA;
import static tcd.training.com.trainingproject.ServicesAndThreads.CustomService.MyCustomService.NOTIFICATION_OFF;
import static tcd.training.com.trainingproject.ServicesAndThreads.CustomService.MyCustomService.NOTIFICATION_ON;

public class CustomServiceActivity extends AppCompatActivity {

    private static int mInteger = 0;

    private TextView mIntegerValueTextView;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mInteger++;
            mIntegerValueTextView.setText(String.valueOf(mInteger));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service_demo);

        mIntegerValueTextView = findViewById(R.id.tv_integer_value);

        Switch toggleAlarmSwitch = findViewById(R.id.sw_toggle_alarm);
        toggleAlarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    startAlarm();
                    Toast.makeText(CustomServiceActivity.this, "Alarm started", Toast.LENGTH_SHORT).show();
                } else {
                    endAlarm();
                    Toast.makeText(CustomServiceActivity.this, "Alarm ended", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch toggleNotificationIconSwitch = findViewById(R.id.sw_toggle_notification_icon);
        toggleNotificationIconSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Intent intent = new Intent(CustomServiceActivity.this, MyCustomService.class);
                if (isChecked) {
                    intent.putExtra(NOTIFICATION_EXTRA, NOTIFICATION_ON);
                } else {
                    intent.putExtra(NOTIFICATION_EXTRA, NOTIFICATION_OFF);
                }
                startService(intent);
            }
        });
    }

    // for alarm
    private void startAlarm() {
        // prepare intent
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // schedule alarm
        PendingIntent operation = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        long interval = 15000L;
        alarm.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                interval,
                operation
        );
    }

    private void endAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(operation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(MyCustomService.INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MyCustomService.class));
    }
}
