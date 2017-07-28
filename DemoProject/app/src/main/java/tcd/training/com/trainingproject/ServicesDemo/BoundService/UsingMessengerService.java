package tcd.training.com.trainingproject.ServicesDemo.BoundService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 28/07/2017.
 */

public class UsingMessengerService extends Service {

    public static final String TAG = UsingMessengerService.class.getSimpleName();

    public static final int MSG_RETRIEVE_INTEGER = 1;
    public static final String INTENT_ACTION = UsingMessengerService.class.getSimpleName();

    private ServiceHandler mServiceHandler;
    private volatile HandlerThread mHandlerThread;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private int mInteger;
    private LocalBroadcastManager mLocalBroadcastManager;

    class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            Log.d(TAG, "handleMessage: ");
            switch (msg.what) {
                case MSG_RETRIEVE_INTEGER:
                    mInteger = msg.arg1 + 1;
                    mServiceHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(INTENT_ACTION);
                            intent.putExtra(getString(R.string.integer_type), mInteger);
                            mLocalBroadcastManager.sendBroadcast(intent);
                        }
                    });
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(getPackageName());
        mHandlerThread.start();
        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
