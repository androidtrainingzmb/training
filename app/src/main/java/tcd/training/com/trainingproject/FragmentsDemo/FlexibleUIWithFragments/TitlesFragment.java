package tcd.training.com.trainingproject.FragmentsDemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import tcd.training.com.trainingproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitlesFragment extends ListFragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private boolean mDualPane;
    private int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1));

        // check to see if we have the details fragment in this screen
        View detailsFrameView = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrameView != null && detailsFrameView.getVisibility() == View.VISIBLE;

        if (mDualPane) {
            getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        showDetails(position);
    }

    private void showDetails(int index) {

        mCurCheckPosition = index;

        if (mDualPane) {
            getListView().setItemChecked(index, true);

            TabPageFragment detailsFragment = (TabPageFragment) getFragmentManager().findFragmentById(R.id.details);
            // check what fragment is currently shown, replace if needed
            if (detailsFragment == null || detailsFragment.getPageIndex() != index) {
                detailsFragment = TabPageFragment.newInstance(index);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (index == 0) {
                    transaction.replace(R.id.details, detailsFragment);
                } else {
//                    transaction.replace(R.id.a_item, detailsFragment);
                }
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            } else {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(ARG_PAGE, index);
                startActivity(intent);
            }
        }

    }
}
