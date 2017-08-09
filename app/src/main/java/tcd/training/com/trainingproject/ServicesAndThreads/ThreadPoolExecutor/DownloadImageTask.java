package tcd.training.com.trainingproject.ServicesAndThreads.ThreadPoolExecutor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by cpu10661-local on 31/07/2017.
 */

public class DownloadImageTask implements Runnable {

    private int mThreadId;
    private Handler mHandler;
    private String mImageUrl;

    public DownloadImageTask(int threadId, Handler handler, String imageUrl) {
        this.mThreadId = threadId;
        this.mHandler = handler;
        this.mImageUrl = imageUrl;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        Bitmap bitmap = downloadImage(mImageUrl);

        Message message = mHandler.obtainMessage(mThreadId, bitmap);
        message.sendToTarget();
    }

    private Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
