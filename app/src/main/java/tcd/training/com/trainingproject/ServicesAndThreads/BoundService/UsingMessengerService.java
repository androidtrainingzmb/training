package tcd.training.com.trainingproject.ServicesAndThreads.BoundService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by cpu10661-local on 28/07/2017.
 */

public class UsingMessengerService extends Service {

    public static final String TAG = UsingMessengerService.class.getSimpleName();

    public static final int MSG_SEND_INTEGER = 1;
    public static final int MSG_RETRIEVE_INTEGER = 2;

    private ServiceHandler mServiceHandler;
    private volatile HandlerThread mHandlerThread;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private int mInteger;

    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

        private ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_SEND_INTEGER:
                    mInteger = msg.arg1 + 1;
                    final Messenger messenger = msg.replyTo;
                    mServiceHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Message response = Message.obtain(null, MSG_RETRIEVE_INTEGER, mInteger, 0);
                            try {
                                messenger.send(response);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
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
