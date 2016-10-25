package com.huake.zhnews.ui.adapter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/25 13:34
 * @描述	      
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huake.zhnews.R;
import com.huake.zhnews.bean.daily.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.huake.zhnews.R.id.tv_feed_author_name;
import static com.huake.zhnews.R.id.tv_feed_content;

public class DailyFeedAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Options> mOptionses;

    public DailyFeedAdapter(Context context, List<Options> optionses) {
        mContext = context;
        mOptionses = optionses;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.item_daily_feed_option, null);
        return new FeedViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedViewHolder) {
            FeedViewHolder feedViewHolder = (FeedViewHolder) holder;
            feedViewHolder.bindItem(mOptionses.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mOptionses.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_feed_author_icon)
        ImageView mIvFeedAuthorIcon;
        @Bind(tv_feed_author_name)
        TextView mTvFeedAuthorName;
        @Bind(tv_feed_content)
        TextView mTvFeedContent;
        @Bind((R.id.card_option))
        CardView card_option;

        FeedViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindItem(Options options, int position) {
            mTvFeedAuthorName.setText(options.getAuthor().getName());
            mTvFeedContent.setText(options.getContent());
            Glide.with(mContext).load(options.getAuthor().getAvatar()).centerCrop().into(mIvFeedAuthorIcon);
            //设置不同的颜色，我服
            if (position % 4 == 0) {
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_1));
            } else if (position % 4 == 1) {
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_2));
            } else if (position % 4 == 2) {
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_3));
            } else if (position % 4 == 3) {
                card_option.setCardBackgroundColor(mContext.getResources().getColor(R.color.card_4));
            }
        }
    }
}
