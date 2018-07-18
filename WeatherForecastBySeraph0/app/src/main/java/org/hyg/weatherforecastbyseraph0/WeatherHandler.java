package org.hyg.weatherforecastbyseraph0;

import android.os.Handler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static java.lang.Integer.parseInt;


/**
 * Created by shiny on 2018-03-27.
 *
 * 별도 스레드에서 메인 스레드의 UI 접근
 *  - SAX 파서를 사용하기 위해 정의해 놓은 DefaultHandler 상속받아 구현
 *  - WeatherView 업데이트
 */

public class WeatherHandler extends DefaultHandler {

    //private WeaterSet mWSet = null;
    //public WeatherSet getWeatherSet() { return mWSet; }
    private boolean mInForecastInfo = false;
    // 오늘 날씨
    private boolean mInCurtConditions = false;
    private boolean mInForecastConditions = false;
    // 섭씨 / 화씨
    private boolean mUsingSITemperature = false;


    /**
     * SAX 에서 특정 태그를 만나면 자동으로 호출
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        //mWset = new WeatherSet();
    }

    /**
     * SAX 에서 특정 태그를 만나면 자동으로 호출
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        // TODO:
    }

    /**
     * SAX 에서 시작  태그를 만나면 자동으로 호출
     * @param uri
     * @param localName : 시작 태그명
     * @param qName
     * @param attributes : 태그 속성들
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch(localName){
            case "forecast_information":
                mInForecastInfo = true;
                break;

            case "current_conditions":
                //mWSet.setWeatherCurrentCondition(new WeatherCurrentCondition());
                mInCurtConditions = true;
                break;

            case "forecast_conditions":
                //mWSet.setWeatherForecastConditions().add(new WeatherForecastCondition());
                mInForecastConditions = true;
                break;

            default:
                String dataAttr = attributes.getValue("data");
                switch(localName) {
                    case "city":
                    case "postal_code":
                    case "latitude_e6":
                    case "longitude_ey":
                    case "forecast_data":
                    case "current_date_time":
                        break;

                    case "unit_system":
                        if(dataAttr.equals("SI")){ mUsingSITemperature = true; }
                        break;

                    case "day_of_week":
                        if(mInCurtConditions) {
                            //mWSet.getWeatherCurrentConditions().setDayOfWeek(dataAttr);
                        } else if(mInForecastConditions){
                            //mWSet.getLastWeatherForecastCondition().setDayOfWeek(datAttr);
                        }
                        break;

                    case "icon":
                        if(mInCurtConditions) {
                            //mWSet.getWeatherCurrentCondition().setIconURL(dataAttr);
                        } else if(mInForecastConditions){
                            //mWset.getLastWeatherForecastCondition().setIconURL(dataAttr);
                        }
                        break;

                    case "condition":
                        if(mInCurtConditions) {
                            //mWSet.getWeatherCurrentCondition().setCondition(dataAttr);
                        } else if(mInForecastConditions){
                            //mWset.getLastWeatherForecastCondition().setCondition(dataAttr);
                        }
                        break;

                    case "temp_f":
                        //mWSet.getWeatherCurrentCondition().setTempFahrenheit(Integer.parseInt(dataAttr));
                        break;

                    case "temp_c":
                        //mWSet.getWeatherCurrentCondition().setTempCelcius(Integer.parseInt(dataAttr));
                        break;

                    case "humidity":
                        //mWSet.getWeatherCurrentCondition().setHumidity(dataAttr);
                        break;

                    case "wind_condition":
                        //mWSet.getWeatherCurrentCondition().setWindCondition(dataAttr);
                        break;

                    case "low":
                        int min = Integer.parseInt(dataAttr);
                        if(this.mUsingSITemperature) {
                            //mWSet.getLastWeatherForecastCondition().setTempMinCelsius(min);
                        } else {
                            //mWSet.getLastWeatherForecastCondition().setTempMinCelsius(fahrenheitToCelsius(min));
                        }
                        break;

                    case "high":
                        int max = Integer.parseInt(dataAttr);
                        if(this.mUsingSITemperature) {
                            //mWSet.getLastWeatherForecastCondition().setTempMaxCelsius(max);
                        } else {
                            //mWSet.getLastWeatherForecastCondition().setTempMaxCelsius(fahrenheitToCelsius(max));
                        }
                        break;

                    default:
                        break;

                }//first switch - default
                break;
        }
    }

    /**
     * SAX 에서 끝  태그를 만나면 자동으로 호출
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName) {
            case "forecast_information":
                mInForecastInfo = false;
                break;

            case "current_conditions":
                mInCurtConditions = false;
                break;

            case "forecast_conditions":
                mInForecastConditions = false;
                break;

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    /**
     * 섭씨로 변환
     * @param tFahrenheit
     * @return
     */
    public static int fahrenheitToCelsius(int tFahrenheit) {
        return (int)((5.0f / 9.0f) * (tFahrenheit - 32));
    }

    /**
     * 화씨로 변환
     * @param tCelsius
     * @return
     */
    public static int celsiusToFahrenheit(int tCelsius) {
        return (int)((9.0f / 5.0f) * (tCelsius + 32));
    }
}
