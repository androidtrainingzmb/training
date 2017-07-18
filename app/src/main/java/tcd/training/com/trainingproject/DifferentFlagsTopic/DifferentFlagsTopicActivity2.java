package tcd.training.com.trainingproject;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DifferentFlagsTopicActivity2 extends AppCompatActivity {
    private static final String TAG = DifferentFlagsTopicActivity2.class.getSimpleName();

    Intent intent = null;
    int flag = -1;
    String instruction = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_flags_topic2);
        getSupportActionBar().setTitle(getLocalClassName());

        Log.d(TAG, "onCreate: Instantiating activity 2");

        final LinkedHashMap<Integer, String> flagOptions = new LinkedHashMap<>();
        flagOptions.put(Intent.FLAG_ACTIVITY_CLEAR_TASK, "FLAG_ACTIVITY_CLEAR_TASK");
        flagOptions.put(Intent.FLAG_ACTIVITY_CLEAR_TOP, "FLAG_ACTIVITY_CLEAR_TOP");
        flagOptions.put(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, "FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
        flagOptions.put(Intent.FLAG_ACTIVITY_FORWARD_RESULT, "FLAG_ACTIVITY_FORWARD_RESULT");
        flagOptions.put(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT, "FLAG_ACTIVITY_LAUNCH_ADJACENT");
        flagOptions.put(Intent.FLAG_ACTIVITY_MULTIPLE_TASK, "FLAG_ACTIVITY_MULTIPLE_TASK");
        flagOptions.put(Intent.FLAG_ACTIVITY_NEW_DOCUMENT, "FLAG_ACTIVITY_NEW_DOCUMENT");
        flagOptions.put(Intent.FLAG_ACTIVITY_NEW_TASK, "FLAG_ACTIVITY_NEW_TASK");
        flagOptions.put(Intent.FLAG_ACTIVITY_NO_ANIMATION, "FLAG_ACTIVITY_NO_ANIMATION");
        flagOptions.put(Intent.FLAG_ACTIVITY_NO_HISTORY, "FLAG_ACTIVITY_NO_HISTORY");
        flagOptions.put(Intent.FLAG_ACTIVITY_NO_USER_ACTION, "FLAG_ACTIVITY_NO_USER_ACTION");
        flagOptions.put(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP, "FLAG_ACTIVITY_PREVIOUS_IS_TOP");
        flagOptions.put(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, "FLAG_ACTIVITY_REORDER_TO_FRONT");
        flagOptions.put(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
        flagOptions.put(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS, "FLAG_ACTIVITY_RETAIN_IN_RECENTS");
        flagOptions.put(Intent.FLAG_ACTIVITY_SINGLE_TOP, "FLAG_ACTIVITY_SINGLE_TOP");
        flagOptions.put(Intent.FLAG_ACTIVITY_TASK_ON_HOME, "FLAG_ACTIVITY_TASK_ON_HOME");

        // layout components
        Button toNextActivityButton = (Button) findViewById(R.id.btn_to_next_activity);
        toNextActivityButton.setText(DifferentFlagsTopicActivity3.class.getSimpleName());
        TextView intentFlagNameTextView = (TextView) findViewById(R.id.tv_intent_flag_name);
        intentFlagNameTextView.setText(flagOptions.get(getIntent().getFlags()));
        final TextView instructionTextView = (TextView) findViewById(R.id.tv_instruction);

        toNextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intent == null) {
                    intent = new Intent(DifferentFlagsTopicActivity2.this, DifferentFlagsTopicActivity3.class);
                }
                if (flag == -1) {
                    flag = getIntent().getFlags();
                }
                intent.setFlags(flag);
                startActivity(intent);
            }
        });

        switch (getIntent().getFlags()) {
            case Intent.FLAG_ACTIVITY_NO_ANIMATION: {
                toNextActivityButton.setVisibility(View.GONE);
                instructionTextView.setVisibility(View.GONE);
                break;
            }
            case Intent.FLAG_ACTIVITY_NO_HISTORY: {
                instruction = "Then tap back button and you will notice that it will return to activity 1.";
                break;
            }
            case Intent.FLAG_ACTIVITY_REORDER_TO_FRONT: {
                instruction = "Actually I would expect it to reorder the back stack to main -> 2 -> 1. Unfortunately, when i hit the back button, it brings me back to the launcher instead of activity 2 (?!?!)";
                toNextActivityButton.setText(DifferentFlagsTopicActivity1.class.getSimpleName());
                intent = new Intent(this, DifferentFlagsTopicActivity1.class);
                break;
            }
            case Intent.FLAG_ACTIVITY_CLEAR_TASK: case Intent.FLAG_ACTIVITY_NEW_TASK: {
                instruction = "It will send an intent with both FLAG_ACTIVITY_CLEAR_TASK and FLAG_ACTIVITY_NEW_TASK on. NEW_TASK shall create a new task with activity 3 and CLEAR_TASK shall remove the previous task (main and activity 1)";
                flag = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
                break;
            }
            case Intent.FLAG_ACTIVITY_TASK_ON_HOME: {
                instruction = "It will send an intent with both FLAG_ACTIVITY_TASK_ON_HOME and FLAG_ACTIVITY_NEW_TASK on. NEW_TASK shall create a new task with activity 3 and CLEAR_TASK shall remove the previous task (main and activity 1)";
                flag = Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK;
                break;
            }
            case Intent.FLAG_ACTIVITY_CLEAR_TOP: {
                instruction = "The new activity is activity 1 and the current activity will be discarded.";
                toNextActivityButton.setText(DifferentFlagsTopicActivity1.class.getSimpleName());
                intent = new Intent(this, DifferentFlagsTopicActivity1.class);
                break;
            }
            case Intent.FLAG_ACTIVITY_SINGLE_TOP: {
                instruction = "Unlike other flags, the new activity will not be created. It is because the activity is already running at the top of the stack.";
                toNextActivityButton.setText(DifferentFlagsTopicActivity2.class.getSimpleName());
                intent = new Intent(this, DifferentFlagsTopicActivity2.class);
                break;
            }
            case Intent.FLAG_ACTIVITY_NEW_DOCUMENT: {
                instruction = "The new activity will be in another view in the recent apps screen.";
                toNextActivityButton.setText(DifferentFlagsTopicActivity2.class.getSimpleName());
                intent = new Intent(this, DifferentFlagsTopicActivity2.class);
                break;
            }
            case Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS: {
                instruction = "Then tap the back button to finish the activity. Finally, tap the recent apps button and the activity 3 shall still be on that list.";
                flag = Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS | Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                break;
            }
//            case Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS: {
//                instruction = "It will send an intent with FLAG_ACTIVITY_EXCLUDE_FROM_INTENT, FLAG_ACTIVITY_NEW_TASK and FLAG_ACTIVITY_CLEAR_TASK on, the last two flags will remove all previous activities. Now tap the back button to get back to the launcher and then tap recent button, the application is gone :)";
//                    flag =  Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
//                break;
//            }
//            case Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT: {
//                flag = Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
//                break;
//            }
            default:
                instructionTextView.setText("There is no demo for this flag. Please check the documentation.");
                toNextActivityButton.setVisibility(View.GONE);
        }

        if (instructionTextView.getText().toString() == null) {
            instructionTextView.setText("Tap below button to navigate to another activity.\n" + instruction);
        }
    }
}
