package com.miui.gallery.model.datalayer.repository.album.other.datasource.memory;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.ui.album.common.AlbumConstants;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.LinkedList;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class OtherAlbumMemoryDataSourceImpl implements IOtherAlbumDataSource {
    public final ICommonAlbumDataSource mDataSourceDelegate;

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 1;
    }

    public OtherAlbumMemoryDataSourceImpl(Context context) {
        this.mDataSourceDelegate = new CommonAlbumMemoryDataSourceImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource
    public Flowable<List<Album>> queryOthersAlbum(Integer num) {
        return this.mDataSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_OTHER_ALBUM_LIST, new QueryParam.Builder().limitByNum(num).build());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource
    public Flowable<CoverList> queryOtherAlbumCovers() {
        return this.mDataSourceDelegate.queryAlbums(AlbumConstants.QueryScene.SCENE_OTHER_ALBUM_COVER, new QueryParam.Builder().columns(IOtherAlbumDataSource.QUERY_OTHER_ALBUM_COVER_PROJECT).build()).flatMap(new Function<List<Album>, Publisher<CoverList>>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.datasource.memory.OtherAlbumMemoryDataSourceImpl.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<CoverList> mo2564apply(List<Album> list) throws Exception {
                LinkedList linkedList = new LinkedList();
                final LinkedList linkedList2 = new LinkedList();
                for (Album album : list) {
                    if (album.isRubbishAlbum() && linkedList2.size() <= 4) {
                        linkedList2.add(album);
                    } else if (linkedList.size() <= 4) {
                        linkedList.add(album);
                    }
                }
                return OtherAlbumMemoryDataSourceImpl.this.mDataSourceDelegate.queryRecentPhotosInAlbum(linkedList, false, 4, -1).map(new Function<List<BaseAlbumCover>, CoverList>() { // from class: com.miui.gallery.model.datalayer.repository.album.other.datasource.memory.OtherAlbumMemoryDataSourceImpl.1.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public CoverList mo2564apply(List<BaseAlbumCover> list2) throws Exception {
                        if (list2.isEmpty() && !linkedList2.isEmpty()) {
                            list2 = new LinkedList<>();
                            for (Album album2 : linkedList2) {
                                BaseAlbumCover baseAlbumCover = new BaseAlbumCover();
                                baseAlbumCover.albumName = album2.getAlbumName();
                                list2.add(baseAlbumCover);
                            }
                        }
                        return new CoverList(list2);
                    }
                });
            }
        });
    }
}
