package com.huake.zhnews.api;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 15:08
 * @描述	      
 */

public class ApiFactory {

    //新的单例模式写法，靠一个静态类来位置单例
    protected  static final Object monitor=new Object();
    static ZhihuApi zhihuApiSingleton = null;
    static GankApi gankApiSingleton = null;
    static DailyApi dailyApiSingleton = null;
    //return Singleton
    //return Singleton
    public static ZhihuApi getZhihuApiSingleton() {
        synchronized (monitor) {
            if (zhihuApiSingleton == null) {
                zhihuApiSingleton = new ApiRetrofit().getZhihuApiService();
            }
            return zhihuApiSingleton;
        }
    }

    public static GankApi getGankApiSingleton() {
        synchronized (monitor) {
            if (gankApiSingleton == null) {
                gankApiSingleton = new ApiRetrofit().getGankApiService();
            }
            return gankApiSingleton;
        }
    }

    public static DailyApi getDailyApiSingleton() {
        synchronized (monitor) {
            if (dailyApiSingleton == null) {
                dailyApiSingleton = new ApiRetrofit().getDailyApiService();
            }
            return dailyApiSingleton;
        }
    }
}
