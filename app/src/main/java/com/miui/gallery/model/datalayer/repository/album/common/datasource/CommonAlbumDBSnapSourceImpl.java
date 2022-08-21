package com.miui.gallery.model.datalayer.repository.album.common.datasource;

import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.dto.Album;
import io.reactivex.Flowable;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class CommonAlbumDBSnapSourceImpl implements ICommonAlbumDataSource {
    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 2;
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<List<Album>> queryAlbums(final long j, final QueryParam queryParam) {
        return Flowable.fromCallable(new Callable<List<Album>>() { // from class: com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumDBSnapSourceImpl.1
            @Override // java.util.concurrent.Callable
            public List<Album> call() throws Exception {
                return AlbumTableServices.queryAlbumSnapDatas(queryParam);
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource
    public Flowable<String> queryAlbumNameById(long j) {
        throw new IllegalStateException("the data source not support this method");
    }
}
