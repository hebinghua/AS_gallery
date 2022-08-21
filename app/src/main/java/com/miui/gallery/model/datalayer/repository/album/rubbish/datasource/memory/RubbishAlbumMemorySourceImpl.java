package com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.memory;

import android.content.Context;
import com.miui.gallery.dao.AlbumTableServices;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.ui.album.common.AlbumConstants;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class RubbishAlbumMemorySourceImpl implements IRubbishAlbumDataSource {
    public ICommonAlbumDataSource mCommonSourceDelegate;

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 1;
    }

    public RubbishAlbumMemorySourceImpl(Context context) {
        this.mCommonSourceDelegate = new CommonAlbumMemoryDataSourceImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource
    public Flowable<List<Album>> queryRubbishAlbum(Integer num) {
        return this.mCommonSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_RUBBISH_ALBUM_LIST, new QueryParam.Builder().limitByNum(num).build());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource
    public Flowable<List<BaseAlbumCover>> queryRubbishAlbumsAllPhoto(final Integer num) {
        return this.mCommonSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_RUBBISH_ALBUM_LIST, new QueryParam.Builder().columns(AlbumTableServices.ALBUM_ONLY_ID_MODE_PROJECTION).limitByNum(num).build()).flatMap(new Function<List<Album>, Publisher<List<BaseAlbumCover>>>() { // from class: com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.memory.RubbishAlbumMemorySourceImpl.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<List<BaseAlbumCover>> mo2564apply(List<Album> list) throws Exception {
                return RubbishAlbumMemorySourceImpl.this.mCommonSourceDelegate.queryRecentPhotosInAlbum(list, false, num.intValue(), -1);
            }
        });
    }
}
