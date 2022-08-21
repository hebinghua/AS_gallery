package com.miui.gallery.vlog.filter;

import com.miui.gallery.vlog.entity.FilterData;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface FilterMenuContract$IFilterMenuView {
    void loadRecyclerView(ArrayList<FilterData> arrayList);

    void updatePlayViewState(boolean z);

    void updateSelectItem();
}
