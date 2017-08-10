package tcd.training.com.trainingproject.ExternalHardware.AudioPlayer;

import android.net.Uri;

/**
 * Created by cpu10661-local on 10/08/2017.
 */

public class Song {
    private long mId;
    private String mName;
    private String mArtist;
    private Uri mUri;

    public Song(long id, String name, String artist) {
        mId = id;
        mName = name;
        mArtist = artist;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }
}
