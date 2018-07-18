package org.hyg.rssfeederbyseraph0;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by shiny on 2018-03-27.
 * 뉴스 아이템 뷰
 * DOM 파서로 부터 읽어온 <item> ... </item> 태그의 내용
 */

public class RSSNewsItem {

    private Context mContext;

    //기사분류(연예, 스포츠 ...)
    private String mCategory;

    public String getCategory() { return mCategory; }
    //아이콘이미지 리소스
    private Drawable mIcon;
    public Drawable getIcon() { return mIcon; }
    //제목
    private String mTitle;
    public String getTitle() { return mTitle; }
    //원본뉴스 링크
    private String mLink;
    public String getLink() { return mLink; }
    //뉴스설명
    private String mDescription;
    public String getDescription() { return mDescription; }
    //취재기자
    private String mAuthor;
    public String getAuthor() { return mAuthor; }
    //기사발행일시
    private String mPubDate;
    public String getPubDate() { return mPubDate; }

    public RSSNewsItem() {

    }

    /**
     * 파서를 통해 읽어 온 노드의 값으로 RSSNewItem 생성시 호출
     * @param title
     * @param link
     * @param description
     * @param pubDate
     * @param author
     * @param category
     */
    public RSSNewsItem(String title, String link, String description, String pubDate, String author, String category) {
        mTitle = title;
        mLink = link;
        mDescription = description;
        mPubDate = pubDate;
        mAuthor = author;
        mCategory = category;
    }



}


