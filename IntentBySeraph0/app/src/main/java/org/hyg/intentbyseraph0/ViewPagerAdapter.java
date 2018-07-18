package org.hyg.intentbyseraph0;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by shiny on 2018-03-14.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int m_cntTabs;

    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.m_cntTabs = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frmSelected = null;
        switch(position){
            case 0:
                frmSelected = new Tab1Fragment();
                break;

            case 1:
                frmSelected = new Tab2Fragment();
                break;

            case 2:
                frmSelected = new Tab3Fragment();
                break;
        }

        return frmSelected;
    }

    @Override
    public int getCount() {
        return m_cntTabs;
    }

}
