package tcd.training.com.trainingproject.ServicesAndThreads.IntentService.EventBus;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 01/08/2017.
 */

public class IntentServiceWithEventBus extends IntentService {

    public static final String TAG = IntentServiceWithEventBus.class.getSimpleName();

    private static int mInteger;

    public IntentServiceWithEventBus() {
        super("IntentServiceWithEventBus");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e(TAG, "onHandleIntent: ");
        mInteger = intent.getIntExtra(getString(R.string.integer_type), -1) + 1;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e(TAG, "onHandleIntent: ");
        EventBus.getDefault().post(new EventBusMessage(mInteger));
    }

}
