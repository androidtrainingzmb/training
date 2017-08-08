package tcd.training.com.trainingproject.FragmentsDemo.TabLayoutDemo;

import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcd.training.com.trainingproject.R;

public class TabLayoutWithFragmentsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout_with_fragment_demo);

        // set up viewPager
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabPageFragmentPagerAdapter(getSupportFragmentManager(), this));

        // set up the tabLayout
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // different tab modes for different orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
    }
}
