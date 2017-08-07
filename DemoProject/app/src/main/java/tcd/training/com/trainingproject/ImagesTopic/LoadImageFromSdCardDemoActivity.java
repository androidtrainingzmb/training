package tcd.training.com.trainingproject.ImagesTopic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import tcd.training.com.trainingproject.R;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

public class LoadImageFromSdCardDemoActivity extends AppCompatActivity {

    private static final String TAG = LoadImageFromSdCardDemoActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private LinearLayout mRootLinearLayout;

    private File mFile;
    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_demo);

        mRootLinearLayout = findViewById(R.id.linear_layout_images_list);

        if (checkWriteExternalStoragePermission()) {
            startDisplayImages();
        }
    }

    private boolean checkWriteExternalStoragePermission() {
        // check if image already exists
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.external_storage_access_error), Snackbar.LENGTH_LONG);
                snackbar.setAction("Grant", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(LoadImageFromSdCardDemoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                });
                snackbar.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
            return false;
        } else {
            return true;
        }
    }

    private void startDisplayImages() {
        mFile = new File(Environment.getExternalStorageDirectory(), "filename.png");
        if (mFile.exists()) {
            mImagePath = mFile.getAbsolutePath();
            loadImageUsingBitmapDecode();
            loadImageUsingDrawable();
            loadImageUsingUri();
            loadImageUsingGlide();
            loadImageUsingPicasso();
        } else {
            downloadAndSaveSampleImage();
        }
    }

    private void loadImageUsingUri() {
        addMethodNameTextView("Uri");
        createAnImageView().setImageURI(Uri.fromFile(mFile));
    }

    private void loadImageUsingDrawable() {
        addMethodNameTextView("Drawable");
        createAnImageView().setImageDrawable(Drawable.createFromPath(mImagePath));
    }

    private void loadImageUsingBitmapDecode() {
        addMethodNameTextView("BitmapDecode");
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
            createAnImageView().setImageBitmap(bitmap);
        } catch (OutOfMemoryError error) {

        }
    }

    private void loadImageUsingPicasso() {
        addMethodNameTextView("Picasso");
        Picasso.with(this).load(mFile).into(createAnImageView());
//        Picasso.with(this).load("file://" + mImagePath).into(createAnImageView());
//        Picasso.with(this).load(Uri.fromFile(mFile)).into(createAnImageView());
    }

    private void loadImageUsingGlide() {
        addMethodNameTextView("Glide");
        Glide.with(this).load(Uri.fromFile(mFile)).into(createAnImageView());
//        Glide.with(this).load(mFile).into(createAnImageView());
//        Glide.with(this).load(R.drawable.landscape_small).into(createAnImageView());
    }

    private void downloadAndSaveSampleImage() {
        // check if the external storage is available
        if (!isExternalStorageWritable()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.external_storage_access_error), Snackbar.LENGTH_SHORT).show();
            return;
        }
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.wait_download), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        // start downloading image
        String imageUrl = "https://source.unsplash.com/TGBfUv0ZgD8/";
        Glide.with(this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>(SIZE_ORIGINAL, SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    FileOutputStream out = new FileOutputStream(mFile);
                    resource.compress(Bitmap.CompressFormat.JPEG, 90, out);

                    startDisplayImages();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    snackbar.dismiss();
                }
            }
        });
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFile = new File(Environment.getExternalStorageDirectory(), "filename.png");
                    downloadAndSaveSampleImage();
                } else {
                    checkWriteExternalStoragePermission();
                }
                return;
            }
        }
    }
}
