package tcd.training.com.trainingproject.ImageProcessing.LoadImages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import tcd.training.com.trainingproject.R;

public class LoadImageFromUrlActivity extends AppCompatActivity {

    private static final String TAG = LoadImageFromUrlActivity.class.getSimpleName();

    private LinearLayout mRootLinearLayout;
    private final String mImageUrl = "https://source.unsplash.com/daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_demo);

        mRootLinearLayout = findViewById(R.id.linear_layout_images_list);

        loadImageUsingDecodeStream();
        loadImageUsingGlide();
        loadImageUsingPicasso();
        loadImageUsingDrawableAndInputStream();
    }

    private void loadImageUsingDrawableAndInputStream() {
        addMethodNameTextView("Drawable and InputStream");
        final ImageView imageView = createAnImageView();
        HandlerThread handlerThread = new HandlerThread(getPackageName());
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = (InputStream) new URL(mImageUrl).getContent();
                    final Drawable drawable = Drawable.createFromStream(inputStream, "image");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void loadImageUsingDecodeStream() {
        addMethodNameTextView("Bitmap Factory - Decode Stream");
        final ImageView imageView = createAnImageView();
        HandlerThread handlerThread = new HandlerThread(getPackageName());
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mImageUrl);
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadImageUsingPicasso() {
        addMethodNameTextView("Picasso");
        Picasso.with(this).load(mImageUrl).into(createAnImageView());
    }

    private void loadImageUsingGlide() {
        addMethodNameTextView("Glide");
        Glide.with(this).load(mImageUrl).into(createAnImageView());
    }

    private void addMethodNameTextView(String methodName) {
        TextView methodNameTextView = new TextView(this);
        methodNameTextView.setText(methodName);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dpToPx_8 =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        layoutParams.setMargins(0, dpToPx_8, 0, dpToPx_8);
        methodNameTextView.setLayoutParams(layoutParams);

        mRootLinearLayout.addView(methodNameTextView);
    }

    private ImageView createAnImageView() {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        mRootLinearLayout.addView(imageView);

        return imageView;
    }
}
