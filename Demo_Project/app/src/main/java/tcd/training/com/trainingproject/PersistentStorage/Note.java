package tcd.training.com.trainingproject.PersistentStorage;

import java.io.Serializable;

/**
 * Created by cpu10661-local on 18/07/2017.
 */

public class Note implements Serializable{
    private String mTitle;
    private String mContent;
    private String mStorageType;

    public Note(String mTitle, String mContent, String mStorageType) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mStorageType = mStorageType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getStorageType() {
        return mStorageType;
    }

    public void setStorageType(String storageType) {
        this.mStorageType = storageType;
    }
}
