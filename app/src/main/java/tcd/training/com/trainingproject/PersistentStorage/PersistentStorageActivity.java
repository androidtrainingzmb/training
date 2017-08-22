package tcd.training.com.trainingproject.PersistentStorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import tcd.training.com.trainingproject.R;

public class PersistentStorageActivity extends AppCompatActivity implements NotesListAdapter.ListItemClickListener {

    private static final int RC_ADD_NODE = 1;

    private RecyclerView mNotesListRecyclerView;
    private NotesListAdapter mNoteListAdapter;
    private FloatingActionButton mFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persistent_storage);

        initializeRecyclerViewList();

        mFAB = findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersistentStorageActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, RC_ADD_NODE);
            }
        });
    }

    private void initializeRecyclerViewList() {
        mNotesListRecyclerView = (RecyclerView) findViewById(R.id.rv_notes_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mNotesListRecyclerView.setLayoutManager(layoutManager);
        mNotesListRecyclerView.setHasFixedSize(true);
        mNoteListAdapter = new NotesListAdapter(this);
        mNotesListRecyclerView.setAdapter(mNoteListAdapter);

        readNoteFromSharedPreferences();
        readNoteFromSQLite();
        readNoteFromInternalStorage();
        readNoteFromExternalStorage();
    }

    private void readNoteFromExternalStorage() {
        if (getExternalFilesDir(null).exists()) {
            File[] files = getExternalFilesDir(null).listFiles();
            for (File file : files) {
                String fileName = file.getName();
                // make sure that it's a text file
                if (fileName.length() > 4 &&
                        fileName.substring(fileName.length() - AddNoteActivity.SAVED_FILE_EXTENSION.length())
                                .equals(AddNoteActivity.SAVED_FILE_EXTENSION)) {
                    Note note = readNoteFromFile(file);
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
                note.setStorageType("Internal File");
                mNoteListAdapter.addNoteToList(note);
            }
        }
    }

    private Note readNoteFromFile(File file) {
        String noteTitle = file.getName();
        String noteContent = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            while (fileInputStream.available() > 0) {
                sb.append((char) fileInputStream.read());
            }
            noteContent = sb.toString();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            noteTitle = noteTitle.substring(0, noteTitle.length() - AddNoteActivity.SAVED_FILE_EXTENSION.length());
            return new Note(noteTitle, noteContent, "");
        }
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
