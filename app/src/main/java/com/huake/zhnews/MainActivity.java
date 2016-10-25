package com.huake.zhnews;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.huake.zhnews.ui.activity.AboutMeActivity;
import com.huake.zhnews.ui.activity.GankWebActivity;
import com.huake.zhnews.ui.adapter.ViewPagerFgAdapter;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.base.MVPBaseActivity;
import com.huake.zhnews.ui.base.MVPBaseFragment;
import com.huake.zhnews.ui.fragment.DailyFragment;
import com.huake.zhnews.ui.fragment.GankFragment;
import com.huake.zhnews.ui.fragment.ZhihuFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends MVPBaseActivity {

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.content_viewPager)
    ViewPager mContentViewPager;
    private List<MVPBaseFragment> fragmentList;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initTab() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ZhihuFragment());
        fragmentList.add(new GankFragment());
        fragmentList.add(new DailyFragment());
        //设置viewpager
        mContentViewPager.setAdapter(new ViewPagerFgAdapter(getSupportFragmentManager(),fragmentList));
        mContentViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mContentViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.today_github:
                String github_trending = "https://github.com/trending";
                startActivity(GankWebActivity.newIntent(this,github_trending));
                return true;
            case R.id.about_me:
                Intent intent = new Intent(this, AboutMeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
