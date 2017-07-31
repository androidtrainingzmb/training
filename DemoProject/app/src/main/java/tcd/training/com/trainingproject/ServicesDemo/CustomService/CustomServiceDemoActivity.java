package tcd.training.com.trainingproject.ServicesDemo.CustomService;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceWithBroadcastReceiver;

public class CustomServiceDemoActivity extends AppCompatActivity {

    private static int mInteger = 0;

    private TextView mIntegerValueTextView;
    private Button mStartAlarmButton;
    private Button mEndAlarmButton;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
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
        mStartAlarmButton = findViewById(R.id.btn_start_alarm);
        mEndAlarmButton = findViewById(R.id.btn_end_alarm);

        mStartAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAlarm();
                Toast.makeText(CustomServiceDemoActivity.this, "Alarm started", Toast.LENGTH_SHORT).show();
            }
        });
        mEndAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endAlarm();
                Toast.makeText(CustomServiceDemoActivity.this, "Alarm ended", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startCustomService() {
        Intent intent = new Intent(this, MyCustomService.class);
        startService(intent);
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
}
