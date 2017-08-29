package tcd.training.com.trainingproject.ServicesAndThreads.ThreadPoolExecutor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import tcd.training.com.trainingproject.R;

public class ThreadPoolExecutorUsingCallableActivity extends AppCompatActivity {

    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static int mNumberOfImages = 8;
    private final String mImageUrl = "https://source.unsplash.com/random";
//    private final String mImageUrl = "https://source.unsplash.com/daily";
//    private final String mImageUrl = "https://source.unsplash.com/WLUHO9A_xik/1600x900";

    private TextView mDownloadOrderTextView;
    private LinearLayout mImagesListLinearLayout;
    private EditText mNumberOfImagesEditText;
    private Button mStartDownloadButton;

    private boolean mDisplayImages = true;
    private long mStartTime;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_demo);

        initializeUiComponents();

        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES * 2,
                NUMBER_OF_CORES * 2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        mStartDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // number of images
                if (mNumberOfImagesEditText.getText().toString().equals("")) {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.number_of_images_empty_error), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                prepareUiForNewDownload();
                // HandlerThread for not block UI while getting bitmap from Future
                HandlerThread handlerThread = new HandlerThread(getPackageName());
                handlerThread.start();
                Handler handler = new Handler(handlerThread.getLooper());
                // start download
                for (int i = 0; i < mNumberOfImages; i++) {
                    final Future<Bitmap> result = executor.submit(new Callable<Bitmap>() {
                        @Override
                        public Bitmap call() throws Exception {
                            Bitmap bitmap = null;
                            try {
                                InputStream inputStream = new URL(mImageUrl).openStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return bitmap;
                        }
                    });
                    final int threadId = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                displayImage(result.get(), threadId);
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
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

    private void prepareUiForNewDownload() {
        mNumberOfImages = Integer.valueOf(mNumberOfImagesEditText.getText().toString());
        // reset download info
        mCount = 0;
        mDownloadOrderTextView.setText("");
        mImagesListLinearLayout.removeAllViews();
        // prepare for download
        mDisplayImages = ((Switch)findViewById(R.id.sw_display_images)).isChecked();
        mStartDownloadButton.setEnabled(false);
        mStartTime = Calendar.getInstance().getTimeInMillis();
    }

    private void displayImage(final Bitmap bitmap, final int threadId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadOrderTextView.append(String.valueOf(threadId + 1) + " ");

                if (mDisplayImages) {
                    ImageView imageView = new ImageView(ThreadPoolExecutorUsingCallableActivity.this);
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
    }
}
