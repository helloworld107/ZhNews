package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 19:16
 * @描述
 */


import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseActivity;
import com.huake.zhnews.ui.presenter.ZhihuWebPresenter;
import com.huake.zhnews.ui.view.IZhihuWebView;

import butterknife.Bind;

public class ZhihuWebActivity extends MVPBaseActivity<IZhihuWebView,ZhihuWebPresenter> implements IZhihuWebView {

    private static final String ID = "id";

    @Bind(R.id.iv_web_img)
    ImageView mIvWebImg;
    @Bind(R.id.tv_img_title)
    TextView mTvImgTitle;
    @Bind(R.id.tv_img_source)
    TextView mTvImgSource;
    @Bind(R.id.web_view)
    WebView mWebView;

    @Override
    public int provideContentViewId() {
        return R.layout.activity_web_view;
    }

    //巧妙让外部类调用调用
    public static Intent newIntent(Context context, String id){
        Intent intent = new Intent(context,ZhihuWebActivity.class);
        //还可以附加一个标识
        intent.putExtra(ZhihuWebActivity.ID,id);
        return intent;
    }

    @Override
    public void initData() {
        String data = getIntent().getStringExtra(ID);
        mPresenter.getDetailNews(data);

    }

    @Override
    public ZhihuWebPresenter createPresenter() {
        return  new ZhihuWebPresenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destoryImg();
    }

    @Override
    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public ImageView getWebImage() {
        return mIvWebImg;
    }

    @Override
    public TextView getImgTitle() {
        return mTvImgTitle;
    }

    @Override
    public TextView getImgSource() {
        return mTvImgSource;
    }
    @Override
    public boolean canBack() {
        return true;
    }
}
