package com.miui.gallery.ui.share;

import com.miui.gallery.ui.share.PrepareItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface OnPrepareListener<T extends PrepareItem> {
    void onPrepareComplete(ArrayList<T> arrayList);
}
