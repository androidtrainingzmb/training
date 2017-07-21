package tcd.training.com.trainingproject.FragmentsDemo.PagerTabStripDemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import tcd.training.com.trainingproject.FragmentsDemo.TabLayoutDemo.TabPageFragmentPagerAdapter;
import tcd.training.com.trainingproject.R;

public class PagerTabStripWithFragmentsActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_tab_strip_with_fragment_demo);

        // set up mViewPager
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TabPageFragmentPagerAdapter(getSupportFragmentManager(), this));
    }
}
