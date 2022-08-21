package com.miui.gallery.adapter;

import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.widget.recyclerview.AsyncDifferConfig;
import com.miui.gallery.widget.recyclerview.AsyncListDiffer;
import com.miui.gallery.widget.recyclerview.DiffUtil;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ListAdapterDelegate.kt */
/* loaded from: classes.dex */
public final class ListAdapterDelegate$differ$2 extends Lambda implements Function0<AsyncListDiffer<IRecord>> {
    public final /* synthetic */ ListAdapterDelegate this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ListAdapterDelegate$differ$2(ListAdapterDelegate listAdapterDelegate) {
        super(0);
        this.this$0 = listAdapterDelegate;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final AsyncListDiffer<IRecord> mo1738invoke() {
        DiffUtil.ItemCallback itemCallback;
        ListAdapterDelegate listAdapterDelegate = this.this$0;
        itemCallback = this.this$0.diffCallback;
        return new AsyncListDiffer<>(listAdapterDelegate, new AsyncDifferConfig.Builder(itemCallback).build());
    }
}
