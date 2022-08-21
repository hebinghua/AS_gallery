package com.miui.gallery.picker.albumdetail;

/* loaded from: classes2.dex */
public interface ISelectAllDecor {
    void deselectAll();

    boolean isAllSelected();

    boolean isNoneSelected();

    void selectAll();

    void setItemStateListener(ItemStateListener itemStateListener);
}
