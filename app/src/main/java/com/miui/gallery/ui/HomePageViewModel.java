package com.miui.gallery.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.miui.gallery.loader.HomeMediaLoader;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.CacheLiveDataKt;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaProcessor;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: HomePageViewModel.kt */
/* loaded from: classes2.dex */
public final class HomePageViewModel extends AndroidViewModel {
    public final CacheLiveData<MediaCacheItem, IRecord> liveData;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageViewModel(Application application) {
        super(application);
        Intrinsics.checkNotNullParameter(application, "application");
        this.liveData = CacheLiveDataKt.cacheLiveData$default(this, application, HomeMediaLoader.URI, null, HomeMediaLoader.getHomePageSelection(GalleryPreferences.HomePage.isHomePageShowAllPhotos()), null, "alias_sort_time DESC ", null, new MediaProcessor(true), 84, null);
    }

    public final CacheLiveData<MediaCacheItem, IRecord> getLiveData() {
        return this.liveData;
    }
}
