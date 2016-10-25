package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/25 13:24
 * @描述	      
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.huake.zhnews.bean.daily.DailyTimeLine;
import com.huake.zhnews.ui.adapter.DailyFeedAdapter;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IDailyFeedView;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DailyFeedPresenter extends BasePresenter<IDailyFeedView> {

    private Context mContext;
    private IDailyFeedView mFeedView;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private boolean isLoadMore=false;
    private DailyTimeLine timeLine;
    private DailyFeedAdapter mDailyFeedAdapter;
    private String next_pager;
    private String has_more;
    private int mLastVisibleItemPosition;
    private String scroll_id;

    public DailyFeedPresenter(Context context) {
        mContext = context;
    }

    public void getDataFromNet(String id,String num){
        mFeedView = getView();
        scroll_id=id;
        if (mFeedView != null) {
            mRecyclerView = mFeedView.getRecyclerView();
            mLayoutManager = mFeedView.getLayoutManager();
            dailyApi.getDailyFeedDetail(id,num)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DailyTimeLine>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(mContext, "没有网络啦", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(DailyTimeLine dailyTimeLine) {
                            disPlayZhihuList(dailyTimeLine,mContext,mFeedView,mRecyclerView);
                        }
                    });
        }
    }

    //展示列表数据
    private void disPlayZhihuList(DailyTimeLine dailyTimeLine, Context context, IDailyFeedView dailyFeedView, RecyclerView recyclerView) {


        if(dailyTimeLine.getResponse().getLast_key()!=null){
            next_pager = dailyTimeLine.getResponse().getLast_key();
            has_more = dailyTimeLine.getResponse().getHas_more();
        }
        if (isLoadMore) {
            if (dailyTimeLine.getResponse().getOptions() == null) {
                dailyFeedView.setDataRefresh(false);
                return;
            }
            else {
                timeLine.getResponse().getOptions().addAll(dailyTimeLine.getResponse().getOptions());
            }
            mDailyFeedAdapter.notifyDataSetChanged();
        } else {
            timeLine = dailyTimeLine;
            mDailyFeedAdapter = new DailyFeedAdapter(mContext, dailyTimeLine.getResponse().getOptions());
            recyclerView.setAdapter(mDailyFeedAdapter);
        }
        dailyFeedView.setDataRefresh(false);
    }

    //scroll滑动监听
    public void scrollRecycleView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //闲置状态判断加载更多的情况
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                    //如果是最后一个，就要判断加载更多的情况了吧
                    //数组元素从0开始，所以要加一
                    Logger.e(mLastVisibleItemPosition+":"+mLayoutManager.getItemCount());
                    if (mLastVisibleItemPosition + 1 == mLayoutManager.getItemCount()) {
                        if(has_more.equals("true")) {
                            isLoadMore = true;
                            mFeedView.setDataRefresh(true);
                            new Handler().postDelayed(() -> getDataFromNet(scroll_id,next_pager), 1000);
                        }else {
                            isLoadMore=false;
                        }


                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

}
