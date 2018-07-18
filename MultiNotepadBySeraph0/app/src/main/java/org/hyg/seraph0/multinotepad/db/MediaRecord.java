package org.hyg.seraph0.multinotepad.db;

/**
 * Created by shiny on 2018-04-06.
 */

public class MediaRecord {
    private Integer mId;
    public Integer getId() { return mId; }
    private String mUri;
    public String getUri() { return mUri; }

    public MediaRecord(int id) {
        mId = id;
    }

    public MediaRecord(int id, String uri) {
        mId = id;
        mUri = uri;
    }

    public boolean isEquals(MediaRecord data) {
        if(mId == -1 || data.getId() == -1 || mId != data.getId()) { return false; }
        if(!mUri.equals(data.getUri())) { return false; }

        return true;
    }
}
