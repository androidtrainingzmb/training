package tcd.training.com.trainingproject.CommunicationBetweenActivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingApplicationObject.MyApplication;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingApplicationObject.ReceiveDataWithApplicationObject;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingBundle.ReceiveDataWithBundleActivity;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingGson.ReceiveGsonObjectActivity;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingOnlyIntent.ReceiveIntentDataActivity;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingParcelableInterface.ParcelableObject;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingParcelableInterface.ReceiveObjectWithParcelableInterface;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingSerializeInterface.ReceiveObjectWithSerializableInterfaceActivity;
import tcd.training.com.trainingproject.CommunicationBetweenActivities.UsingSerializeInterface.SerializableObject;
import tcd.training.com.trainingproject.FragmentsDemo.FlexibleUIWithFragments.FlexibleUIWithFragmentsActivity;
import tcd.training.com.trainingproject.PersistentStorage.PersistentStorageActivity;
import tcd.training.com.trainingproject.R;

public class CommunicationBetweenActivitiesActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_communication_between_activities);

        initializeDataSet();

        mTopicsListView = findViewById(R.id.lv_topics_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTopicsList);
        mTopicsListView.setAdapter(arrayAdapter);
        mTopicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = mTopicsList.get(i);
                Intent intent = new Intent();
                Context context = CommunicationBetweenActivitiesActivity.this;
                if (key.endsWith("Using intent")) {
                    intent = getIntentWithDataExtras();
                    intent.setClass(context, ReceiveIntentDataActivity.class);
                } else if (key.contains("Bundle")) {
                    intent.setClass(context, ReceiveDataWithBundleActivity.class);
                    Bundle bundle = getBundle();
                    intent.putExtra(getString(R.string.data), bundle);
                } else if (key.contains("Serializable")) {
                    intent.setClass(context, ReceiveObjectWithSerializableInterfaceActivity.class);
                    SerializableObject object = new SerializableObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), object);
                }  else if (key.contains("Parcelable")) {
                    intent.setClass(context, ReceiveObjectWithParcelableInterface.class);
                    ParcelableObject object = new ParcelableObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), object);
                } else if (key.contains("GSon")) {
                    intent.setClass(context, ReceiveGsonObjectActivity.class);
                    DemoObject object = new DemoObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    intent.putExtra(getString(R.string.data), new Gson().toJson(object));
                }  else if (key.contains("storage")) {
                    intent.setClass(context, PersistentStorageActivity.class);
                }  else if (key.contains("Application")) {
                    intent.setClass(context, ReceiveDataWithApplicationObject.class);
                    DemoObject object = new DemoObject(mInteger, mDouble, mBoolean, mString, mArrayList, mHashMap);
                    ((MyApplication)getApplication()).setObject(object);
                } else if (key.contains("activity and fragment")) {
                    intent.setClass(context, FlexibleUIWithFragmentsActivity.class);
                } else {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    private Intent getIntentWithDataExtras() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.integer_type), mInteger);
        intent.putExtra(getString(R.string.double_type), mDouble);
        intent.putExtra(getString(R.string.boolean_type), mBoolean);
        intent.putExtra(getString(R.string.string_type), mString);
        intent.putExtra(getString(R.string.array_list_type), mArrayList);
        intent.putExtra(getString(R.string.hash_map_type), mHashMap);
        return intent;
    }

    private void initializeDataSet() {
        mTopicsList = new ArrayList<>(Arrays.asList(
                "A. Between Activities:",
                "  1. Using intent",
                "  2. Using intent with Bundle",
                "  3. Using intent with Serializable interface",
                "  4. Using intent with Parcelable interface",
                "  5. Using intent with string serialization (GSon)",
                "  6. Using storage option (see topic 4 in MainActivity)",
                "  7. Using Application object (not recommended)",
                "B. Between activity and fragment"
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

    public static void inflateLayout(View view, int intVal, boolean boolval, double doubleval,
                                     String stringVal, ArrayList arrayList, HashMap<String, String> hashMap) {

        ((TextView) view.findViewById(R.id.tv_integer_type)).setText(String.valueOf(intVal));
        ((TextView) view.findViewById(R.id.tv_double_type)).setText(String.valueOf(doubleval));
        ((TextView) view.findViewById(R.id.tv_string_type)).setText(stringVal);
        ((TextView) view.findViewById(R.id.tv_boolean_type)).setText(String.valueOf(boolval));

        // array list
        ListView arrayListListView = view.findViewById(R.id.lv_array_list_type);
        if (arrayList != null) {
            arrayListListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, arrayList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view.findViewById(android.R.id.text1)).setTextColor(
                            view.getResources().getColor(android.R.color.tab_indicator_text)
                    );
                    return view;
                }
            });
            LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, arrayList.size() * 150);
            arrayListListView.setLayoutParams(mParam);
        }


        // hash map
        ListView hashMapListView = view.findViewById(R.id.lv_hash_map_type);
        ArrayList<String> hashMapList = new ArrayList<>();
        if (hashMap != null) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                hashMapList.add(entry.getKey() + ": " + entry.getValue());
            }
            hashMapListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, hashMapList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    ((TextView) view.findViewById(android.R.id.text1)).setTextColor(
                            view.getResources().getColor(android.R.color.tab_indicator_text)
                    );
                    return view;
                }
            });
            LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hashMapList.size() * 150);
            hashMapListView.setLayoutParams(mParam);
        }
    }
}
