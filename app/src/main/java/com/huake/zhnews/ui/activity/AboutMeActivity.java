package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 21:48
 * @描述	      
 */

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.TextView;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.base.MVPBaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

import static com.huake.zhnews.R.id.tv_blog;
import static com.huake.zhnews.R.id.tv_github;

public class AboutMeActivity extends MVPBaseActivity {

    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(tv_github)
    TextView mTvGithub;
    @Bind(tv_blog)
    TextView mTvBlog;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_about_me;
    }

    @Override
    public void initData() {
        super.initData();
        mCollapsingToolbarLayout.setTitle("从你的全世界路过");
    }

    @OnClick({R.id.tv_github, tv_blog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_github:
                Intent it1 = new Intent(Intent.ACTION_VIEW, Uri.parse(mTvGithub.getText().toString()));
                it1.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(it1);
                break;
            case R.id.tv_blog:
                Intent it2 = new Intent(Intent.ACTION_VIEW, Uri.parse(mTvBlog.getText().toString()));
                it2.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(it2);
                break;
        }
    }

    @Override
    public boolean canBack() {
        return true;
    }
}
