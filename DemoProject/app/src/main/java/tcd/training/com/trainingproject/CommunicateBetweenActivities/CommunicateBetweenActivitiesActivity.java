package tcd.training.com.trainingproject.CommunicateBetweenActivities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingBundle.ReceivePrimitiveTypesUsingBundleActivity;
import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingGson.ReceiveGsonObjectActivity;
import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingParcelableInterface.ParcelableObject;
import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingParcelableInterface.ReceiveObjectWithParcelableInterface;
import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingSerializeInterface.ReceiveObjectWithSerializableActivity;
import tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingSerializeInterface.SerializableObject;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;
import tcd.training.com.trainingproject.R;

public class CommunicateBetweenActivitiesActivity extends AppCompatActivity {

    private ListView mTopicsListView;
    private ArrayList<String> mTopicsList;


    private int mInteger;
    private double mDouble;
    private String mString;
    private boolean mBoolean;
    private ArrayList<String> mArrayList;
    private HashMap<String, String> mHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate_between_activities);

        initializeDataSet();

        mTopicsListView = findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTopicsList);
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = mTopicsList.get(i);
                Log.e("aaa", "onItemClick: " + key);
                Intent intent = new Intent();
                Context context = CommunicateBetweenActivitiesActivity.this;
                if (key.contains("Bundle")) {
                    intent.setClass(context, ReceivePrimitiveTypesUsingBundleActivity.class);
                    Bundle bundle = getBundle();
                    intent.putExtra(getString(R.string.data), bundle);
                } else if (key.contains("Serializable")) {
                    intent.setClass(context, ReceiveObjectWithSerializableActivity.class);
                    SerializableObject object = new SerializableObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), object);
                }  else if (key.contains("Parcelable")) {
                    intent.setClass(context, ReceiveObjectWithParcelableInterface.class);
                    ParcelableObject object = new ParcelableObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), object);
                } else if (key.contains("GSon")) {
                    intent.setClass(context, ReceiveGsonObjectActivity.class);
                    SerializableObject object = new SerializableObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), new Gson().toJson(object));
                }  else if (key.contains("Intent filter")) {

                } else {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    private void initializeDataSet() {
        mTopicsList = new ArrayList<>(Arrays.asList(
                "1. Send primitive types using Bundle",
                "2. Send object with Serializable interface",
                "3. Send object with Parcelable interface",
                "4. Send object by serializing object into string (using GSon)",
                "5. Using storage option (see topic 4 in MainActivity)",
                "6. Using global variables (not recommended)",
                "7. Using Application object (not recommended)",
                "8. Send data between applications using Intent filter"
        ));

        mInteger = 123;
        mDouble = 123.4;
        mBoolean = true;
        mString = "Testing";
        mArrayList = new ArrayList<>(Arrays.asList("String 1", "String 2"));
        mHashMap = new HashMap<>();
        mHashMap.put("key 1", "value 1");
        mHashMap.put("key 2", "value 1");
    }


    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.integer_type), mInteger);
        bundle.putDouble(getString(R.string.double_type), mDouble);
        bundle.putString(getString(R.string.string_type), mString);
        bundle.putBoolean(getString(R.string.boolean_type), mBoolean);
        bundle.putStringArrayList(getString(R.string.array_list_type), mArrayList);
        bundle.putSerializable(getString(R.string.hash_map_type), mHashMap);
        return bundle;
    }
}
