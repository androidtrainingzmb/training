package tcd.training.com.trainingproject.ImagesTopic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import tcd.training.com.trainingproject.R;

public class CacheImageUsingMemoryDemoActivity extends AppCompatActivity {

    // for caching
    private static final int NUMBER_OF_IMAGES = 5;
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean mIsUsingCache;

    // layout
    private LinearLayout mRootLinearLayout;
    private TextView mShownImagesTextView;

    // loading info
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

        initializeUiComponents();

        prepareToCache();
        loadAllImages();
    }

    private void increaseImageIndex() {
        mImageIndex++;
        mShownImagesTextView.setText(mImageIndex + "/" + NUMBER_OF_IMAGES);
        if (mImageIndex == NUMBER_OF_IMAGES) {
            long end = Calendar.getInstance().getTimeInMillis();
            mShownImagesTextView.append(" (" + String.valueOf(end - mStartTime) + "ms)");
        }
    }

    private void initializeUiComponents() {
        mRootLinearLayout = findViewById(R.id.linear_layout_images_list);
        mShownImagesTextView = findViewById(R.id.tv_shown_images);

        Switch mUsingCacheSwitch = findViewById(R.id.sw_using_cache);
        mUsingCacheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mIsUsingCache = isChecked;
                loadAllImages();
            }
        });

        mIsUsingCache = mUsingCacheSwitch.isChecked();
    }

    private void loadAllImages() {
        // clean previous process
        mRootLinearLayout.removeAllViews();
        mShownImagesTextView.setText("0/" + NUMBER_OF_IMAGES);
        mImageIndex = 0;
        mStartTime = Calendar.getInstance().getTimeInMillis();
        // start loading
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            loadBitmap(mImagesUrl[i % mImagesUrl.length], createAnImageView());
        }
    }

    private void prepareToCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    private void loadBitmap(String imageUrl, ImageView imageView) {
        if (mIsUsingCache) {
            final Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
            // it's already in the cache memory
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                increaseImageIndex();
            } else {
                loadImageUsingDecodeStream(imageUrl);
                CacheBitmapTask task = new CacheBitmapTask();
                task.execute(imageUrl);
            }
        } else {
            loadImageUsingDecodeStream(imageUrl);
        }
    }

    private class CacheBitmapTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
//            final Bitmap bitmap = decodeBitmapFromResource(getResources(), strings[0], 100, 100);
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(CacheImageUsingMemoryDemoActivity.this).asBitmap().load(strings[0]).into(500, 500).get();
                addBitmapToMemoryCache(strings[0], bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private void loadImageUsingDecodeStream(final String imageUrl) {
        final ImageView imageView = createAnImageView();
        HandlerThread handlerThread = new HandlerThread(getPackageName());
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                            increaseImageIndex();
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ImageView createAnImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        mRootLinearLayout.addView(imageView);

        return imageView;
    }

    /*
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (options.outHeight > reqHeight || options.outWidth> reqWidth) {

            // don't know why we have to divide them by 2 but it's declared like this in documentation
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            // (A power of two value is calculated because the decoder uses a final value by rounding down to the nearest power of two,
            // as per the inSampleSize documentation)
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeBitmapFromResource(Resources res, String imageUrl, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    */
}
