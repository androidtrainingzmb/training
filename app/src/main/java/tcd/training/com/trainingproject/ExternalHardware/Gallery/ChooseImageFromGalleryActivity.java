package tcd.training.com.trainingproject.ExternalHardware.Gallery;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tcd.training.com.trainingproject.R;

public class ChooseImageFromGalleryActivity extends AppCompatActivity {

    private static final String TAG = ChooseImageFromGalleryActivity.class.getSimpleName();

    private static final int SLIDE_SHOW_INTERVAL = 5000;
    private static final int PICK_IMAGE_USING_ACTION_GET_CONTENT = 1;
    private static final int PICK_IMAGE_USING_CUSTOM_GALLERY = 2;

    // view pager
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<String> mImagesPathList;
    private int mCurrentPage;

    // other UI components
    private Button mUsingActionGetContentButton;
    private Button mUsingCustomGalleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image_from_gallery);

        initializeViewPagerComponents();
        initializeUiComponents();
    }

    private void initializeUiComponents() {
        mUsingActionGetContentButton = findViewById(R.id.btn_pick_using_action_get_content);
        mUsingActionGetContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImagesUsingActionGetContent();
            }
        });

        mUsingCustomGalleryButton = findViewById(R.id.btn_pick_using_custom_gallery);
        mUsingCustomGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImagesUsingCustomGallery();
            }
        });
    }
    
    private void pickImagesUsingActionGetContent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        Intent chooserIntent = Intent.createChooser(intent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(chooserIntent, PICK_IMAGE_USING_ACTION_GET_CONTENT);
    }

    private void pickImagesUsingCustomGallery() {
        Intent intent = new Intent(this, CustomGalleryActivity.class);
        startActivityForResult(intent, PICK_IMAGE_USING_CUSTOM_GALLERY);
    }

    private void initializeViewPagerComponents() {
        mImagesPathList = new ArrayList<>();
        mViewPagerAdapter = new ViewPagerAdapter(this, mImagesPathList);
        mViewPager = findViewById(R.id.vp_slide_show);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_USING_ACTION_GET_CONTENT && resultCode == RESULT_OK && data != null) {
                // Get the Image from data
                if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    String imagePath = getPathFromContentUri(imageUri);
                    mImagesPathList.add(imagePath);
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            Uri imageUri = mClipData.getItemAt(i).getUri();
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            String imagePath = getPathFromContentUri(imageUri);
                            mImagesPathList.add(imagePath);
                        }
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked any image yet", Toast.LENGTH_LONG).show();
            }
            // results
            mViewPagerAdapter.notifyDataSetChanged();
            for (String path : mImagesPathList) {
                Log.d(TAG, "onActivityResult: " + path);
            }
            startSlideShow();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private String getPathFromContentUri(Uri uri) {
        // get the document ID
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String wholeId = cursor.getString(0);
        // get the id
        String id = wholeId.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);
        // and now the file path
        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        // clean up
        cursor.close();
        return filePath;
    }

    private void startSlideShow() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (mCurrentPage == mImagesPathList.size()) {
                    mCurrentPage = 0;
                }
                mViewPager.setCurrentItem(mCurrentPage++, true);
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, SLIDE_SHOW_INTERVAL);
    }
}
