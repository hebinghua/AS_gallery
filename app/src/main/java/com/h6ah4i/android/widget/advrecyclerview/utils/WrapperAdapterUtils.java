package com.h6ah4i.android.widget.advrecyclerview.utils;

import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;

/* loaded from: classes.dex */
public class WrapperAdapterUtils {
    public static <T> T findWrappedAdapter(RecyclerView.Adapter adapter, Class<T> cls) {
        if (cls.isInstance(adapter)) {
            return cls.cast(adapter);
        }
        if (!(adapter instanceof SimpleWrapperAdapter)) {
            return null;
        }
        return (T) findWrappedAdapter(((SimpleWrapperAdapter) adapter).getWrappedAdapter(), cls);
    }
}
