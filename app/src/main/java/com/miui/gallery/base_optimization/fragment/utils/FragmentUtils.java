package com.miui.gallery.base_optimization.fragment.utils;

import android.app.Activity;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.base_optimization.fragment.FragmentAnimation;

/* loaded from: classes.dex */
public class FragmentUtils {
    public static void startFragment(int i, FragmentManager fragmentManager, Fragment fragment, FragmentAnimation fragmentAnimation, boolean z, boolean z2, String str) {
        if (fragmentManager != null && i > 0) {
            if (!TextUtils.isEmpty(str) && fragmentManager.findFragmentByTag(str) != null) {
                return;
            }
            FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
            if (z2) {
                beginTransaction.addToBackStack(str);
            }
            if (z && !fragment.isAdded()) {
                beginTransaction.add(i, fragment, str);
            } else {
                beginTransaction.replace(i, fragment, str);
            }
            beginTransaction.commitAllowingStateLoss();
        }
    }

    public static void addFragmentToActivityIfNeed(Activity activity, android.app.Fragment fragment, String str) {
        if (activity == null || fragment == null || str == null) {
            return;
        }
        activity.getFragmentManager().beginTransaction().add(fragment, str).commitAllowingStateLoss();
    }

    public static void addFragmentToActivityIfNeed(FragmentActivity fragmentActivity, Fragment fragment, String str) {
        if (fragmentActivity == null || fragment == null || str == null) {
            return;
        }
        fragmentActivity.getSupportFragmentManager().beginTransaction().add(fragment, str).commitAllowingStateLoss();
    }
}
