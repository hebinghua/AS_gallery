package com.miui.gallery.model.datalayer.repository.album.common;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.GalleryDataRepositoryConfig$AlbumRepositoryConfig$CommonAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.util.MediaAndAlbumOperations;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class CommonAlbumModelImpl implements ICommonAlbumModel {
    public WeakReference<Context> mContext;

    public CommonAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<PageResults<List<Album>>> queryAlbums(final long j, final QueryParam queryParam) {
        ICommonAlbumDataSource[] dataSources;
        if (queryParam != null && queryParam.getExtra() != null && queryParam.getExtra().containsKey("query_snap_source_key")) {
            dataSources = GalleryDataRepositoryConfig$AlbumRepositoryConfig$CommonAlbumModel.getSnapDataSources(this.mContext.get());
        } else {
            dataSources = GalleryDataRepositoryConfig$AlbumRepositoryConfig$CommonAlbumModel.getDataSources(this.mContext.get());
        }
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(dataSources).map(new Function<ICommonAlbumDataSource, Flowable<PageResults<List<Album>>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.2
            @Override // java.util.function.Function
            public Flowable<PageResults<List<Album>>> apply(ICommonAlbumDataSource iCommonAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iCommonAlbumDataSource.getSourceType(), iCommonAlbumDataSource.queryAlbums(j, queryParam));
            }
        }).toArray(new IntFunction<Flowable<PageResults<List<Album>>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.1
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<List<Album>>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<PageResults<String>> queryAlbumName(final long j) {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(GalleryDataRepositoryConfig$AlbumRepositoryConfig$CommonAlbumModel.getDataSources(this.mContext.get())).map(new Function<ICommonAlbumDataSource, Flowable<PageResults<String>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.4
            @Override // java.util.function.Function
            public Flowable<PageResults<String>> apply(ICommonAlbumDataSource iCommonAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iCommonAlbumDataSource.getSourceType(), iCommonAlbumDataSource.queryAlbumNameById(j));
            }
        }).toArray(new IntFunction<Flowable<PageResults<String>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.3
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<String>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInPhotoTabPage(final boolean z, final long j) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1118call() throws Exception {
                MediaAndAlbumOperations.doChangeShowInPhotosTab((Context) CommonAlbumModelImpl.this.mContext.get(), j, z, false);
                return Boolean.TRUE;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<Boolean> doChangeAlbumSortPosition(final long[] jArr, final String[] strArr) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1119call() throws Exception {
                MediaAndAlbumOperations.doChangeAlbumSortPosition((Context) CommonAlbumModelImpl.this.mContext.get(), jArr, strArr, false);
                return Boolean.TRUE;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> doReplaceAlbumCover(final long j, final long[] jArr) {
        return Flowable.fromCallable(new Callable<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl.9
            @Override // java.util.concurrent.Callable
            public ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> call() throws Exception {
                return MediaAndAlbumOperations.doReplaceAlbumCover((Context) CommonAlbumModelImpl.this.mContext.get(), j, jArr);
            }
        });
    }
}
