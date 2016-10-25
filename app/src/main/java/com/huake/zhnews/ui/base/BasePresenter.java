package com.huake.zhnews.ui.base;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 15:15
 * @描述	      
 */


import com.huake.zhnews.api.ApiFactory;
import com.huake.zhnews.api.DailyApi;
import com.huake.zhnews.api.GankApi;
import com.huake.zhnews.api.ZhihuApi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
//就干了一件事，推送者关联view
public abstract class BasePresenter<V> {

    protected Reference<V> mViewRef;
    public static final ZhihuApi zhihuApi = ApiFactory.getZhihuApiSingleton();
    public static final GankApi gankApi = ApiFactory.getGankApiSingleton();
    public static final DailyApi dailyApi = ApiFactory.getDailyApiSingleton();


    //通过虚引用关联推送者和view
    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
    protected V getView() {
        return mViewRef.get();
    }

    public boolean isViewAttached(){
        return mViewRef!=null&&mViewRef.get()!=null;
    }

}
