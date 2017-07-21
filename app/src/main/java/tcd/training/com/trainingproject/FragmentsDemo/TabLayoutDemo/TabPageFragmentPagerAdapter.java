package tcd.training.com.trainingproject.FragmentsDemo.TabLayoutDemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tcd.training.com.trainingproject.FragmentsDemo.TabPageFragment;

import static tcd.training.com.trainingproject.FragmentsDemo.FragmentsDemoActivity.mPageTitles;

/**
 * Created by cpu10661-local on 20/07/2017.
 */

public class TabPageFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabPageFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return TabPageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mPageTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitles[position];
    }
}
