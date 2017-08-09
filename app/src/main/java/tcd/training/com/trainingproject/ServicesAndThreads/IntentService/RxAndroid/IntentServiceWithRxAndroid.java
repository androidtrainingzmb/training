package tcd.training.com.trainingproject.ServicesAndThreads.IntentService.RxAndroid;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesAndThreads.IntentService.EventBus.EventBusMessage;

/**
 * Created by cpu10661-local on 01/08/2017.
 */

public class IntentServiceWithRxAndroid extends IntentService {

    public static final String TAG = IntentServiceWithRxAndroid.class.getSimpleName();

    private static int mInteger;

    public IntentServiceWithRxAndroid() {
        super("IntentServiceWithRxAndroid");
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
        RxBus.publish(new EventBusMessage(mInteger));
    }
}
