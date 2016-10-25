package com.huake.zhnews.api;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 14:53
 * @描述	      
 */


import com.huake.zhnews.MyApp;
import com.huake.zhnews.util.StateUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRetrofit {

    //三个基本网址
    public static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/4/";
    public static final String GANK_BASE_URL = "http://gank.io/api/";
    public static final String DAILY_BASE_URL = "http://app3.qdaily.com/app3/";
    //三个访问类
    public ZhihuApi ZhihuApiService;
    public GankApi GankApiService;
    public DailyApi DailyApiService;

    public ZhihuApi getZhihuApiService() {
        return ZhihuApiService;
    }

    public GankApi getGankApiService() {
        return GankApiService;
    }

    public DailyApi getDailyApiService() {
        return DailyApiService;
    }

    //饿汉模式，构造方法的时候就把类都创建好了
    ApiRetrofit() {
        //搞一个自己的缓存
        File httpCacheDirectory = new File(MyApp.mContext.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory,cacheSize);

        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).
                cache(cache).build();

        Retrofit retrofit_zhihu = new Retrofit.Builder()
                .baseUrl(ZHIHU_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Retrofit retrofit_gank = new Retrofit.Builder()
                .baseUrl(GANK_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Retrofit retrofit_daily= new Retrofit.Builder()
                .baseUrl(DAILY_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ZhihuApiService=retrofit_zhihu.create(ZhihuApi.class);
        GankApiService=retrofit_gank.create(GankApi.class);
        DailyApiService=retrofit_daily.create(DailyApi.class);


    }
    //cache
    Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {

        //缓存
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);//max-age指示客户机可以接收生存期不大于指定时间（以秒为单位）的响应,不能超出
        cacheBuilder.maxStale(365, TimeUnit.DAYS);//一年内
        CacheControl cacheControl = cacheBuilder.build();

        Request request = chain.request();
        //如果没有网络看缓存的请求？
        if (!StateUtils.isNetworkAvailable(MyApp.mContext)) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();

        }

        Response originalResponse = chain.proceed(request);
        //处理了一下相应的缓存参数，服务器的数据，不懂啊
        if (StateUtils.isNetworkAvailable(MyApp.mContext)) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };
}
