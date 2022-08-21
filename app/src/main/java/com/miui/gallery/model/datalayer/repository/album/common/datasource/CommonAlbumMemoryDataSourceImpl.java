package com.miui.gallery.model.datalayer.repository.album.common.datasource;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.datalayer.repository.album.common.AlbumLoaderFlowableOnSubscribe;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.GalleryContract;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class CommonAlbumMemoryDataSourceImpl implements ICommonAlbumDataSource {
    public final WeakReference<Context> mContext;

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 1;
    }

    public CommonAlbumMemoryDataSourceImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<List<Album>> queryAlbums(long j, QueryParam queryParam) {
        return Flowable.create(new AlbumLoaderFlowableOnSubscribe(this.mContext.get(), GalleryContract.Album.URI_CACHE, queryParam, j), BackpressureStrategy.LATEST);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<String> queryAlbumNameById(final long j) {
        return Flowable.fromCallable(new Callable<String>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl.1
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                return AlbumDataHelper.queryAlbumNameByAlbumId(GalleryContract.Album.URI_CACHE, GalleryApp.sGetAndroidContext(), j);
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<List<BaseAlbumCover>> queryRecentPhotosInAlbum(final List<Album> list, final boolean z, final int i, final int i2) {
        return Flowable.fromCallable(new Callable<List<BaseAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl.3
            @Override // java.util.concurrent.Callable
            public List<BaseAlbumCover> call() throws Exception {
                return AlbumDataSourceHelper.getInstance().queryRecentPhotosInAlbum((Context) CommonAlbumMemoryDataSourceImpl.this.mContext.get(), GalleryContract.Media.URI, list, z, i, i2);
            }
        });
    }
}
