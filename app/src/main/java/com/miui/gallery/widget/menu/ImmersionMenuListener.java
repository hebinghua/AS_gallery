package com.miui.gallery.widget.menu;

/* loaded from: classes2.dex */
public interface ImmersionMenuListener {
    void onCreateImmersionMenu(ImmersionMenu immersionMenu);

    void onImmersionMenuSelected(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem);

    boolean onPrepareImmersionMenu(ImmersionMenu immersionMenu);
}
