package com.huake.zhnews.ui.adapter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 23:35
 * @描述	      
 */

import android.content.Context;
import android.content.Intent;
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
import com.huake.zhnews.bean.daily.Daily;
import com.huake.zhnews.bean.daily.HeadLine;
import com.huake.zhnews.bean.daily.Response;
import com.huake.zhnews.bean.zhihu.TopStories;
import com.huake.zhnews.ui.activity.DailyFeedActivity;
import com.huake.zhnews.ui.activity.GankWebActivity;
import com.huake.zhnews.util.ScreenUtil;
import com.huake.zhnews.widget.TopStoriesViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DailyListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private Response mResponse;
    public int load_state = LOAD_PULL_TO;

    //普通的刷新又分出了这么多， 下拉刷新，请求更多，没有数据，四种状态专门用来处理加载更多的尾部
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;

    private int feed_position = 0;//由于轮播图和headline使得正常列表多出来的值
    //这会列表内容比较多,非常繁琐
    private static final int TYPE_TOP = -1;
    private static final int TYPE_FOOTER = -2;
    private static final int TYPE_HEADLINE = -3;

    private static final int TYPE_NORMAL_PICTURE = -4;
    private static final int TYPE_NORMAL_TEXT = -5;
    private static final int TYPE_EMPTY = -6;

    public DailyListAdapter(Context context, Response response) {
        mContext = context;
        this.mResponse = response;
    }

    @Override
    public int getItemViewType(int position) {

        //弄了半天只判断头和尾啊
        if (position == 0) {
            return TYPE_TOP;
        } else if (position == 1) {
            return TYPE_HEADLINE;
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            //普通类型又分三种情况
            Daily daily = mResponse.getFeeds().get(position - 2);
            int type = daily.getType();
            if (type == 0 || type == 2) {
                return TYPE_NORMAL_PICTURE;
            } else if (type == 1) {
                return TYPE_NORMAL_TEXT;
            } else {
                return TYPE_EMPTY;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //        Logger.e("viewType " + viewType);
        //        Logger.e("count " + getItemCount());
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
            case TYPE_HEADLINE:
                View rootView = View.inflate(parent.getContext(), R.layout.item_daily_headline, null);
                return new HeadlineViewHolder(rootView);
            case TYPE_NORMAL_PICTURE:
                View rootView1 = View.inflate(parent.getContext(), R.layout.item_daily_feed_0, null);
                return new Feed_0_ViewHolder(rootView1);
            case TYPE_NORMAL_TEXT:
                View rootView2 = View.inflate(parent.getContext(), R.layout.item_daily_feed_1, null);
                return new Feed_1_ViewHolder(rootView2);
            case TYPE_EMPTY://不实现
                View rootView3 = View.inflate(parent.getContext(), R.layout.item_empty, null);
                return new EmptyViewHolder(rootView3);
        }
        View rootView3 = View.inflate(parent.getContext(), R.layout.item_empty, null);
        return new EmptyViewHolder(rootView3);
        //这种写法就是一个坑
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //            getItemViewType(position) //可以通过这种方法判断类型，不过还有中更加简单的方式
        if (holder instanceof DailyListAdapter.FooterViewHolder) {
            DailyListAdapter.FooterViewHolder footerViewHolder = (DailyListAdapter.FooterViewHolder) holder;
            //作者把封装方法写在了holder里！！
            footerViewHolder.bindItem();
        } else if (holder instanceof DailyListAdapter.TopStoriesViewHolder) {
            DailyListAdapter.TopStoriesViewHolder topStoriesViewHolder = (DailyListAdapter.TopStoriesViewHolder) holder;
            topStoriesViewHolder.bindItem(mResponse.getBanners());
        } else if (holder instanceof Feed_1_ViewHolder) {
            Daily daily = mResponse.getFeeds().get(position - 2);
            //由于数据的原因，必须判断类型，如果没有类型就不设数据，好坑爹啊，服务器又干什么坏事了
            Feed_1_ViewHolder feed1viewholder = (Feed_1_ViewHolder) holder;
            feed1viewholder.bindItem(daily);
        } else if (holder instanceof Feed_0_ViewHolder) {
            Daily daily = mResponse.getFeeds().get(position - 2);
            Feed_0_ViewHolder feed0viewholder = (Feed_0_ViewHolder) holder;
            feed0viewholder.bindItem(daily);
        } else if (holder instanceof HeadlineViewHolder) {
            HeadlineViewHolder headlineViewHolder = (HeadlineViewHolder) holder;
            headlineViewHolder.bindItem(mResponse.getHeadline());
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mResponse.getListSize() + 1;//请求头和headline已经帮你加好了，所以再一个请求尾部就可以了
    }

    //写这两个方法就是为了开启轮播图功能而已
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ZhihuListAdapter.TopStoriesViewHolder) {
            ZhihuListAdapter.TopStoriesViewHolder topStoriesViewHolder = (ZhihuListAdapter.TopStoriesViewHolder) holder;
            topStoriesViewHolder.mVpTopStories.startAutoRun();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ZhihuListAdapter.TopStoriesViewHolder) {
            ZhihuListAdapter.TopStoriesViewHolder topStoriesViewHolder = (ZhihuListAdapter.TopStoriesViewHolder) holder;
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

        public void bindItem(List<Daily> banners) {
            //因为轮播图的类型写死了，所以需要转换一下，好坑爹，应该定义成泛型
            List<TopStories> topList = new ArrayList<>();
            for (Daily d : banners) {
                TopStories t = new TopStories();
                t.setImage(d.getImage());
                t.setTitle(d.getPost().getTitle());
                t.setUrl(d.getPost().getAppview());
                topList.add(t);
            }
            mVpTopStories.init(topList, mTvTopTitle, new TopStoriesViewPager.ViewPagerClickListenner() {
                @Override
                public void onClick(TopStories item) {
                    mContext.startActivity(GankWebActivity.newIntent(mContext,item.getUrl()));
                }
            });

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

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
            switch (load_state) {
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

    class Feed_1_ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_feed_1_title)
        TextView tv_feed_1_title;
        @Bind(R.id.tv_feed_1_type)
        TextView tv_feed_1_type;
        @Bind(R.id.iv_feed_1_type_icon)
        ImageView iv_feed_1_type_icon;
        @Bind(R.id.iv_feed_1_icon)
        ImageView iv_feed_1_icon;
        @Bind(R.id.card_feed_1)
        CardView card_feed_1;

        public Feed_1_ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ScreenUtil screenUtil = ScreenUtil.instance(mContext);
            //这种适配方法值得学习
            card_feed_1.setLayoutParams(new LinearLayout.LayoutParams(screenUtil.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public void bindItem(Daily daily) {
            tv_feed_1_title.setText(daily.getPost().getTitle());
            tv_feed_1_type.setText(daily.getPost().getCategory().getTitle());
            Glide.with(mContext).load(daily.getPost().getCategory().getImage_lab()).centerCrop().into(iv_feed_1_type_icon);
            Glide.with(mContext).load(daily.getImage()).centerCrop().into(iv_feed_1_icon);

            card_feed_1.setOnClickListener(v -> {
                mContext.startActivity(GankWebActivity.newIntent(mContext, daily.getPost().getAppview()));

            });
        }


    }


    class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(View rootView) {
            super(rootView);
        }
    }

    class Feed_0_ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_feed_0_icon)
        ImageView iv_feed_0_icon;
        @Bind(R.id.tv_feed_0_title)
        TextView tv_feed_0_title;
        @Bind(R.id.tv_feed_0_desc)
        TextView tv_feed_0_desc;
        @Bind(R.id.iv_feed_0_type)
        ImageView iv_feed_0_type;
        @Bind(R.id.tv_Feed_0_type)
        TextView tv_Feed_0_type;
        @Bind(R.id.card_layout)
        CardView card_layout;

        public Feed_0_ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ScreenUtil screenUtil = ScreenUtil.instance(mContext);
            card_layout.setLayoutParams(new LinearLayout.LayoutParams(screenUtil.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public void bindItem(Daily daily) {
            tv_feed_0_title.setText(daily.getPost().getTitle());
            tv_feed_0_desc.setText(daily.getPost().getDescription());
            tv_Feed_0_type.setText(daily.getPost().getCategory().getTitle());
            Glide.with(mContext).load(daily.getImage()).centerCrop().into(iv_feed_0_icon);

            if (daily.getType() == 0) {
                Glide.with(mContext).load(R.drawable.feed_0_icon).centerCrop().into(iv_feed_0_type);
                                card_layout.setOnClickListener(v -> {
                                    Intent intent = DailyFeedActivity.newIntent(mContext, daily.getPost().getId(), daily.getPost().getDescription(), daily.getPost().getTitle(), daily.getImage());
                                    mContext.startActivity(intent);
                                });
            } else if (daily.getType() == 2) {

                Glide.with(mContext).load(R.drawable.feed_1_icon).centerCrop().into(iv_feed_0_type);
                                card_layout.setOnClickListener(v -> {
                                    Intent intent = GankWebActivity.newIntent(mContext, daily.getPost().getAppview());
                                    mContext.startActivity(intent);
                                });
            }


        }
    }

    class HeadlineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_headline_1)
        TextView tv_headline_1;
        @Bind(R.id.tv_headline_2)
        TextView tv_headline_2;
        @Bind(R.id.tv_headline_3)
        TextView tv_headline_3;
        @Bind(R.id.card_headline)
        CardView card_headline;

        public HeadlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(Daily daily) {
            List<HeadLine> headLines = daily.getList();
            tv_headline_1.setText(headLines.get(0).getDescription());
            tv_headline_2.setText(headLines.get(1).getDescription());
            tv_headline_3.setText(headLines.get(2).getDescription());

            card_headline.setOnClickListener(v -> {
                mContext.startActivity(GankWebActivity.newIntent(mContext, daily.getPost().getAppview()));

            });
        }
    }

    // 提供给外部更新下拉进度条的装
    public void updateLoadStatus(int status) {
        this.load_state = status;
        notifyDataSetChanged();
    }
}
