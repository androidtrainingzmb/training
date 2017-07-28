package tcd.training.com.trainingproject.ServicesDemo.CustomService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcd.training.com.trainingproject.R;

public class CustomServiceDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service_demo);

        startAlarm();
        startCustomService();
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
}
