package tcd.training.com.trainingproject.ServicesDemo.ThreadPoolExecutorDemo;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tcd.training.com.trainingproject.R;

public class ThreadPoolExecutorDemoActivity extends AppCompatActivity implements Handler.Callback {

    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static int NUMBER_OF_IMAGES = 16;
    private final String mImageUrl = "https://source.unsplash.com/random";

    private TextView mDownloadOrderTextView;
    private LinearLayout mImagesListLinearLayout;

    private long startTime;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool_executor_demo);

        mDownloadOrderTextView = findViewById(R.id.tv_download_order);
        mImagesListLinearLayout = findViewById(R.id.linear_layout_images_list);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        Handler handler = new Handler(this);
        startTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            executor.execute(new DownloadImageTask(i, handler, mImageUrl));
        }
    }

    @Override
    public boolean handleMessage(Message message) {

        int threadId = message.what;
        Bitmap bitmap = (Bitmap) message.obj;

        mDownloadOrderTextView.append(String.valueOf(threadId + 1) + " ");

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        mImagesListLinearLayout.addView(imageView);

        mCount++;
        if (mCount == NUMBER_OF_IMAGES) {
            long elapsedTime = (Calendar.getInstance().getTimeInMillis() - startTime) / 60;
            mDownloadOrderTextView.append("(" + String.valueOf(elapsedTime) + "ms)");
        }

        return true;
    }
}
