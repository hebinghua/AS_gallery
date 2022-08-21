package com.miui.gallery.base_optimization.toolbar;

import android.os.Build;
import android.widget.Toolbar;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class ToolbarDelegate<T> implements IToolbar<T> {
    public WeakReference<AppCompatActivity> mActivity;
    public IToolbar mToolbar;

    public void onCreate(AppCompatActivity appCompatActivity, T t) {
        this.mActivity = new WeakReference<>(appCompatActivity);
        if (Build.VERSION.SDK_INT >= 21 && (t instanceof Toolbar)) {
            Toolbar toolbar = (Toolbar) t;
            this.mToolbar = new ToolBarImpl(toolbar);
            appCompatActivity.setActionBar(toolbar);
            return;
        }
        this.mToolbar = new ActionbarImpl((ActionBar) t);
    }

    @Override // com.miui.gallery.base_optimization.toolbar.IToolbar
    public void setTitle(int i) {
        this.mToolbar.setTitle(i);
    }
}
