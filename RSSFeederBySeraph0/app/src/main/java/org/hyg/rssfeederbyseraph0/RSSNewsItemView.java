package org.hyg.rssfeederbyseraph0;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shiny on 2018-03-28.
 * 정보 단위인 RSSNewsItem 을 보여주는 뷰 클래스
 *  - item_news.xml 에서 화면에 보여줄 레이아웃 설정하고 그 UI에 보여 줄 값 처리
 */

public class RSSNewsItemView extends View {

    private Context mContext;
    private TextView mTxtCategory, mTxtTitle, mTxtPubDate, mTxtDescription;
    private ImageView mIcon;
    public void setIcon(Drawable drawable){ mIcon.setImageDrawable(drawable); }

    public RSSNewsItemView(Context context){
        this(context, null);
    }

    public RSSNewsItemView(Context context, @Nullable RSSNewsItem itm) {
        super(context);
        mContext = context;

        if(itm != null) {
            setIcon(itm.getIcon());
            mTxtTitle.setText(itm.getTitle());
            mTxtPubDate.setText(itm.getPubDate());
            mTxtCategory.setText(itm.getCategory());
            mTxtDescription.setText(itm.getDescription());
        }
    }

    public void setText(int idx, String txt) {
        switch(idx) {
            case 0:
                mTxtTitle.setText(txt);
                break;

            case 1:
                mTxtPubDate.setText(txt);
                break;

            case 2:
                mTxtCategory.setText(txt);
                break;

            case 3:
                mTxtDescription.setText(txt);
                break;

            default:
                break;
        }
    }
}
