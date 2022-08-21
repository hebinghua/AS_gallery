package com.miui.gallery.ui.album.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.stat.SamplingStatHelper;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class HoldInconsistencyGridLayoutManager extends GridLayoutManager {
    public HoldInconsistencyGridLayoutManager(Context context, int i) {
        super(context, i);
    }

    public HoldInconsistencyGridLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException unused) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("error", "HoldInconsistencyGridLayoutManager: error onLayoutChildren() IndexOutOfBoundsException");
            SamplingStatHelper.recordCountEvent("error_track", "album_refresh", hashMap);
            Log.e("HoldInconsistencyGridLayoutManager", "error onLayoutChildren() IndexOutOfBoundsException");
        }
    }
}
