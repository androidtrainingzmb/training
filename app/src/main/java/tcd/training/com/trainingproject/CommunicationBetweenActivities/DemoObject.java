package tcd.training.com.trainingproject.CommunicationBetweenActivities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class DemoObject {

    protected int mInteger;
    protected double mDouble;
    protected boolean mBoolean;
    protected String mString;
    protected ArrayList<String> mArrayList;
    protected HashMap<String, String> mHashMap;

    public DemoObject() {

    }

    public DemoObject(int mInteger, double mDouble, boolean mBoolean, String mString, ArrayList<String> mArrayList, HashMap<String, String> mHashMap) {
        this.mInteger = mInteger;
        this.mDouble = mDouble;
        this.mBoolean = mBoolean;
        this.mString = mString;
        this.mArrayList = mArrayList;
        this.mHashMap = mHashMap;
    }

    public void setInteger(int mInteger) {
        this.mInteger = mInteger;
    }

    public void setDouble(double doubleVal) {
        this.mDouble = doubleVal;
    }

    public void setBoolean(boolean boolVal) {
        this.mBoolean = boolVal;
    }

    public void setString(String string) {
        this.mString = string;
    }

    public void setArrayList(ArrayList<String> mArrayList) {
        this.mArrayList = mArrayList;
    }

    public void setHashMap(HashMap<String, String> mHashMap) {
        this.mHashMap = mHashMap;
    }

    public int getInteger() {
        return mInteger;
    }

    public double getDouble() {
        return mDouble;
    }

    public String getString() {
        return mString;
    }

    public boolean isBoolean() {
        return mBoolean;
    }

    public ArrayList<String> getArrayList() {
        return mArrayList;
    }

    public HashMap<String, String> getHashMap() {
        return mHashMap;
    }
}
