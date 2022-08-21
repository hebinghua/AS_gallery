package com.miui.gallery.adapter;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: CursorMediaAdapter.kt */
/* loaded from: classes.dex */
public final class CursorMediaAdapter$adapterDelegate$2 extends Lambda implements Function0<CursorAdapterDelegate> {
    public final /* synthetic */ CursorMediaAdapter this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CursorMediaAdapter$adapterDelegate$2(CursorMediaAdapter cursorMediaAdapter) {
        super(0);
        this.this$0 = cursorMediaAdapter;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CursorAdapterDelegate mo1738invoke() {
        return new CursorAdapterDelegate(this.this$0);
    }
}
