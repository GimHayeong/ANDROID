package org.hyg.intentbyseraph0;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class WebviewMainActivity extends AppCompatActivity {

    private WebView mWebView;
    private Handler mWebHandler = new Handler();
    private Button mWebButton;
    private EditText mWebUrlInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_main);

        mWebView = (WebView)findViewById(R.id.wvWeb);
        WebSettings webSet = mWebView.getSettings();
        webSet.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebBrowerClient());
        mWebView.addJavascriptInterface(new JavaScriptMethods(), "jsIf");
        mWebView.loadUrl("file:///android_asset/www/Sample.html");

        mWebButton = (Button)findViewById(R.id.btnOpenWeb);
        mWebUrlInput = (EditText)findViewById(R.id.edtUrl);
        mWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl(mWebUrlInput.getText().toString());
            }
        });
    }

    /**
     * 웹뷰에서 HTML 페이지의 자바스크립트를 호출할 인터페이스
     */
    final class JavaScriptMethods {
        JavaScriptMethods() {}

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

    /**
     * 웹브라우저 클라이언트 클래스
     */
    final class WebBrowerClient extends WebChromeClient {
        public boolean onJsAlert(WebView wv, String url, String msg, JsResult result){

            result.confirm();

            return true;
        }
    }
}
