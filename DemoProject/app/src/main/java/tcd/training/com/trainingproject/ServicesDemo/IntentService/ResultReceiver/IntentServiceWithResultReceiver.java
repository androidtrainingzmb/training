package tcd.training.com.trainingproject.ServicesDemo.IntentService.ResultReceiver;


import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.IntentServiceDemoActivity;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class IntentServiceWithResultReceiver extends IntentService {

    public static final String INTENT_ACTION = IntentServiceWithResultReceiver.class.getName();
    public static final String TAG = IntentServiceDemoActivity.TAG;

    private ResultReceiver mReceiver;
    private int mInteger;

    public IntentServiceWithResultReceiver() {
//        super(Resources.getSystem().getString(R.string.my_intent_service));
        super("IntentServiceWithResultReceiver");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mInteger = intent.getIntExtra(getString(R.string.integer_type), -1) + 1;
        mReceiver = intent.getParcelableExtra(getString(R.string.result_receiver));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle resultData = new Bundle();
        resultData.putInt(getString(R.string.integer_type), mInteger);
        mReceiver.send(Activity.RESULT_OK, resultData);
    }
}
