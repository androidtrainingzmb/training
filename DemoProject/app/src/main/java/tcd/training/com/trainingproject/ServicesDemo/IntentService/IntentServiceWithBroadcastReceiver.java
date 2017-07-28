package tcd.training.com.trainingproject.ServicesDemo.IntentService;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Calendar;

import tcd.training.com.trainingproject.R;


/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class IntentServiceWithBroadcastReceiver extends IntentService {

    public static final String TAG = IntentServiceDemoActivity.TAG;
    public static final String INTENT_ACTION = IntentServiceWithBroadcastReceiver.class.getName();

    private static int mInteger;

    public IntentServiceWithBroadcastReceiver() {
//        super(Resources.getSystem().getString(R.string.my_intent_service));
        super("IntentServiceWithBroadcastReceiver");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mInteger = intent.getIntExtra(getString(R.string.integer_type), -1) + 1;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent broadcastIntent = new Intent(INTENT_ACTION);
        broadcastIntent.putExtra(getString(R.string.integer_type), mInteger);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
