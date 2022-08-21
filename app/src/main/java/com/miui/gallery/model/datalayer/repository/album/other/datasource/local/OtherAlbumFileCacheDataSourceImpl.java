package com.miui.gallery.model.datalayer.repository.album.other.datasource.local;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource;
import com.miui.gallery.model.datalayer.utils.AlbumFileCache;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.album.common.AlbumConstants;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public class OtherAlbumFileCacheDataSourceImpl implements IOtherAlbumDataSource {
    public final ICommonAlbumDataSource mFileCacheSourceDelegate = new CommonAlbumFileCacheDataSourceImpl();

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 4;
    }

    public OtherAlbumFileCacheDataSourceImpl(Context context) {
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource
    public Flowable<List<Album>> queryOthersAlbum(Integer num) {
        return this.mFileCacheSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_OTHER_ALBUM_LIST, new QueryParam.Builder().limitByNum(num).build());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource
    public Flowable<CoverList> queryOtherAlbumCovers() {
        CoverList coverList = (CoverList) AlbumFileCache.getInstance().getCache(AlbumFileCache.AlbumCacheType.COMMON, "other_album_cover_key", new TypeToken<CoverList<BaseAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.datasource.local.OtherAlbumFileCacheDataSourceImpl.1
        }.getType());
        if (coverList != null) {
            return Flowable.just(coverList);
        }
        return Flowable.empty();
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource
    public <T> void onEventFinish(int i, T t) {
        if (i == 2) {
            PageResults pageResults = (PageResults) t;
            if (pageResults.isFromFile()) {
                return;
            }
            AlbumFileCache.getInstance().saveCache(AlbumFileCache.AlbumCacheType.COMMON, "other_album_cover_key", pageResults.getResult());
        }
    }
}
