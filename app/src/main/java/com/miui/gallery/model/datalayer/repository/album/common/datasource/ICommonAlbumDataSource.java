package com.miui.gallery.model.datalayer.repository.album.common.datasource;

import com.miui.gallery.model.datalayer.repository.album.IBaseDataSource;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface ICommonAlbumDataSource extends IBaseDataSource {
    Flowable<String> queryAlbumNameById(long j);

    Flowable<List<Album>> queryAlbums(long j, QueryParam queryParam);

    default Flowable<List<Album>> queryAlbums(long j) {
        return queryAlbums(j, new QueryParam.Builder().build());
    }

    default Flowable<List<BaseAlbumCover>> queryRecentPhotosInAlbum(List<Album> list, boolean z, int i, int i2) {
        return Flowable.empty();
    }
}
