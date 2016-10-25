package com.huake.zhnews.ui.adapter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/22 16:59
 * @描述	      
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huake.zhnews.ui.base.MVPBaseFragment;

import java.util.List;

public class ViewPagerFgAdapter extends FragmentPagerAdapter {

    private List<MVPBaseFragment> fragmentList;

    public ViewPagerFgAdapter(FragmentManager fm, List<MVPBaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "推荐";
            case 1:
                return "美女";
            case 2:
                return "科技";
        }
        return null;
    }
}
