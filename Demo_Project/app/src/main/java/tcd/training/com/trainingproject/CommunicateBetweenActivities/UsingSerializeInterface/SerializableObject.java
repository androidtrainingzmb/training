package tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingSerializeInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class SerializableObject implements Serializable {
    private int mInteger;
    private double mDouble;
    private boolean mBoolean;
    private String mString;
    private ArrayList<String> mArrayList;
    private HashMap<String, String> mHashMap;

    public SerializableObject() {

    }

    public SerializableObject(int mInteger, double mDouble, boolean mBoolean, String mString, ArrayList<String> mArrayList, HashMap<String, String> mHashMap) {
        this.mInteger = mInteger;
        this.mDouble = mDouble;
        this.mBoolean = mBoolean;
        this.mString = mString;
        this.mArrayList = mArrayList;
        this.mHashMap = mHashMap;
    }

    public void setmInteger(int mInteger) {
        this.mInteger = mInteger;
    }

    public void setmDouble(double mDouble) {
        this.mDouble = mDouble;
    }

    public void setmBoolean(boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public void setmString(String mString) {
        this.mString = mString;
    }

    public void setmArrayList(ArrayList<String> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public void setmHashMap(HashMap<String, String> mHashMap) {
        this.mHashMap = mHashMap;
    }

    public int getmInteger() {
        return mInteger;
    }

    public double getmDouble() {
        return mDouble;
    }

    public String getmString() {
        return mString;
    }

    public boolean ismBoolean() {
        return mBoolean;
    }

    public ArrayList<String> getmArrayList() {
        return mArrayList;
    }

    public HashMap<String, String> getmHashMap() {
        return mHashMap;
    }
}
