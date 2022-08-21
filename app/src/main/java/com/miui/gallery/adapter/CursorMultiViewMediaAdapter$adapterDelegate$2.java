package com.miui.gallery.adapter;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: CursorMultiViewMediaAdapter.kt */
/* loaded from: classes.dex */
public final class CursorMultiViewMediaAdapter$adapterDelegate$2 extends Lambda implements Function0<CursorAdapterDelegate> {
    public final /* synthetic */ CursorMultiViewMediaAdapter this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CursorMultiViewMediaAdapter$adapterDelegate$2(CursorMultiViewMediaAdapter cursorMultiViewMediaAdapter) {
        super(0);
        this.this$0 = cursorMultiViewMediaAdapter;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CursorAdapterDelegate mo1738invoke() {
        return new CursorAdapterDelegate(this.this$0);
    }
}
