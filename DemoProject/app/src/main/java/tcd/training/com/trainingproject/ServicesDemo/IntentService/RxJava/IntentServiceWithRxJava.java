package tcd.training.com.trainingproject.ServicesDemo.IntentService.RxJava;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.EventBus.EventBusMessage;
import tcd.training.com.trainingproject.ServicesDemo.IntentService.EventBus.IntentServiceWithEventBus;

/**
 * Created by cpu10661-local on 01/08/2017.
 */

public class IntentServiceWithRxJava extends IntentService {

    public static final String TAG = IntentServiceWithRxJava.class.getSimpleName();

    private static int mInteger;

    public IntentServiceWithRxJava() {
        super("IntentServiceWithRxJava");
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
