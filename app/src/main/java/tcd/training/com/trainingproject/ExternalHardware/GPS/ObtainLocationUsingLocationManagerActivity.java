package tcd.training.com.trainingproject.ExternalHardware.GPS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tcd.training.com.trainingproject.R;

public class ObtainLocationUsingLocationManagerActivity extends AppCompatActivity {

    private static final String TAG = ObtainLocationUsingLocationManagerActivity.class.getSimpleName();

    private static final int RC_FINE_LOCATION_PERMISSION = 1;
    private Snackbar mRequestLocationPermissionSnackbar = null;

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private ImageView mMapImageView;
    private Button mOpenMapButton;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String mSelectedProvider;
    private Snackbar mRequestProviderSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtain_location);

        initializeUiComponents();
        initializeBasicComponents();

        registerLocationListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerLocationListener();
    }

    private void initializeUiComponents() {
        mLatitudeTextView = findViewById(R.id.tv_latitude);
        mLongitudeTextView = findViewById(R.id.tv_longitude);
        mMapImageView = findViewById(R.id.iv_map);

        mOpenMapButton = findViewById(R.id.btn_open_map);
    }

    private void initializeBasicComponents() {
        boolean isGpsProviderChecked = ((RadioButton)findViewById(R.id.rb_gps_provider)).isChecked();
        mSelectedProvider = isGpsProviderChecked ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // location listener
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                final String latitude = String.valueOf(location.getLatitude());
                final String longitude = String.valueOf(location.getLongitude());
                mLatitudeTextView.setText(latitude);
                mLongitudeTextView.setText(longitude);

                String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
                        + latitude + "," + longitude + "&zoom=16&size=1000x300";
                Glide.with(ObtainLocationUsingLocationManagerActivity.this).load(imageUrl).into(mMapImageView);

                mOpenMapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mapUrl = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(mapUrl));
                        startActivity(intent);
                    }
                });
                mOpenMapButton.setVisibility(View.VISIBLE);

                mLocationManager.removeUpdates(mLocationListener);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                registerLocationListener();
            }

            @Override
            public void onProviderDisabled(String s) {
                mRequestProviderSnackbar.show();
            }
        };

        // request location permission snackbar
        mRequestLocationPermissionSnackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.fine_location_access_error), Snackbar.LENGTH_INDEFINITE);
        mRequestLocationPermissionSnackbar.setAction(getString(R.string.grant), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ObtainLocationUsingLocationManagerActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        RC_FINE_LOCATION_PERMISSION);
            }
        });

        // request provider snackbar
        mRequestProviderSnackbar = Snackbar.make(findViewById(android.R.id.content),
                getString(R.string.require_according_provider), Snackbar.LENGTH_INDEFINITE);
        mRequestProviderSnackbar.setAction(getString(R.string.enable), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationSettingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(locationSettingsIntent);
                mRequestProviderSnackbar.dismiss();
            }
        });
    }

    private void registerLocationListener() {
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestFineLocationPermission();
            return;
        }

        // clean up previous location info
        if (mLocationListener != null) {
            // remove previous location listener
            mLocationManager.removeUpdates(mLocationListener);
            // UI components
            mLatitudeTextView.setText("");
            mLongitudeTextView.setText("");
            mMapImageView.setImageDrawable(null);
            mOpenMapButton.setVisibility(View.GONE);
        }

        // register a new provider
        if (mLocationManager.isProviderEnabled(mSelectedProvider)) {
            mLocationManager.requestLocationUpdates(mSelectedProvider, 0, 0, mLocationListener);
        } else {
            mRequestProviderSnackbar.show();
        }
    }

    private void requestFineLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

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
                    registerLocationListener();
                } else {
                    requestFineLocationPermission();
                }
                return;
        }
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_gps_provider:
                mSelectedProvider = LocationManager.GPS_PROVIDER;
                break;
            case R.id.rb_network_provider:
                mSelectedProvider = LocationManager.NETWORK_PROVIDER;
                break;
        }
        mRequestProviderSnackbar.dismiss();
        registerLocationListener();
    }
}
