package com.miui.gallery.model.datalayer.repository.album.hidden;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IHiddenAlbumModel {
    Flowable<Boolean> cancelAlbumHiddenStatus(long j);

    Flowable<Boolean> doChangeAlbumHiddenStatus(boolean z, long[] jArr);

    Flowable<PageResults<List<Album>>> queryHiddenAlbum();
}
