package com.miui.gallery.adapter;

import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

/* compiled from: IListAdapter.kt */
/* loaded from: classes.dex */
public interface IListAdapter<T> {
    Object submitList(List<? extends T> list, boolean z, Runnable runnable, Continuation<? super Unit> continuation);

    static /* synthetic */ Object submitList$default(IListAdapter iListAdapter, List list, boolean z, Runnable runnable, Continuation continuation, int i, Object obj) {
        if (obj == null) {
            if ((i & 2) != 0) {
                z = true;
            }
            if ((i & 4) != 0) {
                runnable = null;
            }
            return iListAdapter.submitList(list, z, runnable, continuation);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: submitList");
    }
}
