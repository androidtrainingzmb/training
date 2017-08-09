package tcd.training.com.trainingproject.Fragments.FlexibleUIWithFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Stack;

import tcd.training.com.trainingproject.R;

import static tcd.training.com.trainingproject.Fragments.FragmentTopicActivity.mPageTitles;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitlesFragment extends ListFragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private boolean mDualPane;
    private int mCurCheckPosition = 0;
    private Stack<Integer> mIndexStack;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1);
        arrayAdapter.addAll(mPageTitles);
        setListAdapter(arrayAdapter);

        mIndexStack = new Stack<>();

        // check to see if we have the details fragment in this screen
        View detailsFrameView = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrameView != null && detailsFrameView.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        showDetails(position);
    }

    private void showDetails(int index) {

        if (index == mCurCheckPosition && mIndexStack.size() > 0) {
            return;
        }

        // update current value
        mCurCheckPosition = index;
        mIndexStack.add(index);

        if (mDualPane) {
            getListView().setItemChecked(index, true);

            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
            // check what fragment is currently shown, replace if needed
            if (detailsFragment == null || detailsFragment.getPageIndex() != index) {
                detailsFragment = DetailsFragment.newInstance(index);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.details, detailsFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        } else {
            // open a new activity contains that fragment
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(ARG_PAGE, index);
            startActivity(intent);
        }
    }

    public void updateSelectionState() {
        // pop the index stack
        if (mIndexStack.size() > 0) {
            // check if it is the current position
            if (mIndexStack.peek() == mCurCheckPosition) {
                mIndexStack.pop();
                if (mIndexStack.size() == 0) {
                    return;
                }
            }
            mCurCheckPosition = mIndexStack.pop();
            getListView().setItemChecked(mCurCheckPosition, true);
        }
    }
}
