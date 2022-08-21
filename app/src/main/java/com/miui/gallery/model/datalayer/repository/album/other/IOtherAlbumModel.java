package com.miui.gallery.model.datalayer.repository.album.other;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IOtherAlbumModel {
    Flowable<Boolean> doChangeAlbumShowInOtherAlbumPage(boolean z, long[] jArr);

    Flowable<PageResults<CoverList>> queryOtherAlbumCovers();

    Flowable<PageResults<List<Album>>> queryOthersAlbum(Integer num);
}
