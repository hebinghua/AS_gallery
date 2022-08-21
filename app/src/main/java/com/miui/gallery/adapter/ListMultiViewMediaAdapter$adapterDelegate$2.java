package com.miui.gallery.adapter;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ListMultiViewMediaAdapter.kt */
/* loaded from: classes.dex */
public final class ListMultiViewMediaAdapter$adapterDelegate$2 extends Lambda implements Function0<ListAdapterDelegate> {
    public final /* synthetic */ ListMultiViewMediaAdapter<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ListMultiViewMediaAdapter$adapterDelegate$2(ListMultiViewMediaAdapter<T> listMultiViewMediaAdapter) {
        super(0);
        this.this$0 = listMultiViewMediaAdapter;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final ListAdapterDelegate mo1738invoke() {
        ListAdapterDelegate listAdapterDelegate = new ListAdapterDelegate(this.this$0);
        listAdapterDelegate.getDiffer().addListListener(this.this$0);
        return listAdapterDelegate;
    }
}
