package org.hyg.intentbyseraph0.Utils;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by shiny on 2018-04-03.
 * 웹브라우저 클라이언트 클래스
 */
public class W3BrowserClient extends WebChromeClient {
    public boolean onJsAlert(WebView wv, String url, String msg, JsResult result){

        result.confirm();

        return true;
    }
}
