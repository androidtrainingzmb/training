package tcd.training.com.trainingproject.ImagesProcessing.CacheImages;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import tcd.training.com.trainingproject.R;

public class CacheImageUsingExternalLibraryActivity extends AppCompatActivity {

    private static final String TAG = CacheImageUsingExternalLibraryActivity.class.getSimpleName();

    // methods
    public static final int GLIDE_METHOD = 0;
    public static final int PICASSO_METHOD = 1;
    private int mCurrentMethod;

    // for caching
    private static final int NUMBER_OF_IMAGES = 5;
    private boolean mIsUsingCache;

    // layout
    private LinearLayout mRootLinearLayout;
    private Switch mUsingCacheSwitch;
    private TextView mShownImagesTextView;
    private Spinner mMethodSpinner;
    private Button mRefreshButton;

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

        mCurrentMethod = getIntent().getIntExtra(getString(R.string.integer_type), GLIDE_METHOD);

        initializeUiComponents();

        // retain switch state
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.boolean_type))) {
                mIsUsingCache = savedInstanceState.getBoolean(getString(R.string.boolean_type));
            }
            if (mIsUsingCache) {
                loadAllImages();
            }
        } else {
            mIsUsingCache = mUsingCacheSwitch.isChecked();
            loadAllImages();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(getString(R.string.boolean_type), mIsUsingCache);
        super.onSaveInstanceState(outState);
    }

    private void increaseImageIndex() {
        mImageIndex++;
        mShownImagesTextView.setText(mImageIndex + "/" + NUMBER_OF_IMAGES);
        if (mImageIndex == NUMBER_OF_IMAGES) {
            long end = Calendar.getInstance().getTimeInMillis();
            mShownImagesTextView.append(" (" + String.valueOf(end - mStartTime) + "ms)");
            mUsingCacheSwitch.setEnabled(true);
            mRefreshButton.setEnabled(true);
        }
    }

    private void initializeUiComponents() {
        mRootLinearLayout = findViewById(R.id.linear_layout_images_list);
        mShownImagesTextView = findViewById(R.id.tv_shown_images);

        mUsingCacheSwitch = findViewById(R.id.sw_using_cache);
        mUsingCacheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mIsUsingCache = isChecked;
                if (mCurrentMethod == GLIDE_METHOD) {
                    if (!mIsUsingCache) {
                        Glide.get(CacheImageUsingExternalLibraryActivity.this).clearMemory();
                        // call this method from background thread to prevent blocking UI
                        HandlerThread handlerThread = new HandlerThread(getPackageName());
                        handlerThread.start();
                        Handler handler = new Handler(handlerThread.getLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(CacheImageUsingExternalLibraryActivity.this).clearDiskCache();
                            }
                        });
                    }
                } else if (mCurrentMethod == PICASSO_METHOD) {
                    for (String url : mImagesUrl) {
                        Picasso.with(CacheImageUsingExternalLibraryActivity.this).invalidate(url);
                    }
                }
                loadAllImages();
            }
        });
        mIsUsingCache = mUsingCacheSwitch.isChecked();

        mRefreshButton = findViewById(R.id.btn_refresh);
        mRefreshButton.setVisibility(View.VISIBLE);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshButton.setEnabled(false);
                loadAllImages();
            }
        });

        createMethodSpinner();

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
        if (mCurrentMethod == GLIDE_METHOD) {
            RequestListener<String, GlideDrawable> listener = new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    increaseImageIndex();
                    return false;
                }
            };

            if (mIsUsingCache) {
                Glide.with(this).load(imageUrl).listener(listener).into(imageView);
            } else {
                Glide.with(this).load(imageUrl).listener(listener)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(imageView);
            }
        } else if (mCurrentMethod == PICASSO_METHOD) {
            Callback callback = new Callback() {
                @Override
                public void onSuccess() {
                    increaseImageIndex();
                }
                @Override
                public void onError() {}
            };
            if (mIsUsingCache) {
                Picasso.with(this).load(imageUrl).into(imageView, callback);
            } else {
                Picasso.with(this).load(imageUrl)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageView, callback);
            }
        }
    }

    private void createMethodSpinner() {
        mMethodSpinner = new Spinner(this);
        mMethodSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Glide", "Picasso"}));
        ((LinearLayout)findViewById(R.id.linear_layout_root)).addView(mMethodSpinner, 0);

        mMethodSpinner.setSelection(mCurrentMethod);

        mMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentMethod = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
