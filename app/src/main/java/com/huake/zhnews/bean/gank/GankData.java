package com.huake.zhnews.bean.gank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Werb on 2016/8/19.
 * Werb is Wanbo.
 * 分类型的gank，有点搞不懂，还有魅族跟video
 */
public class GankData {

    public Result results;
    public List<String> category;

    public class Result {
        public List<Gank> Android;
        public List<Gank> iOS;
        public List<Gank> restvideo;
        public List<Gank> web;
        public List<Gank> resourecekuo;
        public List<Gank> profice;
        public List<Gank> guest;
        //把数据添加到一起
        public List<Gank> getAllResults() {

            List<Gank> mGankList = new ArrayList<>();

            if (restvideo != null) mGankList.addAll(0,restvideo);
            if (web != null) mGankList.addAll(web);
            if (Android != null) mGankList.addAll(Android);
            if (iOS != null) mGankList.addAll(iOS);
            if (resourecekuo != null) mGankList.addAll(resourecekuo);
            if (guest != null) mGankList.addAll(guest);

            return mGankList;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "Android=" + Android +
                    ", iOS=" + iOS +
                    ", 休息视频=" + restvideo +
                    ", 前端=" + web +
                    ", 拓展资源=" + resourecekuo +
                    ", 福利=" + profice +
                    ", 瞎推荐=" + guest +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GankData{" +
                "results=" + results +
                ", category=" + category +
                '}';
    }
}
