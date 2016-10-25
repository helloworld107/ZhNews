package com.huake.zhnews.ui.adapter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 16:48
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
import com.huake.zhnews.bean.gank.Gank;
import com.huake.zhnews.ui.activity.PictureActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.huake.zhnews.R.id.iv_meizhi;

//这会比较简单，并没有去处理下拉刷新的情况
public class GankListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Gank> mdatas;

    public GankListAdapter(Context context, List<Gank> mdatas) {
        mContext = context;
        this.mdatas = mdatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = View.inflate(mContext, R.layout.item_gank_meizi, null);
        return new GanViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GanViewHolder){
            GanViewHolder ganViewHolder= (GanViewHolder) holder;
            Gank gank = mdatas.get(position);
            ganViewHolder.bindItem(gank);
        }
    }

    @Override
    public int getItemCount() {
        return mdatas.size();
    }

    class GanViewHolder extends RecyclerView.ViewHolder {
        @Bind(iv_meizhi)
        ImageView mIvMeizhi;
        @Bind(R.id.tv_meizhi_title)
        TextView mTvMeizhiTitle;
        @Bind(R.id.card_meizhi)
        CardView mCardMeizhi;

        GanViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        public  void bindItem(Gank gank){
            mTvMeizhiTitle.setText(gank.getDesc());
            Glide.with(mContext).load(gank.getUrl()).centerCrop().into(mIvMeizhi);
            mTvMeizhiTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mContext.startActivity(PictureActivity.newIntent(mContext,gank.getUrl(),gank.getDesc()));
                }
            });
            mIvMeizhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //正常启动一个活动,平移动画
                    mContext.startActivity(PictureActivity.newIntent(mContext,gank.getUrl(),gank.getDesc()));
//                    带有动画的启动 有bug,部分图片只是一个左上角的图片
//                    Intent intent = PictureActivity.newIntent(mContext, gank.getUrl(), gank.getDesc());
//                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,mIvMeizhi,PictureActivity.TRANSIT_PIC);
//                    ActivityCompat.startActivity((Activity) mContext,intent,optionsCompat.toBundle());

                }
            });
        }
    }
}
