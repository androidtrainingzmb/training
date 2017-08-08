package tcd.training.com.trainingproject.ImagesTopic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import tcd.training.com.trainingproject.R;

public class CacheImageUsingDiskDemoActivity extends AppCompatActivity {

    // for caching
    private DiskLruCache mDiskCache;
    private Object mDiskCacheLock = new Object();       // for synchronized
    private boolean mDiskCacheStarting = true;
    private final static int DISK_CACHE_SIZE = 1024 * 1024 * 10;    // 10MB
    private final static String DISK_CACHE_SUBDIR = "thumbnails";
    private boolean mIsUsingCache;

    // layout
    private LinearLayout mRootLinearLayout;
    private Switch mUsingCacheSwitch;
    private TextView mShownImagesTextView;

    // loading info
    private static final int NUMBER_OF_IMAGES = 5;
    private long mStartTime;
    private int mImageIndex = 0;
    private String[] mImagesUrl = new String[] {
            "https://source.unsplash.com/19NtUg2HjeQ/",
            "https://source.unsplash.com/MUHe6nNMXVI/",
            "https://source.unsplash.com/_Ajm-ewEC24/",
            "https://source.unsplash.com/mtu6m_nLFQI/",
            "https://source.unsplash.com/8Hjx3GNZYeA/"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_image_using_memory_demo);

//        initializeUiComponents();
//        initializeCacheComponents();

        LinearLayout rootView = findViewById(R.id.linear_layout_root);
        TextView explanationTextView = new TextView(this);
        explanationTextView.setText("This won't work because I can't import the DiskLruCache mentioned in the documentation. It requires a unique file called charsets in java.nio, but I couldn't find any information on how to do that.\nIf all the required files are imported correctly, just uncomment these lines 62, 63, 99-105, 178-180, 194-200, and it should be running fine.");
        rootView.removeAllViews();
        rootView.addView(explanationTextView);
    }

    private void initializeUiComponents() {
        mRootLinearLayout = findViewById(R.id.linear_layout_images_list);
        mShownImagesTextView = findViewById(R.id.tv_shown_images);

        mUsingCacheSwitch = findViewById(R.id.sw_using_cache);
        mUsingCacheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mIsUsingCache = isChecked;
                loadAllImages();
            }
        });

        mIsUsingCache = mUsingCacheSwitch.isChecked();
    }

    private void initializeCacheComponents() {
        final String cacheFilePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.isExternalStorageRemovable() ?
                        getExternalCacheDir().getPath() : getCacheDir().getPath();
        File cacheFile = new File(cacheFilePath + File.separator + DISK_CACHE_SUBDIR);
        new InitDiskCacheTask().execute(cacheFile);
    }

    private class InitDiskCacheTask extends AsyncTask<File, Void, Void> {

        @Override
        protected Void doInBackground(File... files) {
            synchronized (mDiskCacheLock) {
//                try {
//                    mDiskCache = DiskLruCache.open(files[0], DISK_CACHE_SIZE);
//                    mDiskCacheStarting = false;
//                    mDiskCacheLock.notifyAll();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            return null;
        }
    }

    private void increaseImageIndex() {
        mImageIndex++;
        mShownImagesTextView.setText(mImageIndex + "/" + NUMBER_OF_IMAGES);
        if (mImageIndex == NUMBER_OF_IMAGES) {
            long end = Calendar.getInstance().getTimeInMillis();
            mShownImagesTextView.append(" (" + String.valueOf(end - mStartTime) + "ms)");
            mUsingCacheSwitch.setEnabled(true);
        }
    }

    private void loadAllImages() {
        // clean previous process
        mRootLinearLayout.removeAllViews();
        mShownImagesTextView.setText("0/" + NUMBER_OF_IMAGES);
        mImageIndex = 0;
        mStartTime = Calendar.getInstance().getTimeInMillis();
        // start loading
        mUsingCacheSwitch.setEnabled(false);
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            loadBitmap(mImagesUrl[i % mImagesUrl.length], createAnImageView());
        }
    }

    private void loadBitmap(String imageUrl, ImageView imageView) {
        if (mIsUsingCache) {
            final Bitmap bitmap = getBitmapFromDiskCache(imageUrl);
            // it's already in the cache memory
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                increaseImageIndex();
            } else {
                new LoadAndCacheBitmapTask().execute(imageUrl);
            }
        } else {
            new LoadAndCacheBitmapTask().execute(imageUrl);
        }
    }

    private class LoadAndCacheBitmapTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageUrl = strings[0];
            Bitmap bitmap = getBitmapFromDiskCache(imageUrl);
            // not found in disk cache
            if (bitmap == null) {
                try {
                    URL url = new URL(strings[0]);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addBitmapToDiskCache(imageUrl, bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            createAnImageView().setImageBitmap(bitmap);
            increaseImageIndex();
        }
    }

    private void addBitmapToDiskCache(String key, Bitmap bitmap) {
        synchronized (mDiskCacheLock) {
//            if (mDiskCache != null && mDiskCache.get(key) == null) {
//                mDiskCache.put(key, bitmap);
//            }
        }
    }

    private Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            if (mDiskCache != null) {
//                try {
//                    return mDiskCache.get(key);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return null;
    }

    private ImageView createAnImageView() {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dpToPx_8 =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        layoutParams.setMargins(0, dpToPx_8, 0, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        mRootLinearLayout.addView(imageView);

        return imageView;
    }
}
