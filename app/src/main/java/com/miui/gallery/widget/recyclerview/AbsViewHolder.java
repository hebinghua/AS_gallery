package com.miui.gallery.widget.recyclerview;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import java.util.List;

/* loaded from: classes3.dex */
public abstract class AbsViewHolder extends LifecycleAwareViewHolder {
    public abstract void bindData(int i, int i2, List<Object> list);

    public AbsViewHolder(View view, Lifecycle lifecycle) {
        super(view, lifecycle);
    }

    public void recycle() {
        onRecycle();
    }
}
