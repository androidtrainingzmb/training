package tcd.training.com.trainingproject.ServicesDemo.IntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

import tcd.training.com.trainingproject.R;

import static android.content.ContentValues.TAG;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class MyAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(intent);
        Log.e(TAG, "onReceive: ");
        Log.e(TAG, "onReceive: " + String.valueOf(serviceIntent.getParcelableExtra("receiver") == null));
        serviceIntent.setClass(context, MyIntentService.class);
        context.startService(serviceIntent);
    }
}
