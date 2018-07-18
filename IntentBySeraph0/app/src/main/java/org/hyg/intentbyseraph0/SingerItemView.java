package org.hyg.intentbyseraph0;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by shiny on 2018-03-15.
 * LinearLayout을 상속받은 클래스 : item_singer.xml 의 루트는 LinearLayout
 *  : ListView 의 아이템을 어떻게 보여줄지를 결정
 */

public class SingerItemView extends LinearLayout {

    private TextView mTxtName;
    public void setName(String name) { mTxtName.setText(name); }
    private TextView mTxtMobile;
    public void setMobile(String mobile) { mTxtMobile.setText(mobile); }
    private TextView mTxtAge;
    public void setAge(int age) { mTxtAge.setText(String.valueOf(age)); }
    private ImageView mImgPhoto;
    public void setImage(int resId) { mImgPhoto.setImageResource(resId); }

    public SingerItemView(Context context) {
        super(context);

        init(context);
    }

    public SingerItemView(Context context, AttributeSet attSet){
        super(context, attSet);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.item_singer, this, true);

        mTxtName = (TextView)findViewById(R.id.txtName);
        mTxtMobile = (TextView)findViewById(R.id.txtMobile);
        mTxtAge = (TextView)findViewById(R.id.txtAge);
        mImgPhoto = (ImageView)findViewById(R.id.imgPhoto);
    }
}
