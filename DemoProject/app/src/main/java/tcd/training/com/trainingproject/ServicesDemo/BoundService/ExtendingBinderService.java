package tcd.training.com.trainingproject.ServicesDemo.BoundService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 28/07/2017.
 */

public class ExtendingBinderService extends Service {

    public static final String INTENT_ACTION = ExtendingBinderService.class.getSimpleName();

    private volatile HandlerThread mHandlerThread;
    private ServiceHandler mServiceHandler;
    private LocalBroadcastManager mLocalBroadcastManager;

    private final IBinder mBinder = new LocalBinder();

    private static int mInteger = 0;

    public class LocalBinder extends Binder {
        ExtendingBinderService getService() {
            return ExtendingBinderService.this;
        }
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(this.getPackageName());
        mHandlerThread.start();
        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void setInteger(final int integer) {
        ExtendingBinderService.mInteger = integer + 1;
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(INTENT_ACTION);
                intent.putExtra(getString(R.string.integer_type), mInteger);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });
    }
}
