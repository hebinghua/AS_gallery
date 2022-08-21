package com.miui.gallery.base_optimization.toolbar;

import android.annotation.TargetApi;
import android.widget.Toolbar;

@TargetApi(21)
/* loaded from: classes.dex */
public class ToolBarImpl implements IToolbar<Toolbar> {
    public Toolbar mToolbar;

    public ToolBarImpl(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }

    @Override // com.miui.gallery.base_optimization.toolbar.IToolbar
    public void setTitle(int i) {
        this.mToolbar.setTitle(i);
    }
}
