package com.huake.zhnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseFragment;
import com.huake.zhnews.ui.presenter.GankFgPresenter;
import com.huake.zhnews.ui.view.IGankFgView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Werb on 2016/8/18.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * GankFragment
 */
public class GankFragment extends MVPBaseFragment<IGankFgView,GankFgPresenter> implements IGankFgView {

    @Bind(R.id.content_list)
    RecyclerView mContentList;
    private GridLayoutManager mManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getGankData();
        mPresenter.scrollRecycleView();
    }

    @Override
    protected GankFgPresenter createPresenter() {
        return new GankFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    public void setDataRefreshIs(boolean b) {
        setRefresh(b);
    }

    @Override
    public RecyclerView getRecyclerview() {
        return mContentList;
    }

    @Override
    public GridLayoutManager getGridLayoutManager() {
        return mManager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mManager = new GridLayoutManager(getContext(), 2);
        mContentList.setLayoutManager(mManager);//非常坑爹，这句话不设置就会不显示，还会报错

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        //父类方法，掉用刷新状态
        setDataRefreshIs(true);
        mPresenter.getGankData();//执行完这句方法才会刷新结束，并不是假的
    }
}
