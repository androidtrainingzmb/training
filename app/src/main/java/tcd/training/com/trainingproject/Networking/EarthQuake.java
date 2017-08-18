package tcd.training.com.trainingproject.Networking;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 18/08/2017.
 */

public class EarthQuake {
    private static final String LOCATION_SEPERATOR = " of ";

    private float mMagnitude;
    private String mPlace;
    private long mTime;

    public EarthQuake(float magnitude, String place, long time) {
        this.mMagnitude = magnitude;
        this.mPlace = place;
        this.mTime = time;
    }

    public float getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(float magnitude) {
        this.mMagnitude = magnitude;
    }

    public void setPlace(String place) {
        this.mPlace = place;
    }

    public String getLocationOffset(Context context) {
        return mPlace.contains(LOCATION_SEPERATOR)
                ? mPlace.split(LOCATION_SEPERATOR)[0] + LOCATION_SEPERATOR
                : context.getString(R.string.near_the);
    }

    public String getPrimaryLocation() {
        return mPlace.contains(LOCATION_SEPERATOR)
                ? mPlace.split(LOCATION_SEPERATOR)[1]
                : mPlace;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        return dateFormat.format(calendar.getTime());
    }


    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(calendar.getTime());
    }
}
