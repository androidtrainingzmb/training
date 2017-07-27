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
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.sql.ResultSet;
import java.util.Calendar;

import tcd.training.com.trainingproject.R;

import static android.content.ContentValues.TAG;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class MyIntentService extends IntentService {

    public static final String INTENT_ACTION = MyIntentService.class.getName();

    private int receiveMethod = -1;
    ResultReceiver receiver;

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
        receiver = intent.getParcelableExtra(getString(R.string.result_receiver));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String message = "This is a message from MyIntentService";
        if (receiveMethod == BackgroundTaskIntentServiceActivity.RESULT_RECEIVER_METHOD) {
            Bundle resultData = new Bundle();
            message += "using ResultReceiver";
            resultData.putString(getString(R.string.string_type), message);
            receiver.send(Activity.RESULT_OK, resultData);
        } else if (receiveMethod == BackgroundTaskIntentServiceActivity.BROADCAST_RECEIVER_METHOD) {
            Intent broadcastIntent = new Intent(INTENT_ACTION);
            message += "using LocalBroadcastReceiver";
            broadcastIntent.putExtra(getString(R.string.string_type), message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        }
        Log.e(TAG, "onHandleIntent: " + message);
        Log.e(TAG, "onHandleIntent: " + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
    }
}
