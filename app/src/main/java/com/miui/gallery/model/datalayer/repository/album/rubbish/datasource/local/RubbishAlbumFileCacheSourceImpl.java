package com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.local;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource;
import com.miui.gallery.model.datalayer.utils.AlbumFileCache;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.ui.album.common.AlbumConstants;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public class RubbishAlbumFileCacheSourceImpl implements IRubbishAlbumDataSource {
    public final ICommonAlbumDataSource mFileCacheSourceDelegate = new CommonAlbumFileCacheDataSourceImpl();

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 4;
    }

    public RubbishAlbumFileCacheSourceImpl(Context context) {
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource
    public Flowable<List<Album>> queryRubbishAlbum(Integer num) {
        return this.mFileCacheSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_RUBBISH_ALBUM_LIST, new QueryParam.Builder().limitByNum(num).build());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource
    public Flowable<List<BaseAlbumCover>> queryRubbishAlbumsAllPhoto(Integer num) {
        List list = (List) AlbumFileCache.getInstance().getCache(AlbumFileCache.AlbumCacheType.COMMON, "rubbish_album_cover_key", new TypeToken<List<BaseAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.local.RubbishAlbumFileCacheSourceImpl.1
        }.getType());
        if (list != null) {
            return Flowable.just(list);
        }
        return Flowable.empty();
    }
}
