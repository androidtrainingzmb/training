package tcd.training.com.trainingproject.ServicesDemo.CustomService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceDemoActivity;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceWithBroadcastReceiver;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceWithResultReceiver;

import static android.content.ContentValues.TAG;

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
