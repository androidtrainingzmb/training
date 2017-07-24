package tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingParcelableInterface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class ParcelableObject implements Parcelable {

    private int mInteger;
    private double mDouble;
    private boolean mBoolean;
    private String mString;
    private ArrayList<String> mArrayList;
    private HashMap<String, String> mHashMap;

    public ParcelableObject(int mInteger, double mDouble, boolean mBoolean, String mString, ArrayList<String> mArrayList, HashMap<String, String> mHashMap) {
        this.mInteger = mInteger;
        this.mDouble = mDouble;
        this.mBoolean = mBoolean;
        this.mString = mString;
        this.mArrayList = mArrayList;
        this.mHashMap = mHashMap;
    }

    protected ParcelableObject(Parcel in) {
        mInteger = in.readInt();
        mDouble = in.readDouble();
        mBoolean = in.readByte() != 0;
        mString = in.readString();
        mArrayList = in.createStringArrayList();
        mHashMap = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<ParcelableObject> CREATOR = new Creator<ParcelableObject>() {
        @Override
        public ParcelableObject createFromParcel(Parcel in) {
            return new ParcelableObject(in);
        }

        @Override
        public ParcelableObject[] newArray(int size) {
            return new ParcelableObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mInteger);
        parcel.writeDouble(mDouble);
        parcel.writeByte((byte) (mBoolean ? 1 : 0));
        parcel.writeString(mString);
        parcel.writeStringList(mArrayList);
        parcel.writeSerializable(mHashMap);
    }

    public int getmInteger() {
        return mInteger;
    }

    public void setmInteger(int mInteger) {
        this.mInteger = mInteger;
    }

    public double getmDouble() {
        return mDouble;
    }

    public void setmDouble(double mDouble) {
        this.mDouble = mDouble;
    }

    public boolean ismBoolean() {
        return mBoolean;
    }

    public void setmBoolean(boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public String getmString() {
        return mString;
    }

    public void setmString(String mString) {
        this.mString = mString;
    }

    public ArrayList<String> getmArrayList() {
        return mArrayList;
    }

    public void setmArrayList(ArrayList<String> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public HashMap<String, String> getmHashMap() {
        return mHashMap;
    }

    public void setmHashMap(HashMap<String, String> mHashMap) {
        this.mHashMap = mHashMap;
    }
}
