package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 16:03
 * @描述	      
 */

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.huake.zhnews.MainActivity;
import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.base.MVPBaseActivity;

import butterknife.Bind;


public class SplashActivity extends MVPBaseActivity {


    private static final String TAG = "SplashActivity";


    @Bind(R.id.tv_splash_info)
    TextView mTvSplashInfo;

    private Handler mHandler = new Handler();



    @Override
    protected void onStart() {
        super.onStart();
        AssetManager mgr = getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/rm_albion.ttf");//根据路径得到Typeface
        mTvSplashInfo.setTypeface(tf);//设置字体
        startLoadingData();
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_splash;
    }

    private void startLoadingData() {
        // finish "loading data" in a random time between 1 and 3 seconds
        mHandler.postDelayed(this::goToMain, 1000);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
