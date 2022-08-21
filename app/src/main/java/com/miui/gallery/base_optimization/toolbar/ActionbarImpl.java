package com.miui.gallery.base_optimization.toolbar;

import miuix.appcompat.app.ActionBar;

/* loaded from: classes.dex */
public class ActionbarImpl implements IToolbar<ActionBar> {
    public ActionBar mActionbar;

    public ActionbarImpl(ActionBar actionBar) {
        this.mActionbar = actionBar;
    }

    @Override // com.miui.gallery.base_optimization.toolbar.IToolbar
    public void setTitle(int i) {
        this.mActionbar.setTitle(i);
    }
}
