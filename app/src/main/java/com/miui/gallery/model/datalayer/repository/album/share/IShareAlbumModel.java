package com.miui.gallery.model.datalayer.repository.album.share;

import com.miui.gallery.model.dto.ShareAlbum;
import io.reactivex.Flowable;
import java.util.List;

/* loaded from: classes2.dex */
public interface IShareAlbumModel {
    Flowable<List<ShareAlbum>> queryAlbumListShareInfo();
}
