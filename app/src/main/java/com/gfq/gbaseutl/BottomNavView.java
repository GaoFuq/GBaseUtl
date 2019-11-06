package com.gfq.gbaseutl;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

/**
 * create by 高富强
 * on {2019/11/6} {13:14}
 * desctapion:
 */
public class BottomNavView {
    //通过反射，解决当tab个数大余3个时，BottomNavigationView不会均分宽度，一般来说我们都是需要均分宽度。
//    public void disableShiftMode(BottomNavigationView navigationView) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
//                itemView.setShiftingMode(false);
//                itemView.setChecked(itemView.getItemData().isChecked());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
