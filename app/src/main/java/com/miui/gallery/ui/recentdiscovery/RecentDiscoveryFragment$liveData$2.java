package com.miui.gallery.ui.recentdiscovery;

import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.ui.PhotoListViewModel;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: RecentDiscoveryFragment.kt */
/* loaded from: classes2.dex */
public final class RecentDiscoveryFragment$liveData$2 extends Lambda implements Function0<CacheLiveData<MediaCacheItem, IRecord>> {
    public final /* synthetic */ RecentDiscoveryFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public RecentDiscoveryFragment$liveData$2(RecentDiscoveryFragment recentDiscoveryFragment) {
        super(0);
        this.this$0 = recentDiscoveryFragment;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CacheLiveData<MediaCacheItem, IRecord> mo1738invoke() {
        PhotoListViewModel viewModel;
        viewModel = this.this$0.getViewModel();
        CacheLiveData<MediaCacheItem, IRecord> liveData = viewModel.getLiveData();
        RecentDiscoveryFragment recentDiscoveryFragment = this.this$0;
        CacheLiveData.updateQueryArgs$default(liveData, recentDiscoveryFragment.getUri(), recentDiscoveryFragment.getSelection(), recentDiscoveryFragment.getSelectionArgs(), recentDiscoveryFragment.getSortOrder(), false, 16, null);
        return liveData;
    }
}
