package com.miui.gallery.biz.albumpermission;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.miui.gallery.arch.livedata.ComputableLiveData;
import com.miui.gallery.arch.viewmodel.BaseViewModel;
import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.AlbumCacheItem;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: AlbumPermissionViewModel.kt */
/* loaded from: classes.dex */
public final class AlbumPermissionViewModel extends BaseViewModel {
    public static final Companion Companion = new Companion(null);
    public static final String[] projection;
    public static final String selection;
    public static final Uri uri;
    public final AlbumPermissionViewModel$_albums$1 _albums;
    public final LiveData<List<PermissionAlbum>> albums;
    public final Context context;
    public final CoroutineScope mainScope;

    /* compiled from: AlbumPermissionViewModel.kt */
    /* loaded from: classes.dex */
    public interface AssistedVMFactory {
        AlbumPermissionViewModel create();
    }

    /* JADX WARN: Type inference failed for: r9v2, types: [androidx.lifecycle.LiveData, com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel$_albums$1] */
    public AlbumPermissionViewModel(final Context context, CoroutineScope mainScope) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mainScope, "mainScope");
        this.context = context;
        this.mainScope = mainScope;
        Companion companion = Companion;
        final Uri uri2 = companion.getUri();
        final String[] projection2 = companion.getProjection();
        final String selection2 = companion.getSelection();
        final Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
        final PermissionAlbumProcessor permissionAlbumProcessor = new PermissionAlbumProcessor();
        ?? r9 = new CacheLiveData<AlbumCacheItem, PermissionAlbum>(context, uri2, projection2, selection2, executor, permissionAlbumProcessor) { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel$_albums$1
            {
                Intrinsics.checkNotNullExpressionValue(executor, "THREAD_POOL_EXECUTOR");
            }

            @Override // com.miui.gallery.provider.cache.CacheLiveData
            public void unregisterContentObserver() {
                super.unregisterContentObserver();
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                ComputableLiveData.SelfContentObserver contentObserver = getContentObserver();
                Intrinsics.checkNotNull(contentObserver);
                storageStrategyManager.unregisterPermissionObserver(contentObserver);
            }

            @Override // com.miui.gallery.provider.cache.CacheLiveData
            public void registerContentObserver(List<? extends Uri> list) {
                super.registerContentObserver(list);
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                ComputableLiveData.SelfContentObserver contentObserver = getContentObserver();
                Intrinsics.checkNotNull(contentObserver);
                storageStrategyManager.registerPermissionObserver(contentObserver);
            }
        };
        this._albums = r9;
        LiveData<List<PermissionAlbum>> map = Transformations.map(r9, new Function() { // from class: com.miui.gallery.biz.albumpermission.AlbumPermissionViewModel$special$$inlined$map$1
            @Override // androidx.arch.core.util.Function
            public final List<? extends PermissionAlbum> apply(List<? extends PermissionAlbum> list) {
                List<? extends PermissionAlbum> list2 = list;
                return list2 == null || list2.isEmpty() ? CollectionsKt__CollectionsKt.emptyList() : list2;
            }
        });
        Intrinsics.checkNotNullExpressionValue(map, "crossinline transform: (…p(this) { transform(it) }");
        this.albums = map;
    }

    public final LiveData<List<PermissionAlbum>> getAlbums() {
        return this.albums;
    }

    /* compiled from: AlbumPermissionViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final Uri getUri() {
            return AlbumPermissionViewModel.uri;
        }

        public final String[] getProjection() {
            return AlbumPermissionViewModel.projection;
        }

        public final String getSelection() {
            return AlbumPermissionViewModel.selection;
        }
    }

    static {
        Uri URI_CACHE = GalleryContract.Album.URI_CACHE;
        Intrinsics.checkNotNullExpressionValue(URI_CACHE, "URI_CACHE");
        uri = URI_CACHE;
        projection = new String[]{"coverPath", "localPath"};
        selection = "_id NOT IN (" + ((Object) TextUtils.join(", ", GalleryContract.Album.ALL_VIRTUAL_ALBUM_IDS)) + ") AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom'))) AND photoCount>0";
    }
}
