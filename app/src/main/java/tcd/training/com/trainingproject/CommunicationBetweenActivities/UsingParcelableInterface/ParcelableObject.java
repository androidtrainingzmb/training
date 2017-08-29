package tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingParcelableInterface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.DemoObject;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class ParcelableObject extends DemoObject implements Parcelable {

    public ParcelableObject(int mInteger, double mDouble, boolean mBoolean, String mString, ArrayList<String> mArrayList, HashMap<String, String> mHashMap) {
        super(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
    }

    private ParcelableObject(Parcel in) {
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
}
