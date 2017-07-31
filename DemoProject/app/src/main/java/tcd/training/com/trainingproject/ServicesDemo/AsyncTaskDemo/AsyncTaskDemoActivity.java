package tcd.training.com.trainingproject.ServicesDemo.AsyncTaskDemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import tcd.training.com.trainingproject.R;

public class AsyncTaskDemoActivity extends AppCompatActivity {

    private static final String TAG = AsyncTaskDemoActivity.class.getSimpleName();

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

        startTime = Calendar.getInstance().getTimeInMillis();

        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            new DownloadImageAsyncTask(i).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mImageUrl);
        }
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private int mThreadId;
        private String mImageUrl;

        public DownloadImageAsyncTask(int threadId) {
            this.mThreadId = threadId;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: ");
            if (strings == null) {
                return null;
            }
            mImageUrl = strings[0];
            Log.d(TAG, "doInBackground: " + mImageUrl);
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(mImageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mDownloadOrderTextView.append(String.valueOf(mThreadId + 1) + " ");

            ImageView imageView = new ImageView(AsyncTaskDemoActivity.this);
            imageView.setImageBitmap(bitmap);
            mImagesListLinearLayout.addView(imageView);

            mCount++;
            if (mCount == NUMBER_OF_IMAGES) {
                long elapsedTime = (Calendar.getInstance().getTimeInMillis() - startTime) / 60;
                mDownloadOrderTextView.append("(" + String.valueOf(elapsedTime) + "ms)");
            }
        }
    }
}
