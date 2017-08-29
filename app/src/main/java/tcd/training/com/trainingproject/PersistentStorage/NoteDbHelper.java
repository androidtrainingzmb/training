package tcd.training.com.trainingproject.PersistentStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by cpu10661-local on 19/07/2017.
 */

public class NoteDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotesList.db";

    // SQL statements
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NoteDatabaseContract.NoteEntry.TABLE_NAME + " (" +
                    NoteDatabaseContract.NoteEntry._ID + " INTEGER PRIMARY KEY," +
                    NoteDatabaseContract.NoteEntry.COLUMN_NOTE_TITLE + " TEXT," +
                    NoteDatabaseContract.NoteEntry.COLUMN_NOTE_CONTENT + " TEXT );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NoteDatabaseContract.NoteEntry.TABLE_NAME;


    public NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: " + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

final class NoteDatabaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NoteDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "noteEntry";
        public static final String COLUMN_NOTE_TITLE = "title";
        public static final String COLUMN_NOTE_CONTENT = "content";
    }
}
