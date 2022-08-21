package com.miui.gallery.model.datalayer.repository.album.rubbish.datasource;

import com.miui.gallery.model.datalayer.repository.album.IBaseDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IRubbishAlbumDataSource extends IBaseDataSource {
    Flowable<List<Album>> queryRubbishAlbum(Integer num);

    Flowable<List<BaseAlbumCover>> queryRubbishAlbumsAllPhoto(Integer num);
}
