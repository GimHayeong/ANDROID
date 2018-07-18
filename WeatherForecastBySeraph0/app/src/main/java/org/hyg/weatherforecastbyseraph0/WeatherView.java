package org.hyg.weatherforecastbyseraph0;

import java.net.URL;

/**
 * Created by shiny on 2018-03-28.
 */

public class WeatherView {

    private URL mIconImage = null;
    public void setIconImage(URL value) { mIconImage = value; }
    private Integer mTempCelsius;
    public void setTempCelsius(Integer value) { mTempCelsius = value; }
    private Integer mDayOfWeek;
    public void setDayOfWeek(Integer value) { mDayOfWeek = value; }
    private String mHumidity = null;
    public void setHumidity(String value) { mHumidity = value; }
}
