package tcd.training.com.trainingproject.ExternalHardware.GPS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import tcd.training.com.trainingproject.R;

public class ObtainLocationUsingGooglePlayServicesActivity extends AppCompatActivity {

    private static final int RC_FINE_LOCATION_PERMISSION = 1;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private ImageView mMapImageView;
    private Button mOpenMapButton;

    private Snackbar mRequestLocationPermissionSnackbar = null;

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

        mRequestLocationPermissionSnackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.fine_location_access_error), Snackbar.LENGTH_INDEFINITE);
        mRequestLocationPermissionSnackbar.setAction(getString(R.string.grant), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ObtainLocationUsingGooglePlayServicesActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_FINE_LOCATION_PERMISSION);
            }
        });
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
            requestFineLocationPermission();
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                mRequestLocationPermissionSnackbar = Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.fine_location_access_error), Snackbar.LENGTH_INDEFINITE);
                mRequestLocationPermissionSnackbar.setAction(getString(R.string.grant), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(ObtainLocationUsingGooglePlayServicesActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                RC_FINE_LOCATION_PERMISSION);
                    }
                });
                mRequestLocationPermissionSnackbar.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_FINE_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RC_FINE_LOCATION_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mRequestLocationPermissionSnackbar != null) {
                        mRequestLocationPermissionSnackbar.dismiss();
                    }
                    startLocationUpdates();
                } else {
                    requestFineLocationPermission();
                }
                return;
        }
    }
}
