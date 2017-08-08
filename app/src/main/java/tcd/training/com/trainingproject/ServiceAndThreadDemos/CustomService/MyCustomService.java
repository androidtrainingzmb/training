package tcd.training.com.trainingproject.ServiceAndThreadDemos.CustomService;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Calendar;

import tcd.training.com.trainingproject.R;

import static android.content.ContentValues.TAG;

/**
 * Created by cpu10661-local on 27/07/2017.
 */

public class MyCustomService extends Service {

    public static final String INTENT_ACTION = MyCustomService.class.getName();

    public static final String NOTIFICATION_EXTRA = "notification";
    public static final int NOTIFICATION_ON = 1;
    public static final int NOTIFICATION_OFF = -1;

    private static final int ONGOING_NOTIFICATION_ID = 1;
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
        // intent for the notification
        if (intent.hasExtra(NOTIFICATION_EXTRA)) {
            if (intent.getIntExtra(NOTIFICATION_EXTRA, NOTIFICATION_OFF) == NOTIFICATION_ON) {
                createNotificationIcon();
            } else {
                stopForeground(true);
            }
        }
        // intent of the alarm
        else {
            mServiceHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent broadcastIntent = new Intent(INTENT_ACTION);
                    LocalBroadcastManager.getInstance(MyCustomService.this).sendBroadcast(broadcastIntent);
                    Log.d(TAG, "run: " + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                }
            });
        }
        return START_STICKY;
    }

    private void createNotificationIcon() {
        Intent notificationIntent = new Intent(this, CustomServiceDemoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Content title")
                .setContentText("Content text")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
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
