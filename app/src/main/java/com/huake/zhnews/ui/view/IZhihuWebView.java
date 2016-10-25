package com.huake.zhnews.ui.view;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 19:17
 * @描述	      
 */

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public interface IZhihuWebView {
    WebView getWebView();
    ImageView getWebImage();
    TextView getImgTitle();
    TextView getImgSource();
}
