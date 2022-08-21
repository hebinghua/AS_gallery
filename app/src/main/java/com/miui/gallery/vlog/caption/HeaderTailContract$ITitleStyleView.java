package com.miui.gallery.vlog.caption;

import com.miui.gallery.vlog.entity.HeaderTailData;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface HeaderTailContract$ITitleStyleView {
    void loadRecyclerView(ArrayList<HeaderTailData> arrayList);

    void onRemoveHeadTail();

    void updatePlayViewState(boolean z);

    void updateSelectItem();
}
