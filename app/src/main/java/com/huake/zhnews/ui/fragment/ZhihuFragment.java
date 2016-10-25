package com.huake.zhnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseFragment;
import com.huake.zhnews.ui.presenter.ZhihuFgPresenter;
import com.huake.zhnews.ui.view.IZhihuFgView;

import butterknife.Bind;

/**
 * Created by Werb on 2016/8/18.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * fragment自己就是一个接口view
 */
public class ZhihuFragment extends MVPBaseFragment<IZhihuFgView,ZhihuFgPresenter> implements IZhihuFgView{

    @Bind(R.id.content_list)
    RecyclerView content_list;//recyceview必须要设置linermanager
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getLatsNews();//读取信息
        //设置滑动监听
        mPresenter.scrollRecycleView();
    }

    @Override
    protected ZhihuFgPresenter createPresenter() {
       return new ZhihuFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_zhihu;
    }

    @Override
    protected void initView(View rootView) {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        content_list.setLayoutManager(mLinearLayoutManager);
    }

    //view必须继承方法，低耦合
    @Override
    public void setDataRefreshIs(boolean bool) {
        setRefresh(bool);//父类已经写好
    }

    @Override
    public RecyclerView getRecyclerview() {
        return content_list;
    }

    @Override
    public LinearLayoutManager getLinearLayoutManager() {
        return mLinearLayoutManager;
    }

    //swiperefreh刷新控件所执行的方法
    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        //父类方法，掉用刷新状态
        mPresenter.getLatsNews();//执行完这句方法才会刷新结束，并不是假的
    }
}