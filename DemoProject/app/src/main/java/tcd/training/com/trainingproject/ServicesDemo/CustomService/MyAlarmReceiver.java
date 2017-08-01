package tcd.training.com.trainingproject.ServicesDemo.CustomService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class MyAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MyCustomService.class);
        context.startService(serviceIntent);
    }
}
