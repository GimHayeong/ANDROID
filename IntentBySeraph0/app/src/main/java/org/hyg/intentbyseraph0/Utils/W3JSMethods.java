package org.hyg.intentbyseraph0.Utils;

import android.os.Handler;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by shiny on 2018-04-03.
 * 웹뷰에서 HTML 페이지의 자바스크립트를 호출할 인터페이스
 */
public class W3JSMethods {
    Handler mWebHandler;
    Button mWebButton;
    WebView mWebView;

    public W3JSMethods(Handler handler, Button button, WebView view){
        mWebHandler = handler;
        mWebButton = button;
        mWebView = view;
    }

    /**
     * Html 페이지에서 window.jsIf.clickOnFace() 로 호출 가능
     */
    @android.webkit.JavascriptInterface
    public void clickOnFace(){
        mWebHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebButton.setText("클릭 후 열기");
                mWebView.loadUrl("javascript:changeFace()");
            }
        });
    }

}