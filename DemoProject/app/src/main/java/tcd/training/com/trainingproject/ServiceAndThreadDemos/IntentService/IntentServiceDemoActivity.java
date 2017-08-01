package tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.EventBus.EventBusMessage;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.EventBus.IntentServiceWithEventBus;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.LocalBroadcastReceiver.IntentServiceWithBroadcastReceiver;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.ResultReceiver.IntentServiceWithResultReceiver;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.ResultReceiver.MyResultReceiver;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.RxJava.IntentServiceWithRxJava;
import tcd.training.com.trainingproject.ServiceAndThreadDemos.IntentService.RxJava.RxBus;

public class IntentServiceDemoActivity extends AppCompatActivity {

    public static final String TAG = IntentServiceDemoActivity.class.getSimpleName();

    public static final int RESULT_RECEIVER_METHOD = 0;
    public static final int BROADCAST_RECEIVER_METHOD = 1;
    public static final int EVENT_BUS_METHOD = 2;
    public static final int RX_JAVA_METHOD = 3;

    private TextView mIntegerValueTextView;
    private int mInteger = 0;

    private MyResultReceiver mResultReceiver;
    private BroadcastReceiver mBroadcastReceiver;
    private int mCurrentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service_demo);

        mIntegerValueTextView = findViewById(R.id.tv_integer_value);

        mCurrentMethod = getIntent().getIntExtra(getString(R.string.integer_type), RESULT_RECEIVER_METHOD);
        switch (mCurrentMethod) {
            case RESULT_RECEIVER_METHOD: ((RadioButton)findViewById(R.id.rb_result_receiver)).setChecked(true); break;
            case BROADCAST_RECEIVER_METHOD: ((RadioButton)findViewById(R.id.rb_broadcast_receiver)).setChecked(true); break;
            case EVENT_BUS_METHOD: ((RadioButton)findViewById(R.id.rb_event_bus)).setChecked(true); break;
            case RX_JAVA_METHOD: ((RadioButton)findViewById(R.id.rb_rx_java)).setChecked(true); break;
        }

        setupServiceResultReceiver();
        launchService();
    }

    private void setupServiceResultReceiver() {
        // result receiver
        mResultReceiver = new MyResultReceiver(new Handler());
        mResultReceiver.setReceiver(new MyResultReceiver.Receiver() {
            @Override
            public void onReceivedResult(int resultCode, Bundle resultData) {
                if (resultCode == Activity.RESULT_OK) {
                    mInteger = resultData.getInt(getString(R.string.integer_type));
                    mIntegerValueTextView.setText(String.valueOf(mInteger));
                }
            }
        });
        // broadcast receiver
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mInteger = intent.getIntExtra(getString(R.string.integer_type), -1);
                mIntegerValueTextView.setText(String.valueOf(mInteger));
            }
        };
        // RxJava
        RxBus.subscribe(new Consumer<EventBusMessage>() {
            @Override
            public void accept(@NonNull final EventBusMessage eventBusMessage) throws Exception {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mInteger = eventBusMessage.getInteger();
                        mIntegerValueTextView.setText(String.valueOf(mInteger));
                    }
                });
            }
        });
    }

    // register and unregister broadcast receiver / event bus
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(IntentServiceWithBroadcastReceiver.INTENT_ACTION));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onMessage(EventBusMessage event) {
        mInteger = event.getInteger();
        mIntegerValueTextView.setText(String.valueOf(mInteger));
    }

    private void launchService() {
        Intent serviceIntent = new Intent();
        switch (mCurrentMethod) {
            case RESULT_RECEIVER_METHOD:
                serviceIntent.setClass(this, IntentServiceWithResultReceiver.class);
                serviceIntent.putExtra(getString(R.string.result_receiver), mResultReceiver);
                break;
            case BROADCAST_RECEIVER_METHOD:
                serviceIntent.setClass(this, IntentServiceWithBroadcastReceiver.class);
                break;
            case EVENT_BUS_METHOD:
                serviceIntent.setClass(this, IntentServiceWithEventBus.class);
                break;
            case RX_JAVA_METHOD:
                serviceIntent.setClass(this, IntentServiceWithRxJava.class);
                break;
        }
        serviceIntent.putExtra(getString(R.string.integer_type), mInteger);
        startService(serviceIntent);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.rb_broadcast_receiver:
                    mCurrentMethod = BROADCAST_RECEIVER_METHOD;
                    break;
                case R.id.rb_result_receiver:
                    mCurrentMethod = RESULT_RECEIVER_METHOD;
                    break;
                case R.id.rb_event_bus:
                    mCurrentMethod = EVENT_BUS_METHOD;
                    break;
                case R.id.rb_rx_java:
                    mCurrentMethod = RX_JAVA_METHOD;
                    break;
            }
            launchService();
        }
    }
}
