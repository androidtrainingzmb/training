package tcd.training.com.trainingproject.CommunicateBetweenActivities.UsingBundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tcd.training.com.trainingproject.CommunicateBetweenActivities.Global;
import tcd.training.com.trainingproject.R;

public class ReceivePrimitiveTypesUsingBundleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_intent_data);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra(getString(R.string.data));

            int intVal = bundle.getInt(getString(R.string.integer_type));
            double doubleVal = bundle.getDouble(getString(R.string.double_type));
            String stringVal = bundle.getString(getString(R.string.string_type));
            boolean boolVal = bundle.getBoolean(getString(R.string.boolean_type));
            ArrayList<String> arrayList = bundle.getStringArrayList(getString(R.string.array_list_type));
            HashMap<String, String> hashMap = (HashMap<String, String>) bundle.getSerializable(getString(R.string.hash_map_type));

            Global.inflateLayout(findViewById(android.R.id.content), intVal, boolVal, doubleVal, stringVal, arrayList, hashMap);
        }
    }

}
