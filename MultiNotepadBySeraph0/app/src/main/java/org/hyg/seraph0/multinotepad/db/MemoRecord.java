package org.hyg.seraph0.multinotepad.db;

import org.hyg.seraph0.multinotepad.BasicInfo;

import java.util.Date;

/**
 * Created by shiny on 2018-04-10.
 */

public class MemoRecord {
    private Integer mMemoId;
    public Integer getId() { return mMemoId; }
    private String mMemoDate;
    public String getDate() { return mMemoDate; }
    public void setDate(String value) {
        if(value != null && value.length() > 10) {
            try{
                Date date = BasicInfo.dateFormat.parse(value);
                if(BasicInfo.language.equals("ko")) {
                    mMemoDate = BasicInfo.dateNameFormat2.format(date);
                } else {
                    mMemoDate = BasicInfo.dateNameFormat3.format(date);
                }
            } catch (Exception ex) { mMemoDate = value.substring(0, 10); }
        } else { mMemoDate = ""; }
    }
    private String mMemoText;
    public String getMemoText() { return mMemoText; }
    private MediaRecord mHandwriting;
    public MediaRecord getHandwriting() { return mHandwriting; }
    public Integer getHandwritingId() { return (mHandwriting != null) ? mHandwriting.getId() : -1; }
    public String getHandwritingUri() { return (mHandwriting != null) ? mHandwriting.getUri() : null; }
    private MediaRecord mPhoto;
    public MediaRecord getPhoto() { return mPhoto; }
    public Integer getPhotoId() { return (mPhoto != null) ? mPhoto.getId() : -1; }
    public String getPhotoUri() { return (mPhoto != null) ? mPhoto.getUri() : null; }
    private MediaRecord mVideo;
    public MediaRecord getVideo() { return mVideo; }
    public Integer getVideoId() { return (mVideo != null) ? mVideo.getId() : -1; }
    public String getVideoUri() { return (mVideo != null) ? mVideo.getUri() : null; }
    private MediaRecord mVoice;
    public MediaRecord getVoice() { return mVoice; }
    public Integer getVoiceId() { return (mVoice != null) ? mVoice.getId() : -1; }
    public String getVoiceUri() { return (mVoice != null) ? mVoice.getUri() : null; }

    public MemoRecord(Integer itemId, String date, String text
                    , Integer handwritingId
                    , Integer photoId
                    , Integer videoId
                    , Integer voiceId) {
        mMemoId = itemId;
        setDate(date);
        mMemoText = text;
        mHandwriting = new MediaRecord(handwritingId);
        mPhoto = new MediaRecord(photoId);
        mVideo = new MediaRecord(videoId);
        mVoice = new MediaRecord(voiceId);
    }

    public MemoRecord(Integer itemId, String date, String text
            , Integer handwritingId, String handwritingUri
            , Integer photoId, String photoUri
            , Integer videoId, String videoUri
            , Integer voiceId, String voiceUri) {

        mMemoId = itemId;
        mMemoDate = date;
        mMemoText = text;
        mHandwriting = (handwritingId == -1) ? null : new MediaRecord(handwritingId, handwritingUri);
        mPhoto = (photoId == -1) ? null : new MediaRecord(photoId, photoUri);
        mVideo = (videoId == -1) ? null : new MediaRecord(videoId, videoUri);
        mVoice = (voiceId == -1) ? null : new MediaRecord(voiceId, voiceUri);
    }

    /**
     * id 부터 녹음데이터까지 모든 데이터의 일치여부 반환
     * @param data
     * @return
     */
    public boolean isEquals(MemoRecord data) {
        if(!mMemoId.equals(data.getId())) { return false; }
        if(!mMemoDate.equals(data.getDate())) { return false; }
        if(!mMemoText.equals(data.getMemoText())) { return false; }
        if(!mHandwriting.isEquals(data.getHandwriting())) { return false; }
        if(mPhoto.isEquals(data.getPhoto())) { return false; }
        if(mVideo.isEquals(data.getVideo())) { return false; }
        if(mVoice.isEquals(data.getVoice())) { return false; }

        return true;
    }
}
