package com.huake.zhnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseFragment;
import com.huake.zhnews.ui.presenter.DailyFgPresenter;
import com.huake.zhnews.ui.view.IDailyFgView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Werb on 2016/9/2.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * DailyFragment
 */
public class DailyFragment extends MVPBaseFragment<IDailyFgView, DailyFgPresenter> implements IDailyFgView {

    @Bind(R.id.content_list)
    RecyclerView mContentList;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected DailyFgPresenter createPresenter() {
        return new DailyFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {

        return mContentList;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {

        return mLinearLayoutManager;
    }

    @Override
    public void requestDataRefresh() {
        mPresenter.getLatsNews("0");
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //初始化数据
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        //千万不要忘记这句话，非常重要
        mContentList.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getLatsNews("0");
        mPresenter.scrollRecycleView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
