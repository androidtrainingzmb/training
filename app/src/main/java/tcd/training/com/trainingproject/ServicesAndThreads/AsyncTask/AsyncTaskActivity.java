package tcd.training.com.trainingproject.ServicesAndThreads.AsyncTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import tcd.training.com.trainingproject.R;

public class AsyncTaskActivity extends AppCompatActivity {

    private static final String TAG = AsyncTaskActivity.class.getSimpleName();

    private static int mNumberOfImages = 8;
    private final String mImageUrl = "https://source.unsplash.com/random";
//    private final String mImageUrl = "source.unsplash.com/4MSwGhLKhi8";

    private TextView mDownloadOrderTextView;
    private LinearLayout mImagesListLinearLayout;
    private EditText mNumberOfImagesEditText;
    private Switch mUsingExecuteOnExecutorSwitch;
    private Button mStartDownloadButton;
    
    private boolean mDisplayImages = true;
    private long mStartTime;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_demo);

        initializeUiComponents();

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
                // using executeOnExecutor
                boolean usingExecuteOnExecutor = mUsingExecuteOnExecutorSwitch.isChecked();
                // prepare for download
                mDisplayImages = ((Switch)findViewById(R.id.sw_display_images)).isChecked();
                mStartDownloadButton.setEnabled(false);
                mStartTime = Calendar.getInstance().getTimeInMillis();
                // start download
                for (int i = 0; i < mNumberOfImages; i++) {
                    if (usingExecuteOnExecutor) {
                        new DownloadImageAsyncTask(i).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mImageUrl);
                    } else {
                        new DownloadImageAsyncTask(i).execute(mImageUrl);
                    }
                }
            }
        });
    }

    private void initializeUiComponents() {
        mDownloadOrderTextView = findViewById(R.id.tv_download_order);
        mImagesListLinearLayout = findViewById(R.id.linear_layout_images_list);
        mNumberOfImagesEditText = findViewById(R.id.edt_number_of_images);
        mUsingExecuteOnExecutorSwitch = findViewById(R.id.sw_execute_on_executor);
        mStartDownloadButton = findViewById(R.id.btn_start_download);
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private final int mThreadId;
        private String mImageUrl;

        public DownloadImageAsyncTask(int threadId) {
            this.mThreadId = threadId;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (strings == null) {
                return null;
            }
            mImageUrl = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(mImageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mDownloadOrderTextView.append(String.valueOf(mThreadId + 1) + " ");

            if (mDisplayImages) {
                ImageView imageView = new ImageView(AsyncTaskActivity.this);
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
    }
}
