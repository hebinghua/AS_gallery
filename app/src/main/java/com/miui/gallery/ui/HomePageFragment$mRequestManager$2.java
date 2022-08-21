package com.miui.gallery.ui;

import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.glide.GlideRequests;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$mRequestManager$2 extends Lambda implements Function0<GlideRequests> {
    public final /* synthetic */ HomePageFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$mRequestManager$2(HomePageFragment homePageFragment) {
        super(0);
        this.this$0 = homePageFragment;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final GlideRequests mo1738invoke() {
        GlideRequests with = GlideApp.with(this.this$0);
        Intrinsics.checkNotNullExpressionValue(with, "with(this)");
        return with;
    }
}
