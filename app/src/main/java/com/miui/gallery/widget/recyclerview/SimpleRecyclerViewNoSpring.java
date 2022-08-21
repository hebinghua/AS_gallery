package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes3.dex */
public class SimpleRecyclerViewNoSpring extends SimpleLinearRecyclerView {
    public SimpleRecyclerViewNoSpring(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SimpleRecyclerViewNoSpring(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setAlwaysDisableSpring(true);
    }
}
