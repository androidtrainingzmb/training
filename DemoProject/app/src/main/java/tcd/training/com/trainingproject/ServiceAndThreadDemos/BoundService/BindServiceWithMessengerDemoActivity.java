package tcd.training.com.trainingproject.ServiceAndThreadDemos.BoundService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class BindServiceWithMessengerDemoActivity extends AppCompatActivity {

    private static final String TAG = BindServiceWithMessengerDemoActivity.class.getSimpleName();

    private TextView integerValueTextView;
    private Button incrementByOneButton;

    private int mInteger = 0;
    private ServiceConnection mConnection;
    private Messenger mMessenger = null;
    private boolean mBound;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_demo);

        integerValueTextView = findViewById(R.id.tv_integer_value);
        integerValueTextView.setText(String.valueOf(mInteger));

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mMessenger = new Messenger(iBinder);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mMessenger = null;
                mBound = false;
            }
        };

        incrementByOneButton = findViewById(R.id.btn_increment_by_one);
        incrementByOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    Message message = Message.obtain(null, UsingMessengerService.MSG_RETRIEVE_INTEGER, mInteger, mInteger);
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(BindServiceWithMessengerDemoActivity.this, "The service has not been bound to the activity.", Toast.LENGTH_SHORT).show();
                } 
            }
        });

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
        Intent intent = new Intent(this, UsingMessengerService.class);
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
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(UsingMessengerService.INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}
