package tcd.training.com.trainingproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import tcd.training.com.trainingproject.ExternalHardware.VideoPlayer.VideoPlayerUsingMediaPlayerActivity;

/**
 * Created by cpu10661-local on 11/08/2017.
 */

public class GlobalMethod {

    public static boolean isReadExternalStoragePermissionGranted(final Context context, View view, final int requestCode) {
        // check if image already exists
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(view, context.getString(R.string.external_storage_access_error), Snackbar.LENGTH_LONG);
                snackbar.setAction(context.getString(R.string.grant), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                requestCode);
                    }
                });
                snackbar.show();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCode);
            }
            return false;
        } else {
            return true;
        }
    }
}
