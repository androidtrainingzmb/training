package tcd.training.com.trainingproject.ExternalHardware.Gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 11/08/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<String> mImagesPath;

    public ViewPagerAdapter(Context context, ArrayList<String> imagesPath) {
        this.mContext = context;
        mImagesPath = imagesPath;
    }

    @Override
    public int getCount() {
        return mImagesPath.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slide_show_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.iv_image_demo);
        Glide.with(mContext).load(mImagesPath.get(position)).into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((LinearLayout)object));
    }
}
