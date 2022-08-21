package com.miui.gallery.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;

/* loaded from: classes2.dex */
public abstract class AntiDoubleItemClickListener implements ItemClickSupport.OnItemClickListener {
    public long mLastClickTimeMillis;
    public int mMinClickInterval;

    public abstract void onAntiDoubleItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2);

    public AntiDoubleItemClickListener() {
        this(UIMsg.MSG_MAP_PANO_DATA);
    }

    public AntiDoubleItemClickListener(int i) {
        this.mMinClickInterval = i;
    }

    @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
    public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        if (System.currentTimeMillis() - this.mLastClickTimeMillis < this.mMinClickInterval) {
            DefaultLogger.e("DoubleClick", "On filtered click event");
            return false;
        }
        this.mLastClickTimeMillis = System.currentTimeMillis();
        onAntiDoubleItemClick(recyclerView, view, i, j, f, f2);
        return true;
    }
}
