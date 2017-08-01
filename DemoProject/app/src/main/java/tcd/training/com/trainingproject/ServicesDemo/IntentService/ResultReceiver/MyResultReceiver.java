package tcd.training.com.trainingproject.ServicesDemo.IntentService.ResultReceiver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by cpu10661-local on 26/07/2017.
 */

public class MyResultReceiver extends ResultReceiver {

    private Receiver mReceiver;

    public MyResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        mReceiver.onReceivedResult(resultCode, resultData);
    }

    public interface Receiver {
        void onReceivedResult(int resultCode, Bundle resultData);
    }
}
