package tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments;

import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import tcd.training.com.trainingproject.FragmentsDemo.TabPageFragment;
import tcd.training.com.trainingproject.R;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.ICommunicate {

    private static final String TAG_LOG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(android.R.id.content, detailsFragment, "fragment").commit();
            getSupportFragmentManager().executePendingTransactions();
            ((DetailsFragment)getSupportFragmentManager().findFragmentByTag("fragment")).setPortraitMode(true);
        }
    }

    @Override
    public void passDataToActivity(int index) {

    }
}
