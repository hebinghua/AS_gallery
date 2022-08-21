package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class RecyclerViewUtils {
    public static void setChildVisibility(RecyclerView recyclerView, int i) {
        for (int i2 = 0; i2 < recyclerView.getChildCount(); i2++) {
            recyclerView.getChildAt(i2).setVisibility(i);
        }
    }
}
