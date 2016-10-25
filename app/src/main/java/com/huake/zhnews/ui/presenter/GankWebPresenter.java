package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 23:19
 * @描述	      
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IGankWebView;

public class GankWebPresenter extends BasePresenter<IGankWebView> {

    private Activity mActivity;
    private ProgressBar mProgressBar;

    public GankWebPresenter(Activity activity) {
        mActivity = activity;
    }

    public void  setWebView(String url){
        IGankWebView view = getView();
        WebView webView = view.getWebView();
        mProgressBar = view.getProgressBar();
        //设置webview属性
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
//        settings.setBuiltInZoomControls(true);//显示放大缩小按钮  不管用
//        settings.setUseWideViewPort(true);//双击放大缩小
        //设置监听
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
//            * 所有跳转的链接都在此方法中回调
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        //设置针对javascript的监听，两个监听混合的用
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mActivity.setTitle(title);
            }
        });
        webView.loadUrl(url);

    }
}
