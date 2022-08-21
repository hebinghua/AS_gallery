package com.miui.gallery.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.CacheLiveDataKt;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaProcessor;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PhotoListViewModel.kt */
/* loaded from: classes2.dex */
public final class PhotoListViewModel extends AndroidViewModel {
    public final CacheLiveData<MediaCacheItem, IRecord> liveData;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PhotoListViewModel(Application application) {
        super(application);
        Intrinsics.checkNotNullParameter(application, "application");
        this.liveData = CacheLiveDataKt.cacheLiveData$default(this, application, null, null, null, null, null, null, new MediaProcessor(true), 126, null);
    }

    public final CacheLiveData<MediaCacheItem, IRecord> getLiveData() {
        return this.liveData;
    }
}
