package tcd.training.com.trainingproject.PersistentStorage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import tcd.training.com.trainingproject.R;

public class DisplayNoteDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        findViewById(R.id.rg_save_method).setVisibility(View.GONE);
        findViewById(R.id.btn_save_note).setVisibility(View.GONE);

        Note note = (Note) getIntent().getSerializableExtra("note");
        EditText noteTitleEditText = findViewById(R.id.et_note_title);
        noteTitleEditText.setText(note.getTitle());
        noteTitleEditText.setFocusable(false);
        EditText noteContentEditText = findViewById(R.id.et_note_content);
        noteContentEditText.setText(note.getContent());
        noteContentEditText.setFocusable(false);
    }
}
