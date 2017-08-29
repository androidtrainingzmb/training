package tcd.training.com.trainingproject.Fragments.PagerTabStrip;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import tcd.training.com.trainingproject.Fragments.TabLayout.TabPageFragmentPagerAdapter;
import tcd.training.com.trainingproject.R;

public class PagerTabStripWithFragmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_tab_strip_with_fragment_demo);

        // set up mViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabPageFragmentPagerAdapter(getSupportFragmentManager(), this));
    }
}
