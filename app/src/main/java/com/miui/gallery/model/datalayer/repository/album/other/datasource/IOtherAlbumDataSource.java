package com.miui.gallery.model.datalayer.repository.album.other.datasource;

import com.miui.gallery.model.datalayer.repository.album.IBaseDataSource;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IOtherAlbumDataSource extends IBaseDataSource {
    public static final String[] QUERY_OTHER_ALBUM_COVER_PROJECT = {j.c, "name", "attributes"};

    default <T> void onEventFinish(int i, T t) {
    }

    Flowable<CoverList> queryOtherAlbumCovers();

    Flowable<List<Album>> queryOthersAlbum(Integer num);

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    default <T> void onFinish(int i, T t) {
        if (t instanceof PageResults) {
            try {
                if (i == 1) {
                    onEventFinish(i, (PageResults) t);
                } else {
                    onEventFinish(i, (PageResults) t);
                }
            } catch (Exception unused) {
                DefaultLogger.e("IOtherAlbumDataSource", "call onFinish event, but this [%s] parameter type does not support", ((PageResults) t).getResult().getClass());
            }
        }
    }
}
