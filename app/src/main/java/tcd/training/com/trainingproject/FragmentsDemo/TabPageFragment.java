package tcd.training.com.trainingproject.FragmentsDemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcd.training.com.trainingproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabPageFragment extends Fragment {


    private static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public TabPageFragment() {
        // Required empty public constructor
    }

    public static TabPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TabPageFragment fragment = new TabPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public int getPageIndex() {
        return mPage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_page, container, false);

        TextView pageNumberIndicatorTextView = view.findViewById(R.id.tv_tab_page_number_indicator);
        pageNumberIndicatorTextView.setText("Fragment #" + mPage);
        return view;
    }
}
