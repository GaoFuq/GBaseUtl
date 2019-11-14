package com.gfq.gbaseutl.examples;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 作者：高富强
 * 日期：2019/8/16 16:57
 * 描述：
 */
public class TabEntity implements CustomTabEntity {
    public String title;
    private int selectedIcon;
    private int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}