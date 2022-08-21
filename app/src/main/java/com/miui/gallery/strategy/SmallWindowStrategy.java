package com.miui.gallery.strategy;

import android.app.Activity;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SmallWindowStrategy implements IStrategy$IWindowSizeStrategy {
    @Override // com.miui.gallery.strategy.IStrategy$IWindowSizeStrategy
    public void setActionBarExpandState(Activity activity) {
        ActionBar appCompatActionBar;
        if (activity == null || !(activity instanceof AppCompatActivity) || (appCompatActionBar = ((AppCompatActivity) activity).getAppCompatActionBar()) == null) {
            return;
        }
        appCompatActionBar.setExpandState(0);
        appCompatActionBar.setResizable(false);
    }
}
