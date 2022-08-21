package com.miui.gallery.model.datalayer.repository;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.IOtherAlbumDataSource;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.local.OtherAlbumFileCacheDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.other.datasource.memory.OtherAlbumMemoryDataSourceImpl;

/* loaded from: classes2.dex */
public class GalleryDataRepositoryConfig$AlbumRepositoryConfig$OtherAlbumModel {
    public static IOtherAlbumDataSource[] getDataSources(int i, Context context) {
        return new IOtherAlbumDataSource[]{new OtherAlbumFileCacheDataSourceImpl(context), new OtherAlbumMemoryDataSourceImpl(context)};
    }
}
