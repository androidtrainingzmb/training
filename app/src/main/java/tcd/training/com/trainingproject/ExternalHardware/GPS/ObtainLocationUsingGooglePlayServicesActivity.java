package tcd.training.com.trainingproject.ExternalHardware.GPS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import tcd.training.com.trainingproject.R;

public class ObtainLocationUsingGooglePlayServicesActivity extends AppCompatActivity {

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private ImageView mMapImageView;
    private Button mOpenMapButton;

    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtain_location);

        initializeUiComponents();
        initializeBasicComponents();

        createLocationRequest();
        initializeLocationUpdateCallback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void initializeUiComponents() {
        mLatitudeTextView = findViewById(R.id.tv_latitude);
        mLongitudeTextView = findViewById(R.id.tv_longitude);
        mMapImageView = findViewById(R.id.iv_map);
        mOpenMapButton = findViewById(R.id.btn_open_map);

        findViewById(R.id.rg_provider).setVisibility(View.GONE);
    }

    private void initializeBasicComponents() {
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initializeLocationUpdateCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
//                for (Location location : locationResult.getLocations()) {
//
//                }
                Location lastLocation = locationResult.getLastLocation();
                final String latitude = String.valueOf(lastLocation.getLatitude());
                final String longitude = String.valueOf(lastLocation.getLongitude());
                mLatitudeTextView.setText(latitude);
                mLongitudeTextView.setText(longitude);

                String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
                        + latitude + "," + longitude + "&zoom=16&size=1000x300";
                Glide.with(ObtainLocationUsingGooglePlayServicesActivity.this).load(imageUrl).into(mMapImageView);

                mOpenMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUrl));
                        startActivity(intent);
                    }
                });
                mOpenMapButton.setVisibility(View.VISIBLE);
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
}
