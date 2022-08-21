package com.miui.gallery.model.datalayer.repository.album.other;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.datalayer.repository.GalleryDataRepositoryConfig$AlbumRepositoryConfig$OtherAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.OnDataSourceFinishEventConsumer;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.util.MediaAndAlbumOperations;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class OtherAlbumModelImpl implements IOtherAlbumModel {
    public final WeakReference<Context> mContext;
    public final IOtherAlbumDataSource[] mDataSources;

    public OtherAlbumModelImpl(Context context) {
        WeakReference<Context> weakReference = new WeakReference<>(context);
        this.mContext = weakReference;
        this.mDataSources = GalleryDataRepositoryConfig$AlbumRepositoryConfig$OtherAlbumModel.getDataSources(1, weakReference.get());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<PageResults<List<Album>>> queryOthersAlbum(final Integer num) {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(this.mDataSources).map(new Function<IOtherAlbumDataSource, Flowable<PageResults<List<Album>>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl.2
            @Override // java.util.function.Function
            public Flowable<PageResults<List<Album>>> apply(IOtherAlbumDataSource iOtherAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iOtherAlbumDataSource.getSourceType(), iOtherAlbumDataSource.queryOthersAlbum(num)).doAfterNext(new OnDataSourceFinishEventConsumer(1, OtherAlbumModelImpl.this.mDataSources));
            }
        }).toArray(new IntFunction<Flowable<PageResults<List<Album>>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl.1
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<List<Album>>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<PageResults<CoverList>> queryOtherAlbumCovers() {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(this.mDataSources).map(new Function<IOtherAlbumDataSource, Flowable<PageResults<CoverList>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl.4
            @Override // java.util.function.Function
            public Flowable<PageResults<CoverList>> apply(IOtherAlbumDataSource iOtherAlbumDataSource) {
                return PageResults.wrapperDataToPageResult(iOtherAlbumDataSource.getSourceType(), iOtherAlbumDataSource.queryOtherAlbumCovers()).doAfterNext(new OnDataSourceFinishEventConsumer(2, OtherAlbumModelImpl.this.mDataSources));
            }
        }).toArray(new IntFunction<Flowable<PageResults<CoverList>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl.3
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<CoverList>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInOtherAlbumPage(final boolean z, final long[] jArr) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1122call() throws Exception {
                return Boolean.valueOf(MediaAndAlbumOperations.doChangeShowInOtherAlbums(GalleryApp.sGetAndroidContext(), jArr, z, false));
            }
        });
    }
}
