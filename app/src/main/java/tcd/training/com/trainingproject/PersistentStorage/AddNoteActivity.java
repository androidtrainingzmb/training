package tcd.training.com.trainingproject.PersistentStorage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.File;
import java.io.FileOutputStream;

import tcd.training.com.trainingproject.R;

public class AddNoteActivity extends AppCompatActivity {

    private EditText mNoteTitleEditText;
    private EditText mNoteContentEditText;
    private Button mSaveNoteButton;

    // radio buttons
    private static final int SHARED_PREFERENCE_METHOD = 0;
    private static final int SQLITE_METHOD = 1;
    private static final int INTERNAL_FILE_METHOD = 2;
    private static final int EXTERNAL_FILE_METHOD = 3;
    private int saveFileMethod = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initializeBasicComponents();
    }

    private void initializeBasicComponents() {
        mNoteTitleEditText = findViewById(R.id.et_note_title);
        mNoteContentEditText = findViewById(R.id.et_note_content);
        mSaveNoteButton = findViewById(R.id.btn_save_note);

        mSaveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteToStorage();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void saveNoteToStorage() {
        if (mNoteTitleEditText.getText().toString().length() == 0) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.empty_note_title_error), Snackbar.LENGTH_LONG).show();
            return;
        } else if (mNoteContentEditText.getText().toString().length() == 0) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.empty_note_content_error), Snackbar.LENGTH_SHORT).show();
            return;
        }

        switch (saveFileMethod) {
            case SHARED_PREFERENCE_METHOD:
                saveNoteUsingSharedPreference();
                break;
            case SQLITE_METHOD:
                saveNoteUsingSQLite();
                break;
            case INTERNAL_FILE_METHOD:
                saveNoteUsingInternalStorage();
                break;
            case EXTERNAL_FILE_METHOD:
                saveNoteUsingExternalStorage();
                break;
        }
    }

    private void saveNoteUsingExternalStorage() {
        if (!isExternalStorageWritable()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.require_external_storage_permission_error), Snackbar.LENGTH_SHORT).show();
            return;
        }
        File file = new File(getExternalFilesDir(null), mNoteTitleEditText.getText().toString());
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(mNoteContentEditText.getText().toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void saveNoteUsingInternalStorage() {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(mNoteTitleEditText.getText().toString(), Context.MODE_PRIVATE);
            outputStream.write(mNoteContentEditText.getText().toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveNoteUsingSQLite() {
        NoteDbHelper mDbHelper = new NoteDbHelper(this);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseContract.NoteEntry.COLUMN_NOTE_TITLE, mNoteTitleEditText.getText().toString());
        values.put(NoteDatabaseContract.NoteEntry.COLUMN_NOTE_CONTENT, mNoteContentEditText.getText().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(NoteDatabaseContract.NoteEntry.TABLE_NAME, null, values);
    }

    private void saveNoteUsingSharedPreference() {
        // write
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.note_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mNoteTitleEditText.getText().toString(), mNoteContentEditText.getText().toString());
        editor.commit();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.rb_shared_preference:
                    saveFileMethod = SHARED_PREFERENCE_METHOD;
                    break;
                case R.id.rb_sqlite:
                    saveFileMethod = SQLITE_METHOD;
                    break;
                case R.id.rb_internal_file:
                    saveFileMethod = INTERNAL_FILE_METHOD;
                    break;
                case R.id.rb_external_file:
                    saveFileMethod = EXTERNAL_FILE_METHOD;
                    break;
            }
        }
    }
}
