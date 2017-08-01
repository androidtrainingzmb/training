package tcd.training.com.trainingproject.ServicesDemo.HandlerThreadDemo;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tcd.training.com.trainingproject.R;
import tcd.training.com.trainingproject.ServicesDemo.ThreadPoolExecutorDemo.DownloadImageTask;

public class HandlerThreadDemoActivity extends AppCompatActivity implements Handler.Callback {

    private static int mNumberOfImages = 8;
    private final String mImageUrl = "https://source.unsplash.com/random";
//    private final String mImageUrl = "source.unsplash.com/4MSwGhLKhi8";

    private TextView mDownloadOrderTextView;
    private LinearLayout mImagesListLinearLayout;
    private EditText mNumberOfImagesEditText;
    private Button mStartDownloadButton;

    private HandlerThread mHandlerThread;

    private boolean mDisplayImages = true;
    private long mStartTime;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_demo);

        initializeUiComponents();

        mHandlerThread = new HandlerThread(this.getLocalClassName());
        mHandlerThread.start();
        final Handler handler = new Handler(mHandlerThread.getLooper(), this);

        mStartDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // number of images
                if (mNumberOfImagesEditText.getText().toString().equals("")) {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.number_of_images_empty_error), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mNumberOfImages = Integer.valueOf(mNumberOfImagesEditText.getText().toString());
                // reset download info
                mCount = 0;
                mDownloadOrderTextView.setText("");
                mImagesListLinearLayout.removeAllViews();
                // prepare for download
                mDisplayImages = ((Switch)findViewById(R.id.sw_display_images)).isChecked();
                mStartDownloadButton.setEnabled(false);
                mStartTime = Calendar.getInstance().getTimeInMillis();
                // start download
                for (int i = 0; i < mNumberOfImages; i++) {
                    handler.post(new DownloadImageTask(i, handler, mImageUrl));
                }}
        });
    }

    private void initializeUiComponents() {
        mDownloadOrderTextView = findViewById(R.id.tv_download_order);
        mImagesListLinearLayout = findViewById(R.id.linear_layout_images_list);
        mNumberOfImagesEditText = findViewById(R.id.edt_number_of_images);
        mStartDownloadButton = findViewById(R.id.btn_start_download);

        // hide the switch
        findViewById(R.id.sw_execute_on_executor).setVisibility(View.GONE);
        findViewById(R.id.tv_execute_on_executor).setVisibility(View.GONE);
    }

    @Override
    public boolean handleMessage(Message message) {

        final int threadId = message.what;
        final Bitmap bitmap = (Bitmap) message.obj;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadOrderTextView.append(String.valueOf(threadId + 1) + " ");

                if (mDisplayImages) {
                    ImageView imageView = new ImageView(HandlerThreadDemoActivity.this);
                    imageView.setImageBitmap(bitmap);
                    mImagesListLinearLayout.addView(imageView);
                }

                mCount++;
                if (mCount == mNumberOfImages) {
                    double elapsedTime = (Calendar.getInstance().getTimeInMillis() - mStartTime) / 1000.0;
                    mDownloadOrderTextView.append("(" + String.valueOf(elapsedTime) + "s)");
                    mStartDownloadButton.setEnabled(true);
                }
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }
}
