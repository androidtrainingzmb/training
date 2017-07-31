package tcd.training.com.trainingproject.ServicesDemo.CustomService;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by cpu10661-local on 27/07/2017.
 */

public class MyCustomService extends Service {

    public static final String INTENT_ACTION = MyCustomService.class.getName();
    private ServiceHandler mServiceHandler;
    private volatile HandlerThread mHandlerThread;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(this.getPackageName());
        mHandlerThread.start();
        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent broadcastIntent = new Intent(INTENT_ACTION);
                LocalBroadcastManager.getInstance(MyCustomService.this).sendBroadcast(broadcastIntent);
                Log.d(TAG, "run: " + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                stopSelf();
            }
        });
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
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
}
