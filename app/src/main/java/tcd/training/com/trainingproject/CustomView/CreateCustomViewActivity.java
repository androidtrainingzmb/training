package tcd.training.com.trainingproject.CustomView;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import tcd.training.com.trainingproject.R;

public class CreateCustomViewActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_custom_view);

        circleView = findViewById(R.id.circle_view);

        Button changeColorButton = findViewById(R.id.btn_change_color);
        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                circleView.setCircleColor(color);
                color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                circleView.setLabelColor(color);
                circleView.setCircleLabel(String.valueOf(color));
            }
        });
    }
}
