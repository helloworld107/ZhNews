package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 9:03
 * @描述	      
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.huake.zhnews.R;
import com.huake.zhnews.bean.zhihu.NewsTimeLine;
import com.huake.zhnews.ui.adapter.ZhihuListAdapter;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IZhihuFgView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//基类presenter就干了一件事，推送者和视图捆绑
public class ZhihuFgPresenter extends BasePresenter<IZhihuFgView> {

    private Context mContext;
    private IZhihuFgView mView;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutManager;//拿到列表数量用的
    private NewsTimeLine mTimeLine;
    private boolean isLoadMore = false; // 是否加载过更多
    private ZhihuListAdapter mZhihuListAdapter;
    private String mtime;
    private int mLastVisibleItemPosition;
    private boolean isHeadRefresh =true;//判断是下拉刷新还是上拉加载更多


    public ZhihuFgPresenter(Context context) {
        mContext = context;
    }

    //得到信息，显示数据
    public void getLatsNews() {
        //需要mview的三个方法都用到了,取得控件，取得布局，取得视图

        mView = getView();
        if (isHeadRefresh){
            mView.setDataRefreshIs(true);
        }else {
            isHeadRefresh=true;
        }
        mRecyclerview = mView.getRecyclerview();
        mLinearLayoutManager = mView.getLinearLayoutManager();
        //读取网络数据
        zhihuApi.getLatestNews().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsTimeLine>() {
                    @Override
                    public void call(NewsTimeLine newsTimeLine) {
                        disPlayZhihuList(newsTimeLine, mContext, mView, mRecyclerview);
                    }
                });


    }

    //得到原来的信息，有储存时间
    public void getBeforeNews(String time) {
        mView = mViewRef.get();
        mRecyclerview = mView.getRecyclerview();
        mLinearLayoutManager = mView.getLinearLayoutManager();
        //读取网络数据
        zhihuApi.getBeforetNews(time).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsTimeLine>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, R.string.load_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(NewsTimeLine newsTimeLine) {
                        disPlayZhihuList(newsTimeLine, mContext, mView, mRecyclerview);
                    }
                });

    }

    ;

    //展示列表数据
    private void disPlayZhihuList(NewsTimeLine newsTimeLine, Context context, IZhihuFgView iZhihuFgView, RecyclerView recyclerView) {
        //判断是否加载更多
        if (isLoadMore) {
            //是否加载更多以时间为标记呀,时间是第一次访问存的，没有访问过第一次肯定不会加载更多
            if (mtime == null) {
                mZhihuListAdapter.updateLoadStatus(ZhihuListAdapter.LOAD_NONE);
                return;
            } else {
                mTimeLine.getStories().addAll(newsTimeLine.getStories());
            }
            mZhihuListAdapter.notifyDataSetChanged();
        } else {
            mTimeLine = newsTimeLine;
            mZhihuListAdapter = new ZhihuListAdapter(mContext, newsTimeLine);
            recyclerView.setAdapter(mZhihuListAdapter);//该写适配器了吧
            //            mZhihuListAdapter.notifyDataSetChanged();//这个方法有争议，可以不写吗？？
        }
        //停止刷新图标
        iZhihuFgView.setDataRefreshIs(false);
        mtime = newsTimeLine.getDate();
    }

    //scroll滑动监听
    public void scrollRecycleView() {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //闲置状态判断加载更多的情况
                if (mZhihuListAdapter == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                    //如果是最后一个，就要判断加载更多的情况了吧
                    //数组元素从0开始，所以要加一
                    if (mLastVisibleItemPosition + 1 == mLinearLayoutManager.getItemCount()) {
                        //其实判断的有点详细，因为只是一个圆圈嘛，这个两个状态完全可以合并
                        isHeadRefresh=false;
                        mZhihuListAdapter.updateLoadStatus(ZhihuListAdapter.LOAD_PULL_TO);
                        isLoadMore = true;
                        mZhihuListAdapter.updateLoadStatus(ZhihuListAdapter.LOAD_MORE);
                        //耗时操作在子线程中执行,并且模拟加载一秒的环境
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getBeforeNews(mtime);
                            }
                        }, 1000);
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
