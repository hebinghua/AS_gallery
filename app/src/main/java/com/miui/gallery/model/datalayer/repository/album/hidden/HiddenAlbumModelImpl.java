package com.miui.gallery.model.datalayer.repository.album.hidden;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.util.MediaAndAlbumOperations;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class HiddenAlbumModelImpl implements IHiddenAlbumModel {
    public final ICommonAlbumDataSource mCommonAlbumMemoryDataSource;
    public final WeakReference<Context> mContext;

    public HiddenAlbumModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mCommonAlbumMemoryDataSource = new CommonAlbumMemoryDataSourceImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<PageResults<List<Album>>> queryHiddenAlbum() {
        return PageResults.wrapperDataToPageResult(1, this.mCommonAlbumMemoryDataSource.queryAlbums(AlbumConstants.QueryScene.SCENE_HIDDEN_ALBUM_LIST));
    }

    public Flowable<Boolean> doChangeAlbumHiddenStatus(final boolean z, final long j) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.hidden.HiddenAlbumModelImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1120call() throws Exception {
                return Boolean.valueOf(AlbumTableServices.changeAlbumHiddenStatus((Context) HiddenAlbumModelImpl.this.mContext.get(), j, z, false));
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<Boolean> doChangeAlbumHiddenStatus(final boolean z, final long[] jArr) {
        return Flowable.fromCallable(new Callable<Boolean>() { // from class: com.miui.gallery.model.datalayer.repository.album.hidden.HiddenAlbumModelImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Boolean mo1121call() throws Exception {
                MediaAndAlbumOperations.doChangeHiddenStatus(GalleryApp.sGetAndroidContext(), jArr, z, false);
                return Boolean.TRUE;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<Boolean> cancelAlbumHiddenStatus(long j) {
        return doChangeAlbumHiddenStatus(false, j);
    }
}
