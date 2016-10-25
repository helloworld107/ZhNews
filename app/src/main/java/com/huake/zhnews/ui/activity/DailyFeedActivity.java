package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/25 13:20
 * @描述	      
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.MVPBaseActivity;
import com.huake.zhnews.ui.presenter.DailyFeedPresenter;
import com.huake.zhnews.ui.view.IDailyFeedView;

import butterknife.Bind;

import static com.huake.zhnews.R.id.feed_list;
import static com.huake.zhnews.R.id.iv_feed_img;
import static com.huake.zhnews.R.id.tv_feed_desc;
import static com.huake.zhnews.R.id.tv_feed_title;

public class DailyFeedActivity extends MVPBaseActivity<IDailyFeedView, DailyFeedPresenter> implements IDailyFeedView {

    private static final String FEED_ID = "feed_id";
    private static final String FEED_DESC = "feed_desc";
    private static final String FEED_TITLE = "feed_title";
    private static final String FEED_IMG = "feed_img";
    @Bind(iv_feed_img)
    ImageView mIvFeedImg;
    @Bind(tv_feed_title)
    TextView mTvFeedTitle;
    @Bind(tv_feed_desc)
    TextView mTvFeedDesc;
    @Bind(feed_list)
    RecyclerView mFeedList;

    private String id;
    private String desc;
    private String title;
    private String img;
    private GridLayoutManager mGridLayoutManager;


    @Override
    public DailyFeedPresenter createPresenter() {
        return new DailyFeedPresenter(this);
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_daily_feed;
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mFeedList;
    }

    @Override
    public GridLayoutManager getLayoutManager() {
        return mGridLayoutManager;
    }

    @Override
    public void initData() {
        super.initData();
        parseIntent();
        mTvFeedTitle.setText(title);
        mTvFeedDesc.setText(desc);
        Glide.with(this).load(img).centerCrop().into(mIvFeedImg);

        mGridLayoutManager = new GridLayoutManager(this,2);
        mFeedList.setLayoutManager(mGridLayoutManager);

        mPresenter.getDataFromNet(id,"0");
        mPresenter.scrollRecycleView();

    }

    public static Intent newIntent(Context context, String id, String desc, String title, String img) {
        Intent intent = new Intent(context, DailyFeedActivity.class);
        intent.putExtra(DailyFeedActivity.FEED_ID, id);
        intent.putExtra(DailyFeedActivity.FEED_DESC, desc);
        intent.putExtra(DailyFeedActivity.FEED_TITLE, title);
        intent.putExtra(DailyFeedActivity.FEED_IMG, img);
        return intent;
    }

    private void parseIntent() {
        id = getIntent().getStringExtra(FEED_ID);
        desc = getIntent().getStringExtra(FEED_DESC);
        title = getIntent().getStringExtra(FEED_TITLE);
        img = getIntent().getStringExtra(FEED_IMG);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(mIvFeedImg);
    }

    @Override
    public boolean canBack() {
        return true;
    }


}
