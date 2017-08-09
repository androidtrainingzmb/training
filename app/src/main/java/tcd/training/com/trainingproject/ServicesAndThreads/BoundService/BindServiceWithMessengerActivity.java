package tcd.training.com.trainingproject.ServicesAndThreads.BoundService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

public class BindServiceWithMessengerActivity extends AppCompatActivity {

    private static final String TAG = BindServiceWithMessengerActivity.class.getSimpleName();

    private TextView mIntegerValueTextView;
    private Button mIncrementByOneButton;

    private int mInteger = 0;
    private ServiceConnection mConnection;
    private Messenger mMessenger = null;
    private boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_service_demo);

        mIntegerValueTextView = findViewById(R.id.tv_integer_value);
        mIntegerValueTextView.setText(String.valueOf(mInteger));

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

        mIncrementByOneButton = findViewById(R.id.btn_increment_by_one);
        mIncrementByOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    Message message = Message.obtain(null, UsingMessengerService.MSG_SEND_INTEGER, mInteger, mInteger);
                    // create a handler to handle response message
                    message.replyTo = new Messenger(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {
                            switch (message.what) {
                                case UsingMessengerService.MSG_RETRIEVE_INTEGER:
                                    mInteger = message.arg1;
                                    mIntegerValueTextView.setText(String.valueOf(mInteger));
                                    break;
                            }
                            return true;
                        }
                    }));
                    // send it
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(BindServiceWithMessengerActivity.this, "The service has not been bound to the activity.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
