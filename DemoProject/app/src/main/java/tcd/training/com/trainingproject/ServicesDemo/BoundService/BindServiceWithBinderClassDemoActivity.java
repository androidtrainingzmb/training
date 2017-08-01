package tcd.training.com.trainingproject.ServicesDemo.BoundService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import tcd.training.com.trainingproject.R;

public class BindServiceWithBinderClassDemoActivity extends AppCompatActivity {
    private static final String TAG = BindServiceWithBinderClassDemoActivity.class.getSimpleName();

//    public static final TAG = BindServiceWithBinderClassDemoActivity.class.getSimpleName();

    private TextView integerValueTextView;
    private Button incrementByOneButton;

    ExtendedBinderClassService mExtendingBinderService;
    boolean mBound = false;
    private ServiceConnection mConnection;
    private BroadcastReceiver mReceiver;
    private int mInteger = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_demo);

        integerValueTextView = findViewById(R.id.tv_integer_value);
        integerValueTextView.setText(String.valueOf(mInteger));

        incrementByOneButton = findViewById(R.id.btn_increment_by_one);
        incrementByOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    mExtendingBinderService.setInteger(mInteger);
                }
            }
        });

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ExtendedBinderClassService.LocalBinder localBinder = (ExtendedBinderClassService.LocalBinder) iBinder;
                mExtendingBinderService = localBinder.getService();
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBound = false;
            }
        };

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mInteger = intent.getIntExtra(getString(R.string.integer_type), -1);
                integerValueTextView.setText(String.valueOf(mInteger));
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ExtendedBinderClassService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ExtendedBinderClassService.INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}