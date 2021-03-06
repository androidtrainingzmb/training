package tcd.training.com.trainingproject.ExternalInternalIntent;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tcd.training.com.trainingproject.IntentFlags.IntentFlagsActivity;
import tcd.training.com.trainingproject.R;

public class ExternalInternalIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_internal_intent);

        Button internalIntentButton = findViewById(R.id.btn_internal_intent);
        Button externalIntentButton = findViewById(R.id.btn_external_intent);

        internalIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExternalInternalIntentActivity.this, IntentFlagsActivity.class);
                startActivity(intent);
            }
        });

        externalIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("tcd.training.com.myapplication", "tcd.training.com.myapplication.MainActivity"));
                if(getPackageManager().resolveActivity(intent, 0) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(ExternalInternalIntentActivity.this, "No app installed that can perform this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
