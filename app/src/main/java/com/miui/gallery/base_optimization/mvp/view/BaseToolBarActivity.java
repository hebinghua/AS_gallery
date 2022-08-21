package com.miui.gallery.base_optimization.mvp.view;

import android.os.Bundle;
import com.miui.gallery.base_optimization.toolbar.IToolbar;
import com.miui.gallery.base_optimization.toolbar.ToolbarDelegate;

/* loaded from: classes.dex */
public abstract class BaseToolBarActivity<T> extends Activity implements IToolbar<T> {
    public ToolbarDelegate<T> mToolbarDelegate;

    public T getTopBar() {
        return null;
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ToolbarDelegate<T> toolbarDelegate = new ToolbarDelegate<>();
        this.mToolbarDelegate = toolbarDelegate;
        toolbarDelegate.onCreate(this, getTopBar() == null ? (T) getAppCompatActionBar() : getTopBar());
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    @Override // android.app.Activity, com.miui.gallery.base_optimization.toolbar.IToolbar
    public void setTitle(int i) {
        this.mToolbarDelegate.setTitle(i);
    }
}
