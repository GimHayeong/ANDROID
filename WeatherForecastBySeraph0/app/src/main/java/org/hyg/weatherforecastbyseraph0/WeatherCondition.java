package org.hyg.weatherforecastbyseraph0;

/**
 * Created by shiny on 2018-03-28.
 */

public class WeatherCondition {
    //요일
    private String mDayOfWeek = null;
    public String getDayOfWeek() { return mDayOfWeek; }
    public void setDayOfWeek(String value) { mDayOfWeek = value; }
    //섭씨
    private Integer mCelsius = null;
    public Integer getCelsius() { return mCelsius; }
    public void setCelsius(Integer value) { mCelsius = value; }
    //화씨
    private Integer mFahrenheit = null;
    public Integer getFahrenheit() { return mFahrenheit; }
    public void setFahrenheit(Integer value) { mFahrenheit = value; }
    //날씨아이콘 위치
    private String mIconUrl = null;
    public String geIconUrl() { return mIconUrl; }
    public void setIconUrl(String value) { mIconUrl = value; }
    //날씨
    private String mCondition = null;
    public String getCondition() { return mCondition; }
    public void setCondition(String value) { mCondition = value; }
    //풍속
    private String mWindCondition = null;
    public String getWindCondition() { return mWindCondition; }
    public void setWindCondition(String value) { mWindCondition = value; }
    //습도
    private String mHumidity = null;
    public String getHumidity() { return mHumidity; }
    public void setHumidity(String value) { mHumidity = value; }
}
