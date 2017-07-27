package tcd.training.com.trainingproject.ServicesDemo.IntentService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
        Bundle bundle = intent.getBundleExtra(context.getString(R.string.data));
        Intent serviceIntent = new Intent(context, MyIntentService.class);
        serviceIntent.putExtra(context.getString(R.string.integer_type), bundle.getInt(context.getString(R.string.integer_type)));
        serviceIntent.putExtra(context.getString(R.string.result_receiver), bundle.getParcelable(context.getString(R.string.result_receiver)));
        context.startService(serviceIntent);
    }
}
