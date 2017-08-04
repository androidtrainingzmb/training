package tcd.training.com.trainingproject.ImagesTopic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    
    private boolean mIsImageSmall;
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
                mImageView.setAdjustViewBounds(isChecked);
            }
        });

        mScaleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView.ScaleType scaleType = null;
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case "center": scaleType = ImageView.ScaleType.CENTER; break;
                    case "centerCrop": scaleType = ImageView.ScaleType.CENTER_CROP; break;
                    case "centerInside": scaleType = ImageView.ScaleType.CENTER_INSIDE; break;
                    case "fitCenter": scaleType = ImageView.ScaleType.FIT_CENTER; break;
                    case "fitStart": scaleType = ImageView.ScaleType.FIT_START; break;
                    case "fitEnd": scaleType = ImageView.ScaleType.FIT_END; break;
                    case "fitXY": scaleType = ImageView.ScaleType.FIT_XY; break;
                    case "matrix": scaleType = ImageView.ScaleType.MATRIX; break;
                }
                mImageView.setScaleType(scaleType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializeUiComponents() {
        mImageInPortraitSwitch = findViewById(R.id.sw_portrait_mode);
        mScaleTypeSpinner = findViewById(R.id.sp_scale_type);
        mImageView = findViewById(R.id.iv_image_demo);
        mAdjustViewBoundsSwitch = findViewById(R.id.sw_adjust_view_bounds);

        mIsImageInPortraitMode = mImageInPortraitSwitch.isChecked();
        mIsImageSmall = ((RadioButton)findViewById(R.id.rb_small_image)).isChecked();
        updateImageView();
    }

    private void updateImageView() {
        int imageId;
        if (mIsImageSmall) {
            if (mIsImageInPortraitMode) {
                imageId = R.drawable.portrait_small;
            } else {
                imageId = R.drawable.landscape_small;
            }
        } else {
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
                    mIsImageSmall = true;
                    break;
                case R.id.rb_large_image:
                    mIsImageSmall = false;
                    break;
            }
            updateImageView();
        }
    }
}
