package com.huake.zhnews.ui.base;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 15:15
 * @描述	      
 */

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.huake.zhnews.R;

import butterknife.ButterKnife;


public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;
    protected AppBarLayout mAppBar;
    protected Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mIsRequestDataRefresh = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //允许为空，不是所有都要实现MVP模式
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
//        initView();
        //        设置其他公共布局文件
        //因为很多布局文件起了一样的id名字，所以可以在公共基拿取该活动所对应布局的控件，感觉耦合度太高了哈
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //别忘了toolbar要单独设置我
        initToolbar();
        //刷新控件也搞在了这里，这样感觉很不灵活呀
        if (isSetRefresh()) {
            setupSwipeRefresh();
        }
        initTab();//初始化tab框
        initData();
    }

    public void initData() {
    }

    private void initToolbar() {
        if (mToolbar != null & mAppBar != null) {
            setSupportActionBar(mToolbar);
            //如果不是主界面，toolbar应该显示返回的按钮
            if (canBack()) {
                ActionBar supportActionBar = getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
            //适配
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }
    }

    public void initTab() {
    }

    //    protected abstract void initView();

    private void setupSwipeRefresh() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,
                R.color.refresh_progress_2, R.color.refresh_progress_3);
        mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        //本来应该放入一个刷新监听，为什么这里仅仅放入了一个刷新的方法，难度只是为了复写？
        mRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
    }

    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    public abstract T createPresenter();

    public abstract int provideContentViewId();//用于引入布局文件

    /**
     * 判断当前 Activity 是否允许返回
     * 主界面不允许返回，次级界面允许返回
     *
     * @return false
     */
    public boolean canBack() {
        return false;
    }

    public Boolean isSetRefresh() {
        return false;
    }

    //实现返回键的功能


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    //设置swipe能否刷新
    public void setRefresh(boolean requestDataRefresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (requestDataRefresh){
            mRefreshLayout.setRefreshing(true);
        }else {
            //如果不能刷新就关闭
            mIsRequestDataRefresh=false;
            //为什么不能刷新要走这么一个缓慢的过程，奇怪
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRefreshLayout != null) {
                        mRefreshLayout.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }
}
