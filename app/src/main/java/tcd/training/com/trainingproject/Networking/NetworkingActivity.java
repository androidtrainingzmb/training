package tcd.training.com.trainingproject.Networking;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import tcd.training.com.trainingproject.R;

public class NetworkingActivity extends AppCompatActivity {

    private static final String TAG = NetworkingActivity.class.getSimpleName();

    private final int USING_HTTP_URL_CONNECTION_METHOD = 1;
    private final int USING_OK_HTTP__METHOD = 2;
    private final int USING_VOLLEY_LIBRARY = 3;
    private final int USING_ANY_CONNECTION = 4;
    private final int USING_MOBILE_DATA = 5;
    private final int USING_WIFI = 6;

    private final int READ_TIMEOUT = 10000;
    private final int CONNECT_TIMEOUT = 15000;

    private EarthQuakeAdapter mEarthQuakeAdapter;
    private ArrayList<EarthQuake> mEarthQuakeArrayList;
    private ProgressBar mProgressBar;
    private TextView mDownloadStatusTextView;

    private int mConnectMethod;
    private int mConnectionType;
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);

        initializeUiComponents();

        determineRetrieveSpecs();

        retrieveData();
    }

    private void initializeUiComponents() {
        // recycler view
        RecyclerView earthquakeRecyclerView = findViewById(R.id.rv_earthquakes_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        earthquakeRecyclerView.setLayoutManager(layoutManager);

        earthquakeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mEarthQuakeArrayList = new ArrayList<>();
        mEarthQuakeAdapter = new EarthQuakeAdapter(this, mEarthQuakeArrayList);
        earthquakeRecyclerView.setAdapter(mEarthQuakeAdapter);

        // others
        mProgressBar = findViewById(R.id.pb_download_progress);
        mDownloadStatusTextView = findViewById(R.id.tv_download_status);
    }

    private void determineRetrieveSpecs() {
        // download methods
        if (((RadioButton)findViewById(R.id.rb_http_url_connection)).isChecked()) {
            mConnectMethod = USING_HTTP_URL_CONNECTION_METHOD;
        } else if (((RadioButton)findViewById(R.id.rb_ok_http)).isChecked()) {
            mConnectMethod = USING_OK_HTTP__METHOD;
        } else if (((RadioButton)findViewById(R.id.rb_volley_library)).isChecked()) {
            mConnectMethod = USING_VOLLEY_LIBRARY;
        }
        // connection types
        if (((RadioButton)findViewById(R.id.rb_any)).isChecked()) {
            mConnectionType = USING_ANY_CONNECTION;
        } else if (((RadioButton)findViewById(R.id.rb_mobile_data)).isChecked()) {
            mConnectionType = USING_MOBILE_DATA;
        } else if (((RadioButton)findViewById(R.id.rb_wifi)).isChecked()) {
            mConnectionType = USING_WIFI;
        }
    }

    private boolean isRequiredNetworkAvailable() {
        ConnectivityManager conn =  (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else {
            switch (mConnectionType) {
                case USING_ANY_CONNECTION:
                    return true;
                case USING_MOBILE_DATA:
                    return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
                case USING_WIFI:
                    return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                default:
                    return false;
            }
        }
    }

    private void retrieveData() {

        if (!isRequiredNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.network_not_available), Toast.LENGTH_SHORT).show();
            return;
        }

        prepareDownload();
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-08-01&endtime=2017-08-31&minmag=4";
        switch (mConnectMethod) {
            case USING_HTTP_URL_CONNECTION_METHOD:
                retrieveDataUsingHttpUrlConnection(url);
                break;
            case USING_OK_HTTP__METHOD:
                retrieveDataUsingOkHttpLibrary(url);
                break;
            case USING_VOLLEY_LIBRARY:
                retrieveDataUsingVolleyLibrary(url);
                break;
        }
    }

    private void prepareDownload() {
        mProgressBar.setVisibility(View.VISIBLE);
        mDownloadStatusTextView.setText(getString(R.string.downloading));

        mStartTime = Calendar.getInstance().getTimeInMillis();
    }

    private void retrieveDataUsingOkHttpLibrary(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                String jsonResponse = response.body().string();
                readEarthquakesFromJsonResponse(jsonResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUiAfterFetchingData();
                    }
                });
            }
        });
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
                        Log.d(TAG, "onResponse: ");
                        readEarthquakesFromJsonResponse(response);
                        updateUiAfterFetchingData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");
                    }
                });

        queue.add(stringRequest);
    }

    private class RetrieveEarthquakesTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(READ_TIMEOUT);
                urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();
                    String jsonResponse = readFromStream(inputStream);
                    readEarthquakesFromJsonResponse(jsonResponse);
                }

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
            updateUiAfterFetchingData();
        }
    }

    private void updateUiAfterFetchingData() {
        mEarthQuakeAdapter.notifyDataSetChanged();

        mProgressBar.setVisibility(View.GONE);

        long elapsedTime = Calendar.getInstance().getTimeInMillis() - mStartTime;
        mDownloadStatusTextView.setText("Found " + mEarthQuakeArrayList.size() + " result(s) in " + elapsedTime + "ms");

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
                mConnectMethod = USING_HTTP_URL_CONNECTION_METHOD;
                break;
            case R.id.rb_volley_library:
                mConnectMethod = USING_VOLLEY_LIBRARY;
                break;
            case R.id.rb_ok_http:
                mConnectMethod = USING_OK_HTTP__METHOD;
                break;
            case R.id.rb_any:
                mConnectionType = USING_ANY_CONNECTION;
                break;
            case R.id.rb_mobile_data:
                mConnectionType = USING_MOBILE_DATA;
                break;
            case R.id.rb_wifi:
                mConnectionType = USING_WIFI;
                break;
        }
        mEarthQuakeAdapter.clear();
        mEarthQuakeAdapter.notifyDataSetChanged();
        retrieveData();
    }
}
