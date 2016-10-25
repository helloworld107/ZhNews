package com.huake.zhnews.ui.adapter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 9:16
 * @描述	      
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huake.zhnews.R;
import com.huake.zhnews.bean.zhihu.NewsTimeLine;
import com.huake.zhnews.bean.zhihu.Stories;
import com.huake.zhnews.bean.zhihu.TopStories;
import com.huake.zhnews.ui.activity.ZhihuWebActivity;
import com.huake.zhnews.util.ScreenUtil;
import com.huake.zhnews.widget.TopStoriesViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ZhihuListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private NewsTimeLine mNewsTimeLine;
    public int load_state=LOAD_PULL_TO;
    //普通的刷新又分出了这么多， 下拉刷新，请求更多，没有数据，四种状态专门用来处理加载更多的尾部
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;

    //轮播图和加载尾部是两种情况，其次都是正常情况
    private static final int TYPE_TOP = -1;
    private static final int TYPE_FOOTER =-2;
    private static final int TYPE_NORMAL =-3;

    public ZhihuListAdapter(Context context, NewsTimeLine newsTimeLine) {
        mContext = context;
        mNewsTimeLine = newsTimeLine;
    }

    @Override
    public int getItemViewType(int position) {
        //弄了半天只判断头和尾啊
        if (mNewsTimeLine.getTop_stories() != null) {
            if (position == 0) {
                return TYPE_TOP;
            } else if (position + 1 == getItemCount()) {//角标从0开始，所以加一
                return TYPE_FOOTER;
            } else {
                return TYPE_NORMAL;
            }
        } else if (getItemCount() == position + 1) {//角标从0开始，所以加一
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //仅仅就是简单的判断三种情况，而不是添加请求头，开发者也很水，还想换工作，呵呵了
        switch (viewType) {
            case TYPE_TOP:
                View inflate1 = View.inflate(mContext, R.layout.item_zhihu_top_stories, null);
                TopStoriesViewHolder topStoriesViewHolder = new TopStoriesViewHolder(inflate1);
                return topStoriesViewHolder;
            case TYPE_FOOTER:
                View inflate2 = View.inflate(mContext, R.layout.activity_view_footer, null);
                FooterViewHolder footerViewHolder = new FooterViewHolder(inflate2);
                return footerViewHolder;
            default:
                View inflate3 = View.inflate(mContext, R.layout.item_zhihu_stories, null);
                StoriesViewHolder storiesViewHolder = new StoriesViewHolder(inflate3);
                return storiesViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            getItemViewType(position) //可以通过这种方法判断类型，不过还有中更加简单的方式
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            //作者把封装方法写在了holder里！！
            footerViewHolder.bindItem();
        }else if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.bindItem(mNewsTimeLine.getTop_stories());
        } else if (holder instanceof StoriesViewHolder) {
            StoriesViewHolder storiesViewHolder = (StoriesViewHolder) holder;
            storiesViewHolder.bindItem(mNewsTimeLine.getStories().get(position-1));//减去的是轮播图的内容
        }
    }

    @Override
    public int getItemCount() {
        return mNewsTimeLine.getStories().size() + 2;//多了请求头和请求尾
    }

    //写这两个方法就是为了开启轮播图功能而已
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.mVpTopStories.startAutoRun();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.mVpTopStories.stopAutoRun();
        }
    }

    class TopStoriesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.vp_top_stories)
        TopStoriesViewPager mVpTopStories;
        @Bind(R.id.tv_top_title)
        TextView mTvTopTitle;

            TopStoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        public void bindItem(List<TopStories> topList) {

           mVpTopStories.init(topList,mTvTopTitle, new TopStoriesViewPager.ViewPagerClickListenner() {
               @Override
               public void onClick(TopStories item) {
                   mContext.startActivity(ZhihuWebActivity.newIntent(mContext,item.getId()));
               }
           });

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.progress)
        ProgressBar mProgress;
        @Bind(R.id.tv_load_prompt)
        TextView mTvLoadPrompt;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //宽度好定，高度麻烦，根据屏幕适配方法用代码写一下
            ScreenUtil instance = ScreenUtil.instance(mContext);
            int height = instance.dip2px(40);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            view.setLayoutParams(layoutParams);

        }
        public void bindItem() {
            //尾部加载 更多就要判断类型咯
            switch (load_state){
                case LOAD_MORE:
                    mTvLoadPrompt.setText("正在加载中。。。");
                    mProgress.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    mTvLoadPrompt.setText("上拉加载更多");
                    mProgress.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    mTvLoadPrompt.setText("已无更多数据");
                    mProgress.setVisibility(View.GONE);
                    break;
                case LOAD_END:
                    mProgress.setVisibility(View.GONE);
                    break;
            }
        }
    }

    class StoriesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_stories_title)
        TextView mTvStoriesTitle;
        @Bind(R.id.iv_stories_img)
        ImageView mIvStoriesImg;
        @Bind(R.id.card_stories)
        CardView mCardStories;

        StoriesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            //定制了一下caraview的大小
            ScreenUtil instance = ScreenUtil.instance(mContext);
            int screenWidth = instance.getScreenWidth();
            mCardStories.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        //设置数据
        public void bindItem(Stories stories) {
            Glide.with(mContext).load(stories.getImages()[0]).centerCrop().into(mIvStoriesImg);
            mTvStoriesTitle.setText(stories.getTitle());
            mCardStories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(ZhihuWebActivity.newIntent(mContext,stories.getId()));
                }
            });
        }
    }
    // 提供给外部更新下拉进度条的装
    public void updateLoadStatus(int status) {
        this.load_state=status;
        notifyDataSetChanged();
    }
}
