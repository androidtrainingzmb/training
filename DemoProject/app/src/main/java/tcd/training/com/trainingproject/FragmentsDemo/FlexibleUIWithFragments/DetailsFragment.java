package tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import tcd.training.com.trainingproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private ICommunicate mCommunicate;
    private boolean mIsPortraitMode = false;

    private Button sendDataUsingInterfaceButton;
    private Button sendDataUsingGetActivityButton;
    private Button sendDataUsingLocalBroadcastReceiverButton;
    private TextView tapSendDataMessageTextView;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DetailsFragment fragment = new DetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // initialize components
        TextView pageNumberIndicatorTextView = view.findViewById(R.id.tv_page_number_indicator);
        pageNumberIndicatorTextView.setText("Fragment #" + mPage);
        sendDataUsingInterfaceButton = view.findViewById(R.id.btn_send_data_using_interface);
        sendDataUsingGetActivityButton = view.findViewById(R.id.btn_send_data_using_get_activity);
        sendDataUsingLocalBroadcastReceiverButton = view.findViewById(R.id.btn_send_data_using_local_broadcast_receiver);
        tapSendDataMessageTextView = view.findViewById(R.id.tv_tap_send_data_message);

        // button appears in landscape mode
        if (mIsPortraitMode) {
            sendDataUsingInterfaceButton.setVisibility(View.GONE);
            tapSendDataMessageTextView.setVisibility(View.GONE);
        } else {
            sendDataUsingInterfaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCommunicate.passDataToActivity(mPage);
                }
            });
            sendDataUsingGetActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((FlexibleUIWithFragmentsActivity)getActivity()).updateIndicatorTextView(mPage);
                }
            });
            sendDataUsingLocalBroadcastReceiverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getString(R.string.send_data_to_activity_intent_name));
                    intent.putExtra(getString(R.string.integer_type), mPage);
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }
            });
        }

        return view;
    }

    public void setPortraitMode(boolean isPortraitMode) {
        mIsPortraitMode = isPortraitMode;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCommunicate = (ICommunicate) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ICommunicate");
        }
    }

    public interface ICommunicate {
        void passDataToActivity(int index);
    }
}
