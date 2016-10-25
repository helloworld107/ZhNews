package com.huake.zhnews.ui.view;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 9:01
 * @描述	      
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public interface IZhihuFgView {

    void setDataRefreshIs(boolean b);
    RecyclerView getRecyclerview();
    LinearLayoutManager getLinearLayoutManager();
}
