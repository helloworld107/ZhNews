package com.huake.zhnews.ui.view;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/23 16:02
 * @描述	      
 */

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public interface IGankFgView {

    void setDataRefreshIs(boolean b);
    RecyclerView getRecyclerview();
    GridLayoutManager getGridLayoutManager();
}
