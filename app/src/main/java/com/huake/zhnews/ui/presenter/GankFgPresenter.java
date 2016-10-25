package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 16:03
 * @描述	      
 */

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.huake.zhnews.bean.gank.Gank;
import com.huake.zhnews.bean.gank.Meizhi;
import com.huake.zhnews.bean.gank.Video;
import com.huake.zhnews.ui.adapter.GankListAdapter;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IGankFgView;
import com.huake.zhnews.util.DateUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class GankFgPresenter extends BasePresenter<IGankFgView> {

    private Context mContext;
    private IGankFgView mView;
    private RecyclerView mRecyclerview;
    private boolean isLoadMore = false; // 是否加载过更多
    private List<Gank> lists;
    private int mLastVisibleItemPosition;
    private GridLayoutManager mGridLayoutManager;
    private int page = 1;//服务器这个数据是分页加载的
    private GankListAdapter mGankListAdapter;


    public GankFgPresenter(Context context) {
        mContext = context;
    }

    //得到信息，显示数据
    public void getGankData() {
        mView = getView();
        mView.setDataRefreshIs(true);
        if (mView != null) {
            mRecyclerview = mView.getRecyclerview();
            mGridLayoutManager = mView.getGridLayoutManager();

            if (isLoadMore) {
                page = page + 1;
            }
            //读取网络数据,压缩加载，还不清楚机理,类似于一个合成功能，可以把多个api的参数同时处理返回一个自己想要的结果
            //
            Observable.zip(gankApi.getMeizhiData(page), gankApi.getVideoData(page), new Func2<Meizhi, Video, Meizhi>() {
                @Override
                public Meizhi call(Meizhi meizhi, Video video) {
                    return creatDesc(meizhi, video);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Meizhi>() {
                        @Override
                        public void onCompleted() {
                            mView.setDataRefreshIs(false);

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(mContext, "网络不见了", Toast.LENGTH_SHORT).show();
                            mView.setDataRefreshIs(false);

                        }

                        @Override
                        public void onNext(Meizhi meizhi) {

                            disPlayGankList(meizhi.getResults(), mContext, mView, mRecyclerview);
                        }
                    });
        }


    }


    private void disPlayGankList(List<Gank> datas, Context context, IGankFgView view, RecyclerView recyclerview) {
        //判断是否加载更多
        if (isLoadMore) {
            //是否加载更多以时间为标记呀,时间是第一次访问存的，没有访问过第一次肯定不会加载更多
            if (lists == null) {
                mView.setDataRefreshIs(false);
                return;
            }
            lists.addAll(datas);
            mGankListAdapter.notifyDataSetChanged();
        } else {
            lists = datas;
            mGankListAdapter = new GankListAdapter(context, lists);
            if (mGankListAdapter != null) {
                recyclerview.setAdapter(mGankListAdapter);//该写适配器了吧
            }
        }
        //停止刷新图标
//        isLoadMore = false;真是自作聪明啊，作者就从来没有去考虑过没有数据加载不了更多的意图，加这就话只会造成bug,哎
        view.setDataRefreshIs(false);
    }

    //scroll滑动监听
    public void scrollRecycleView() {
        mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mGankListAdapter==null){
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                    if (mGankListAdapter.getItemCount() == mLastVisibleItemPosition + 1) {
                        //如果是最后一个我应该刷新呀
                        mView.setDataRefreshIs(true);
                        isLoadMore = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getGankData();

                            }
                        }, 1000);

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    //    // /**
    //    * MeiZhi = list , gankmeizhi = 福利
    //    *这完全是服务器数取处理的问题，很坑爹，不用深刻，主要知道作者匹配两个集合的思想，
    //            * 先遍历一个集合，再在集合中遍历另外一个，相当于循环的循环
    //    *
    //
    private Meizhi creatDesc(Meizhi meizhi, Video video) {
        for (Gank gankmeizhi : meizhi.getResults()) {
            gankmeizhi.desc = gankmeizhi.desc + " " +
                    getVideoDesc(gankmeizhi.getPublishedAt(), video.getResults());
        }
        return meizhi;
    }

    //匹配同一天的福利描述和视频描述
    private String getVideoDesc(Date publishedAt, List<Gank> results) {
        String videoDesc = "";
        for (int i = 0; i < results.size(); i++) {
            Gank video = results.get(i);
            //出版日期没有设置成创建日期
            if (video.getPublishedAt() == null)
                video.setPublishedAt(video.getCreatedAt());
            //只是匹配同一天描述信息的信息
            if (DateUtils.isSameDate(publishedAt, video.getPublishedAt())) {
                videoDesc = video.getDesc();
                break;
            }
        }
        return videoDesc;
    }
}
