package com.miui.gallery.activity;

import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.ui.AsyncViewPrefetcher;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;

/* compiled from: HomePageStartupHelper2.kt */
/* loaded from: classes.dex */
public final class HomePageStartupHelper2$attach$1$2 implements AsyncViewPrefetcher.IPrefetchSpecProvider {
    public static final HomePageStartupHelper2$attach$1$2 INSTANCE = new HomePageStartupHelper2$attach$1$2();

    @Override // com.miui.gallery.ui.AsyncViewPrefetcher.IPrefetchSpecProvider
    public final List<AsyncViewPrefetcher.PrefetchSpec> provide() {
        return CollectionsKt__CollectionsKt.listOf((Object[]) new AsyncViewPrefetcher.PrefetchSpec[]{new AsyncViewPrefetcher.PrefetchSpec(2, 1), new AsyncViewPrefetcher.PrefetchSpec(1, Config$ThumbConfig.get().sPredictItemsOneScreen), new AsyncViewPrefetcher.PrefetchSpec(2, Config$ThumbConfig.get().sPredictHeadersOneScreen - 1)});
    }
}
