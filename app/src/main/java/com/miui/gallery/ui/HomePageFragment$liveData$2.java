package com.miui.gallery.ui;

import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$liveData$2 extends Lambda implements Function0<CacheLiveData<MediaCacheItem, IRecord>> {
    public final /* synthetic */ HomePageFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$liveData$2(HomePageFragment homePageFragment) {
        super(0);
        this.this$0 = homePageFragment;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CacheLiveData<MediaCacheItem, IRecord> mo1738invoke() {
        HomePageViewModel viewModel;
        viewModel = this.this$0.getViewModel();
        return viewModel.getLiveData();
    }
}
