package com.huake.zhnews.api;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 14:37
 * @描述	      
 */


import com.huake.zhnews.bean.zhihu.News;
import com.huake.zhnews.bean.zhihu.NewsTimeLine;
import com.huake.zhnews.bean.zhihu.SplashImage;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ZhihuApi {

    @GET("start-image/1080*1920")
    Observable<SplashImage> getSplashImage();

    @GET("news/latest")
    Observable<NewsTimeLine> getLatestNews();

    @GET("news/before/{time}")
    Observable<NewsTimeLine> getBeforetNews(@Path("time") String time);

    @GET("news/{id}")
    Observable<News> getDetailNews(@Path("id") String id);

}
