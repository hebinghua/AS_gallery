package com.miui.gallery.ui;

import android.net.Uri;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: ModernAlbumDetailFragment.kt */
/* loaded from: classes2.dex */
public final class ModernAlbumDetailFragment$liveData$2 extends Lambda implements Function0<CacheLiveData<MediaCacheItem, IRecord>> {
    public final /* synthetic */ ModernAlbumDetailFragment this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ModernAlbumDetailFragment$liveData$2(ModernAlbumDetailFragment modernAlbumDetailFragment) {
        super(0);
        this.this$0 = modernAlbumDetailFragment;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final CacheLiveData<MediaCacheItem, IRecord> mo1738invoke() {
        PhotoListViewModel viewModel;
        viewModel = this.this$0.getViewModel();
        CacheLiveData<MediaCacheItem, IRecord> liveData = viewModel.getLiveData();
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.this$0;
        Uri uri = modernAlbumDetailFragment.getUri();
        Intrinsics.checkNotNullExpressionValue(uri, "uri");
        CacheLiveData.updateQueryArgs$default(liveData, uri, modernAlbumDetailFragment.getSelection(), modernAlbumDetailFragment.getSelectionArgs(), modernAlbumDetailFragment.getCurrentSortOrder(), false, 16, null);
        return liveData;
    }
}
