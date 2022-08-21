package com.miui.gallery.model.datalayer.repository.album.common;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public interface ICommonAlbumModel {
    Flowable<Boolean> doChangeAlbumShowInPhotoTabPage(boolean z, long j);

    Flowable<Boolean> doChangeAlbumSortPosition(long[] jArr, String[] strArr);

    Flowable<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> doReplaceAlbumCover(long j, long[] jArr);

    Flowable<PageResults<String>> queryAlbumName(long j);

    Flowable<PageResults<List<Album>>> queryAlbums(long j, QueryParam queryParam);
}
