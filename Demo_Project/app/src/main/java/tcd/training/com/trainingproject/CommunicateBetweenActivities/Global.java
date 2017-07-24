package tcd.training.com.trainingproject.CommunicateBetweenActivities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 24/07/2017.
 */

public class Global {

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
        }
        LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, arrayList.size() * 150);
        arrayListListView.setLayoutParams(mParam);

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
        }
        mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, hashMapList.size() * 150);
        hashMapListView.setLayoutParams(mParam);
    }
}
