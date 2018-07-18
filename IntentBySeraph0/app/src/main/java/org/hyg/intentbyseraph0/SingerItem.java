package org.hyg.intentbyseraph0;

/**
 * Created by shiny on 2018-03-15.
 */

public class SingerItem {
    private String mName;
    public String getName() { return mName; }
    public void setName(String name) { mName = name; }
    private String mMobile;
    public String getMobile() { return mMobile; }
    public void setMobile(String mobile) { mMobile = mobile; }
    private int mAge;
    public int getAge() { return mAge; }
    public void setAge(int age) { mAge = age; }
    private int mResId;
    public int getResId() { return mResId; }
    public void setResId(int resId) { mResId = resId; }


    public SingerItem(String name, String mobile){
        mName = name;
        mMobile = mobile;
    }

    public SingerItem(String name, String mobile, int age, int resId){
        mName = name;
        mMobile = mobile;
        mAge = age;
        mResId = resId;
    }
}
