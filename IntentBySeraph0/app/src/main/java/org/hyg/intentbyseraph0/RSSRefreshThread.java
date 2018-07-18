package org.hyg.intentbyseraph0;

import android.os.Handler;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by shiny on 2018-03-27.
 */

public class RSSRefreshThread extends Thread {
    private String mUrl;
    private final String TAG = "RSSMAIN";
    private Handler mHandler;
    private Runnable mUpdateRunnable;
    public RSSRefreshThread(String url) {
        mUrl = url;
    }

    @Override
    public void run() {
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            URL url = new URL(mUrl);

            InputStream inStream = getInputStreamUsingHttp(url);
            Document document = builder.parse(inStream);

            int itmCount = processDocument(document);
            Log.d(TAG, itmCount + " news item processed.");

            //mHandler.post(mUpdateRunnable);
        } catch (ParserConfigurationException e) {
            // newDocumentBuilder() 메서드를 위한 exception
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // URL() 메서드를 위한 exception
            e.printStackTrace();
        } catch (SAXException e) {
            // parse() 메서드를 위한 exception
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int processDocument(Document document) {
        // TODO:
        return 0;
    }

    private InputStream getInputStreamUsingHttp(URL url) {
        // TODO:
        return null;
    }
}