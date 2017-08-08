package tcd.training.com.trainingproject.ImagesTopic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

import tcd.training.com.trainingproject.R;

public class ImageViewScaleTypeDemoActivity extends AppCompatActivity {

    private Switch mImageInPortraitSwitch;
    private Spinner mScaleTypeSpinner;
    private ImageView mImageView;
    private Switch mAdjustViewBoundsSwitch;

    enum ImageSize {SMALL, MEDIUM, LARGE};
    private ImageSize mImageSize;
    private boolean mIsImageInPortraitMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_scale_type_demo);

        initializeUiComponents();

        mImageInPortraitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mIsImageInPortraitMode = isChecked;
                updateImageView();
            }
        });

        mAdjustViewBoundsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                refreshImageView();
            }
        });

        mScaleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshImageView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        refreshImageView();
    }

    private void initializeUiComponents() {
        mImageInPortraitSwitch = findViewById(R.id.sw_portrait_mode);
        mScaleTypeSpinner = findViewById(R.id.sp_scale_type);
        mImageView = findViewById(R.id.iv_image_demo);
        mAdjustViewBoundsSwitch = findViewById(R.id.sw_adjust_view_bounds);

        mIsImageInPortraitMode = mImageInPortraitSwitch.isChecked();
        if (((RadioButton)findViewById(R.id.rb_small_image)).isChecked()) {
            mImageSize = ImageSize.SMALL;
        } else if (((RadioButton)findViewById(R.id.rb_medium_image)).isChecked()) {
            mImageSize = ImageSize.MEDIUM;
        } else {
            mImageSize = ImageSize.LARGE;
        }
        updateImageView();
    }

    private void refreshImageView() {
        updateImageView();
        updateScaleType((String) mScaleTypeSpinner.getSelectedItem());
        mImageView.setAdjustViewBounds(mAdjustViewBoundsSwitch.isChecked());
    }

    private void updateScaleType(String scaleType) {
        ImageView.ScaleType type = null;
        switch (scaleType) {
            case "center": type = ImageView.ScaleType.CENTER; break;
            case "centerCrop": type = ImageView.ScaleType.CENTER_CROP; break;
            case "centerInside": type = ImageView.ScaleType.CENTER_INSIDE; break;
            case "fitCenter": type = ImageView.ScaleType.FIT_CENTER; break;
            case "fitStart": type = ImageView.ScaleType.FIT_START; break;
            case "fitEnd": type = ImageView.ScaleType.FIT_END; break;
            case "fitXY": type = ImageView.ScaleType.FIT_XY; break;
            case "matrix": type = ImageView.ScaleType.MATRIX; break;
        }
        mImageView.setScaleType(type);
        mImageView.setAdjustViewBounds(mAdjustViewBoundsSwitch.isChecked());
    }

    private void updateImageView() {
        int imageId = R.drawable.portrait_small;
        if (mImageSize == ImageSize.SMALL) {
            if (mIsImageInPortraitMode) {
                imageId = R.drawable.portrait_small;
            } else {
                imageId = R.drawable.landscape_small;
            }
        } else if (mImageSize == ImageSize.MEDIUM) {
            if (mIsImageInPortraitMode) {
                imageId = R.drawable.portrait_medium;
            } else {
                imageId = R.drawable.landscape_medium;
            }
        } else if (mImageSize == ImageSize.LARGE){
            if (mIsImageInPortraitMode) {
                imageId = R.drawable.portrait_large;
            } else {
                imageId = R.drawable.landscape_large;
            }
        }
        mImageView.setImageResource(imageId);
    }

    public void onRadioButtonClicked(View view) {
        if (((RadioButton)view).isChecked()) {
            switch (view.getId()) {
                case R.id.rb_small_image:
                    mImageSize = ImageSize.SMALL;
                    break;
                case R.id.rb_large_image:
                    mImageSize = ImageSize.LARGE;
                    break;
                case R.id.rb_medium_image:
                    mImageSize = ImageSize.MEDIUM;
                    break;
            }
            updateImageView();
        }
    }
}
