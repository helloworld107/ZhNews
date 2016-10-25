package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 23:26
 * @描述	      
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.huake.zhnews.bean.daily.DailyTimeLine;
import com.huake.zhnews.ui.adapter.DailyListAdapter;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IDailyFgView;
import com.orhanobut.logger.Logger;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DailyFgPresenter extends BasePresenter<IDailyFgView> {
    private Context mContext;
    private IDailyFgView mIDailyFgView;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutManager;//拿到列表数量用的
    private boolean isLoadMore = false; // 是否加载过更多
    private DailyListAdapter mDailyListAdapter;
    private DailyTimeLine timeLine;
    private int mLastVisibleItemPosition;
    private String next_pager;//数据还有页数之分
    private String has_more;
    private boolean isHeadRefresh =true;//判断是下拉刷新还是上拉加载更多


    public DailyFgPresenter(Context context) {
        mContext = context;
    }

    //得到信息，显示数据
    public void getLatsNews(String num) {
        //需要mview的三个方法都用到了,取得控件，取得布局，取得视图
        mIDailyFgView = getView();
        if (isHeadRefresh) {
            mIDailyFgView.setDataRefresh(true);
        }else {
            isHeadRefresh=true;
        }
        mRecyclerview = mIDailyFgView.getRecyclerView();
        mLinearLayoutManager = mIDailyFgView.getLayoutManager();
        //读取网络数据
        dailyApi.getDailyTimeLine(num).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DailyTimeLine>() {
                    @Override
                    public void call(DailyTimeLine dailyTimeLine) {
                        if (dailyTimeLine.getMeta().getMsg().equals("success")) {
                            disPlayZhihuList(dailyTimeLine, mContext, mIDailyFgView, mRecyclerview);
                        }
                    }
                });
    }


    //展示列表数据
    private void disPlayZhihuList(DailyTimeLine dailyTimeLine, Context context, IDailyFgView iDailyFgView, RecyclerView recyclerView) {


        //下一个页数
        if(dailyTimeLine.getResponse().getLast_key()!=null){
            next_pager = dailyTimeLine.getResponse().getLast_key();
        }
        //是否加载更多
        has_more = dailyTimeLine.getResponse().getHas_more();
        //判断是否加载更多
        if (isLoadMore) {
            //是否加载更多以时间为标记呀,时间是第一次访问存的，没有访问过第一次肯定不会加载更多
            if (timeLine==null||dailyTimeLine.getResponse().getFeeds()==null) {
                iDailyFgView.setDataRefresh(false);
                mDailyListAdapter.updateLoadStatus(DailyListAdapter.LOAD_NONE);
                return;
            }
            timeLine.getResponse().getFeeds().addAll(dailyTimeLine.getResponse().getFeeds());
            mDailyListAdapter.notifyDataSetChanged();
        } else {
            timeLine=dailyTimeLine;
            mDailyListAdapter = new DailyListAdapter(mContext,timeLine.getResponse());
            recyclerView.setAdapter(mDailyListAdapter);//该写适配器了吧
            mDailyListAdapter.updateLoadStatus(DailyListAdapter.LOAD_NONE);
        }
        iDailyFgView.setDataRefresh(false);
    }

    //scroll滑动监听
    public void scrollRecycleView() {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //闲置状态判断加载更多的情况
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                    //如果是最后一个，就要判断加载更多的情况了吧
                    //数组元素从0开始，所以要加一
                    Logger.e(mLastVisibleItemPosition+":"+mLinearLayoutManager.getItemCount());
                    if (mLastVisibleItemPosition + 1 == mLinearLayoutManager.getItemCount()) {
//                        Toast.makeText(mContext, "come", Toast.LENGTH_SHORT).show();
                        isHeadRefresh=false;
                        mDailyListAdapter.updateLoadStatus(DailyListAdapter.LOAD_PULL_TO);
                        if(has_more.equals("true")) {
                            isLoadMore = true;
                            mDailyListAdapter.updateLoadStatus(DailyListAdapter.LOAD_MORE);
                            new Handler().postDelayed(() -> getLatsNews(next_pager), 1000);
                        }else {
                            isLoadMore=false;
                            mDailyListAdapter.updateLoadStatus(DailyListAdapter.LOAD_NONE);
                        }


                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

}
