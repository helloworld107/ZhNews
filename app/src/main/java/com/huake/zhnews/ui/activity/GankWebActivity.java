package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 23:17
 * @描述	      
 */

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseActivity;
import com.huake.zhnews.ui.presenter.GankWebPresenter;
import com.huake.zhnews.ui.view.IGankWebView;

import butterknife.Bind;

public class GankWebActivity extends MVPBaseActivity<IGankWebView, GankWebPresenter> implements IGankWebView {

    @Bind(R.id.pb_progress)
    ProgressBar mPbProgress;
    @Bind(R.id.url_web)
    WebView mUrlWeb;
    public static final String GANK_URL = "gank_url";

    @Override
    public ProgressBar getProgressBar() {
        return mPbProgress;
    }

    @Override
    public WebView getWebView() {
        return mUrlWeb;
    }

    @Override
    public GankWebPresenter createPresenter() {
        return new GankWebPresenter(this);
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_gank_web;
    }

    @Override
    public void initData() {
        super.initData();
        String url = getIntent().getStringExtra(GANK_URL);
        mPresenter.setWebView(url);
    }

    public static  Intent newIntent(Context context,String url){
        Intent intent = new Intent(context,GankWebActivity.class);
        intent.putExtra(GANK_URL,url);
        return intent;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlWeb.destroy();
    }
}
