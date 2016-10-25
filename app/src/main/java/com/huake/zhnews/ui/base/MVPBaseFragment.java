package com.huake.zhnews.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huake.zhnews.R;

import butterknife.ButterKnife;

/**
 * Created by Werb on 2016/7/27.
 * Werb is Wanbo.
 * Contact Me : werbhelius@gmail.com
 * 处理了布局，绑定推送者和刷新控件的逻辑
 */
public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    protected T mPresenter;

    private boolean mIsRequestDataRefresh = false;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(createViewLayoutId(),container,false);
        ButterKnife.bind(this,rootView);
        initView(rootView);
        if(isSetRefresh()) {
            //设置属性控件参数，调用这个控件必须设置
            setupSwipeRefresh(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }

    private void setupSwipeRefresh(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        if(mRefreshLayout != null){
            mRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,
                    R.color.refresh_progress_2,R.color.refresh_progress_3);
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            //原本应该是一个内部类的接口，结果用java8简单的实现了,而且是子类完成，可以设置成抽象类
            mRefreshLayout.setOnRefreshListener(this::requestDataRefresh);
        }
    }

    //应该改成抽象的
    public  void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }


    public void setRefresh(boolean requestDataRefresh) {

        //判断空指针异常罢了
        if (mRefreshLayout == null) {
            return;
        }



        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            mRefreshLayout.postDelayed(() -> {
                if (mRefreshLayout != null) {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        } else {
            mRefreshLayout.setRefreshing(true);
        }
    }

    protected abstract T createPresenter();

    protected abstract int createViewLayoutId();

    protected  void initView(View rootView){}

    public Boolean isSetRefresh(){
        return true;
    }

}

