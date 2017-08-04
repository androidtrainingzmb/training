package tcd.training.com.trainingproject.ImagesTopic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.Executor;

import tcd.training.com.trainingproject.R;

public class CacheImageUsingMemoryDemoActivity extends AppCompatActivity {

    private static final int NUMBER_OF_IMAGES = 100;
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean mIsUsingCache;

    private LinearLayout mRootLinearLayout;
    private Switch mUsingCacheSwitch;
    private TextView mShownImagesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_image_using_memory_demo);

        initializeUiComponents();

        prepareToCache();
        loadAllImages();
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

    private void loadAllImages() {
        mRootLinearLayout.removeAllViews();
        long start = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < NUMBER_OF_IMAGES; i++) {
            loadBitmap(R.drawable.landscape_large, createAnImageView());
            mShownImagesTextView.setText(String.valueOf(i + 1));
        }
        long end = Calendar.getInstance().getTimeInMillis();
        mShownImagesTextView.append(" (" + String.valueOf(end - start) + "ms)");
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

    private void loadBitmap(int resId, ImageView imageView) {
        if (mIsUsingCache) {
            final Bitmap bitmap = getBitmapFromMemoryCache(String.valueOf(resId));
            // it's already in the cache memory
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(resId);
                CacheBitmapTask task = new CacheBitmapTask();
                task.execute(resId);
            }
        } else {
            imageView.setImageResource(resId);
        }
    }

    private class CacheBitmapTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            final Bitmap bitmap = decodeBitmapFromResource(getResources(), integers[0], 100, 100);
            addBitmapToMemoryCache(String.valueOf(integers[0]), bitmap);
            return bitmap;
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;
        if (options.outHeight > reqHeight || options.outWidth> reqWidth) {

            // don't know why we have to divide them by 2 but it's declared like this in documentation
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;

            /*
             * Calculate the largest inSampleSize value that is a power of 2 and keeps both
             * height and width larger than the requested height and width.
             * (A power of two value is calculated because the decoder uses a final value by rounding down to the nearest power of two,
             * as per the inSampleSize documentation)
             */
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
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

    private ImageView createAnImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        mRootLinearLayout.addView(imageView);

        return imageView;
    }
}
