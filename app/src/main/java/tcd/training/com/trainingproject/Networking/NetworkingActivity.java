package tcd.training.com.trainingproject.Networking;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import javax.security.auth.login.LoginException;

import tcd.training.com.trainingproject.R;

public class NetworkingActivity extends AppCompatActivity {

    private static final String TAG = NetworkingActivity.class.getSimpleName();

    private final int USING_HTTP_URL_CONNECTION_METHOD = 1;
    private final int USING_VOLLEY_LIBRARY = 2;

    private int mUsingMethod = USING_VOLLEY_LIBRARY;

    private RecyclerView mEarthquakeRecyclerView;
    private EarthQuakeAdapter mEarthQuakeAdapter;
    private ArrayList<EarthQuake> mEarthQuakeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);

        initializeUiComponents();
        
        determineRetrieveMethod();

        retrieveData();
    }

    private void determineRetrieveMethod() {
        if (((RadioButton)findViewById(R.id.rb_http_url_connection)).isChecked()) {
            mUsingMethod = USING_HTTP_URL_CONNECTION_METHOD;
        } else if (((RadioButton)findViewById(R.id.rb_volley_library)).isChecked()) {
            mUsingMethod = USING_VOLLEY_LIBRARY;
        }
    }

    private void retrieveData() {
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10";
        switch (mUsingMethod) {
            case USING_HTTP_URL_CONNECTION_METHOD:
                retrieveDataUsingHttpUrlConnection(url);
                break;
            case USING_VOLLEY_LIBRARY:
                retrieveDataUsingVolleyLibrary(url);
                break;
        }
    }

    private void retrieveDataUsingHttpUrlConnection(String url) {
        new RetrieveEarthquakesTask().execute(url);
    }

    private void retrieveDataUsingVolleyLibrary(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        readEarthquakesFromJsonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ");
                    }
                });

        queue.add(stringRequest);
    }

    private void initializeUiComponents() {
        // recycler view
        mEarthquakeRecyclerView = findViewById(R.id.rv_earthquakes_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mEarthquakeRecyclerView.setLayoutManager(layoutManager);

        mEarthquakeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mEarthQuakeArrayList = new ArrayList<>();
        mEarthQuakeAdapter = new EarthQuakeAdapter(this, mEarthQuakeArrayList);
        mEarthquakeRecyclerView.setAdapter(mEarthQuakeAdapter);
    }

    class RetrieveEarthquakesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                if (url == null) {
                    return null;
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    String jsonResponse = readFromStream(inputStream);
                    Log.e(TAG, "doInBackground: " + jsonResponse);
                    readEarthquakesFromJsonResponse(jsonResponse);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mEarthQuakeAdapter.notifyDataSetChanged();
        }
    }

    private void readEarthquakesFromJsonResponse(String response) {
        try {
            JSONObject root = new JSONObject(response);
            JSONArray features = root.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                float magnitude = (float) properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                mEarthQuakeArrayList.add(new EarthQuake(magnitude, place, time));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readFromStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_http_url_connection:
                mUsingMethod = USING_HTTP_URL_CONNECTION_METHOD;
                break;
            case R.id.rb_volley_library:
                mUsingMethod = USING_VOLLEY_LIBRARY;
                break;
        }
        mEarthQuakeAdapter.clear();
        mEarthQuakeAdapter.notifyDataSetChanged();
        retrieveData();
    }
}
