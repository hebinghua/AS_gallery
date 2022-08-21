package com.miui.gallery.model.datalayer.repository.album.cloud;

import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface ICloudAlbumModel {
    Flowable<Boolean> doChangeAlbumBackupStatus(boolean z, long j);

    Flowable<PageResults<List<Album>>> queryCloudAlbums();
}
