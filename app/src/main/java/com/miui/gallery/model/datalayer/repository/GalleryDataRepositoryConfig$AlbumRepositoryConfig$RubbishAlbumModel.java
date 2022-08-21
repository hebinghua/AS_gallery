package com.miui.gallery.model.datalayer.repository;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.IRubbishAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.local.RubbishAlbumFileCacheSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.rubbish.datasource.memory.RubbishAlbumMemorySourceImpl;

/* loaded from: classes2.dex */
public class GalleryDataRepositoryConfig$AlbumRepositoryConfig$RubbishAlbumModel {
    public static IRubbishAlbumDataSource[] getDataSources(Context context) {
        return new IRubbishAlbumDataSource[]{new RubbishAlbumFileCacheSourceImpl(context), new RubbishAlbumMemorySourceImpl(context)};
    }
}
