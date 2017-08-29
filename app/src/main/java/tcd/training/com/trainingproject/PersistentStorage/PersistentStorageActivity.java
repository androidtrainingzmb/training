package tcd.training.com.trainingproject.PersistentStorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import tcd.training.com.trainingproject.R;

import static tcd.training.com.trainingproject.PersistentStorage.AddNoteActivity.SAVED_FILE_EXTENSION;

public class PersistentStorageActivity extends AppCompatActivity implements NotesListAdapter.ListItemClickListener {

    private static final int RC_ADD_NODE = 1;

    private NotesListAdapter mNoteListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistent_storage);

        initializeRecyclerViewList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersistentStorageActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, RC_ADD_NODE);
            }
        });
    }

    private void initializeRecyclerViewList() {
        RecyclerView notesListRecyclerView = findViewById(R.id.rv_notes_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesListRecyclerView.setLayoutManager(layoutManager);
        notesListRecyclerView.setHasFixedSize(true);
        mNoteListAdapter = new NotesListAdapter(this);
        notesListRecyclerView.setAdapter(mNoteListAdapter);

        readNoteFromSharedPreferences();
        readNoteFromSQLite();
        readNoteFromInternalStorage();
        readNoteFromExternalStorage();
    }

    private void readNoteFromExternalStorage() {
        if (getExternalFilesDir(null).exists()) {
            File[] files = getExternalFilesDir(null).listFiles();
            for (File file : files) {
                Note note = readNoteFromFile(file);
                if (note != null) {
                    note.setStorageType("External File");
                    mNoteListAdapter.addNoteToList(note);
                }
            }
        }
    }

    private void readNoteFromInternalStorage() {
        if (getFilesDir().exists()) {
            File[] files = getFilesDir().listFiles();
            for (File file : files) {
                Note note = readNoteFromFile(file);
                if (note != null) {
                    note.setStorageType("Internal File");
                    mNoteListAdapter.addNoteToList(note);
                }
            }
        }
    }

    private Note readNoteFromFile(File file) {
        String title = file.getName();
        String content = "";

        // make sure that it's the compatible file type
        if (title.length() <= SAVED_FILE_EXTENSION.length() ||
                !title.substring(title.length() - SAVED_FILE_EXTENSION.length()).equals(SAVED_FILE_EXTENSION)) {
            return null;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            while (fileInputStream.available() > 0) {
                sb.append((char) fileInputStream.read());
            }
            content = sb.toString();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        title = title.substring(0, title.length() - SAVED_FILE_EXTENSION.length());
        return new Note(title, content, "");
    }

    private void readNoteFromSQLite() {
        NoteDbHelper mDbHelper = new NoteDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                NoteDatabaseContract.NoteEntry._ID,
                NoteDatabaseContract.NoteEntry.COLUMN_NOTE_TITLE,
                NoteDatabaseContract.NoteEntry.COLUMN_NOTE_CONTENT
        };

        Cursor cursor = db.query(
                NoteDatabaseContract.NoteEntry.TABLE_NAME,      // The table to query
                projection,                                     // The columns to return
                null,                                           // The columns for the WHERE clause
                null,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        while (cursor.moveToNext()) {
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(NoteDatabaseContract.NoteEntry._ID));
            String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseContract.NoteEntry.COLUMN_NOTE_TITLE));
            String noteContent = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseContract.NoteEntry.COLUMN_NOTE_CONTENT));
            mNoteListAdapter.addNoteToList(new Note(noteTitle, noteContent, "SQLite database"));
        }

        cursor.close();
        mDbHelper.close();

    }

    private void readNoteFromSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.note_shared_preferences), Context.MODE_PRIVATE);
        // read
        sharedPref.getString(getString(R.string.note_shared_preferences), "");
        Map<String, ?> keys = sharedPref.getAll();
        for(Map.Entry<String, ?> entry : keys.entrySet()) {
            mNoteListAdapter.addNoteToList(new Note(entry.getKey(), entry.getValue().toString(), "Shared Preferences"));
        }
    }

    @Override
    public void onListItemClick(int item) {
        Intent intent = new Intent(this, DisplayNoteDetailsActivity.class);
        intent.putExtra("note", mNoteListAdapter.getItem(item));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_ADD_NODE:
                if (resultCode == RESULT_OK) {
                    mNoteListAdapter.clear();
                    readNoteFromSQLite();
                    readNoteFromInternalStorage();
                    readNoteFromExternalStorage();
                    readNoteFromSharedPreferences();
                    mNoteListAdapter.notifyDataSetChanged();
                }
        }
    }
}
