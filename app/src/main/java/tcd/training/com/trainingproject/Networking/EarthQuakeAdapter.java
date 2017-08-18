package tcd.training.com.trainingproject.Networking;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 18/08/2017.
 */

public class EarthQuakeAdapter extends RecyclerView.Adapter<EarthQuakeAdapter.EarthQuakeViewHolder> {

    private Context mContext;
    private ArrayList<EarthQuake> mEarthQuakeList;

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuake> mEarthQuakeList) {
        mContext = context;
        this.mEarthQuakeList = mEarthQuakeList;
    }

    public void clear() {
        mEarthQuakeList.clear();
    }

    @Override
    public EarthQuakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        return new EarthQuakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EarthQuakeViewHolder holder, int position) {
        EarthQuake earthQuake = mEarthQuakeList.get(position);

        DecimalFormat formatter = new DecimalFormat("0.0");
        holder.mMagnitudeTextView.setText(formatter.format(earthQuake.getMagnitude()));
        holder.mMagnitudeTextView.setTextColor(getMagnitudeColor(earthQuake.getMagnitude()));


        holder.mLocationOffsetTextView.setText(earthQuake.getLocationOffset(mContext));
        holder.mPlaceTextView.setText(earthQuake.getPrimaryLocation());
        holder.mDateTextView.setText(earthQuake.getDate());
        holder.mTimeTextView.setText(earthQuake.getTime());
    }

    @Override
    public int getItemCount() {
        return mEarthQuakeList.size();
    }

    class EarthQuakeViewHolder extends RecyclerView.ViewHolder {

        private TextView mMagnitudeTextView;
        private TextView mLocationOffsetTextView;
        private TextView mPlaceTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;

        public EarthQuakeViewHolder(View itemView) {
            super(itemView);

            mMagnitudeTextView = itemView.findViewById(R.id.tv_magnitude);
            mLocationOffsetTextView = itemView.findViewById(R.id.tv_location_offset);
            mPlaceTextView = itemView.findViewById(R.id.tv_primary_location);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mTimeTextView = itemView.findViewById(R.id.tv_time);
        }
    }

    private int getMagnitudeColor(float magnitude) {
        int magnitudeColorResourceId;
        switch ((int) Math.floor(magnitude)) {
            case 0:
            case 1: magnitudeColorResourceId = R.color.magnitude1; break;
            case 2: magnitudeColorResourceId = R.color.magnitude2; break;
            case 3: magnitudeColorResourceId = R.color.magnitude3; break;
            case 4: magnitudeColorResourceId = R.color.magnitude4; break;
            case 5: magnitudeColorResourceId = R.color.magnitude5; break;
            case 6: magnitudeColorResourceId = R.color.magnitude6; break;
            case 7: magnitudeColorResourceId = R.color.magnitude7; break;
            case 8: magnitudeColorResourceId = R.color.magnitude8; break;
            case 9: magnitudeColorResourceId = R.color.magnitude9; break;
            default: magnitudeColorResourceId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(mContext, magnitudeColorResourceId);
    }
}
