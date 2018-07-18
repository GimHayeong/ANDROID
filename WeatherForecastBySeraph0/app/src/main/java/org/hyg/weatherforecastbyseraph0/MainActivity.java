package org.hyg.weatherforecastbyseraph0;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 구글 날씨 정보를 이용하여 원하는 지역의 날씨 정보 검색앱
 * "https://www.google.co.kr/search?dcr=0&q=google+weather&ved="
 */
public class MainActivity extends AppCompatActivity {

    public static int TIMEOUT_MS = 10000;

    private static final String TAG = "WeatherForecast";
    private static String mBaseUrl = "http://www.google.com";
    private static String mWeatherUrl = "http://www.google.com/ig/api?weather=";
    private WeatherHandler mWHandler;
    private ProgressDialog mPorgressDialog;
    private Handler mHandler;
    private Runnable mWRunnalbe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        final EditText edtSearch = (EditText)findViewById(R.id.edtSearch);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = edtSearch.getText().toString();
                String city = search.trim().replace(" ", "%20");
                
                displayWeather(city);
            }
        };
        ((Button)findViewById(R.id.btnSearch)).setOnClickListener(OnButtonClick);
    }

    private void displayWeather(String city) {

        try{
            mPorgressDialog = ProgressDialog.show(this
                                                , "Weather Refresh"
                                                , "날씨 정보 업데이트 중 ..."
                                                , true
                                                , true);

            WeatherThread thread = new WeatherThread(city);
            thread.start();

        } catch (Exception ex) {
            resetWeatherView();
            Log.e(TAG, "Error", ex);
        }
    }

    private void resetWeatherView() {
    }


    /**
     * 별도 스레드로 생성되어 메인 스레드가 다른 작업을 할 동안 서버로부터의 응답을 대기.
     */
    private class WeatherThread extends Thread{

        private String m_City;
        /**
         * 1 : HTTP
         * Default : Socket
         */
        private int m_JobType = 0;

        public WeatherThread(String city) {
            m_City = city;
        }

        /**
         * 별도 스레드에서 메인 스레드의 UI를 변경하기 위해 Handler 를 만들고 Runnable 에서 UI 변경
         */
        Runnable m_WeatherRunnable = new Runnable() {
            @Override
            public void run() {
                try{
                    /*
                    WeatherSet wSet = mWHandler.getWeaterSet();
                    updateWeatherView(R.id.weather_today, wSet.getWeatherCurrentCondition());
                    for(int i=0; i<4; i++){
                        updateWeatherView(R.drawable.weather1 + i, wSet.getWeatherCurrentCondition().get(i));
                    }
                    */
                    mPorgressDialog.dismiss();
                } catch (Exception ex) { }
            }
        };

        /**
         *
         * @param resId
         * @param condition
         */
        private void updateWeatherView(int resId, WeatherCurrentCondition condition)
                throws MalformedURLException
        {
            URL iconUrl = new URL(mBaseUrl + condition.geIconUrl());

            //WeatherView view = (WeatherView)findViewById(resId);
            //view.setIconImage(iconUrl);
            //view.setTempCelsius(condition.getCelsius());
            //view.setDayOfWeek(condition.getDayOfWeek());
        }

        /**
         *
         * @param resId
         * @param condition
         * @throws MalformedURLException
         */
        private void updateWeatherView(int resId, WeatherForecastCondition condition)
                throws MalformedURLException
        {
            URL iconUrl = new URL(mBaseUrl + condition.geIconUrl());

            //WeatherView view = (WeatherView)findViewById(resId);
            //view.setIconImage(iconUrl);
            //view.setTempCelsius(condition.getCelsius());
            //view.setHumidity(condition.getHumidity());
        }

        @Override
        public void run() {
            try{

                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                SAXParser parser = parserFactory.newSAXParser();
                XMLReader reader = parser.getXMLReader();

                mWHandler = new WeatherHandler();
                reader.setContentHandler(mWHandler);

                InputStream inStream;
                if(m_JobType == 1) {
                    inStream = getInputStreamUsingHttp(m_City);
                } else {
                    inStream = getInputStreamUsingSocket(m_City);
                }

                reader.parse(new InputSource(inStream));

                //mWHandler.post(m_WeatherRunnable);

            } catch (Exception ex) {
                Log.e(TAG, "Error", ex);
            }
        }

        private InputStream getInputStreamUsingHttp(String city) throws Exception {
            String query = mWeatherUrl + city;
            URL url = new URL(query);
            InputStream inStream;

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);

            int resCode = conn.getResponseCode();
            inStream = conn.getInputStream();

            /*if(conn != null){
                if(resCode == HttpURLConnection.HTTP_OK){
                }
            }*/

            return inStream;
        }

        private InputStream getInputStreamUsingSocket(String city) throws Exception{
            Socket socket = createSocket(new URL(mWeatherUrl));
            String query = "GET /ig/api?weather=" + city + " HTTP/1.1\r\n"
                    + "Accept: */*\r\rn"
                    + "Accept-Encoding: gzip, deflate\r\n"
                    + "User-Agent: Dalvik/1.1.0\r\n"
                    + "Host: www.google.com\r\n"
                    + "Connection: Keep-Alive\r\n\r\n";

            byte[] bytes = query.getBytes();
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes, 0, bytes.length);
            outputStream.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String strXml = null;
            while(true){
                String line = reader.readLine();
                if(line == null) { break; }

                if(line.startsWith("<?xml")) {
                    strXml = line;
                    break;
                }
            }

            byte[] bytesXml = strXml.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytesXml);

            return inputStream;
        }

        private Socket createSocket(URL url) throws Exception {
            Socket socket = null;
            String host = url.getHost();
            int port = url.getPort();
            if(port < 1) { port = 80; }

            long now = SystemClock.uptimeMillis();


            InetAddress addr = InetAddress.getByName(host);
            SocketAddress socketAddr = new InetSocketAddress(addr, port);

            socket = new Socket();
            socket.connect(socketAddr, TIMEOUT_MS);

            return socket;
        }


    }


}
