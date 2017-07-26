package tcd.training.com.trainingproject.ServicesDemo.IntentService;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.nio.channels.InterruptedByTimeoutException;
import java.sql.ResultSet;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class MyIntentService extends IntentService {

    public static final String INTENT_ACTION = MyIntentService.class.getName();

    private int receiveMethod = -1;

    public MyIntentService() {
//        super(Resources.getSystem().getString(R.string.my_intent_service));
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        receiveMethod = intent.getIntExtra(getString(R.string.integer_type), BackgroundTaskIntentServiceActivity.RESULT_RECEIVER_METHOD);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String message = "This is a message from MyIntentService";
        if (receiveMethod == BackgroundTaskIntentServiceActivity.RESULT_RECEIVER_METHOD) {
            ResultReceiver receiver = intent.getParcelableExtra("receiver");
            Bundle resultData = new Bundle();
            resultData.putString(getString(R.string.string_type), message + " using ResultReceiver");
            receiver.send(Activity.RESULT_OK, resultData);
        } else if (receiveMethod == BackgroundTaskIntentServiceActivity.BROADCAST_RECEIVER_METHOD) {
            Intent broadcastIntent = new Intent(INTENT_ACTION);
            broadcastIntent.putExtra(getString(R.string.string_type), message + " using LocalBroadcastReceiver");
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
    }
}
