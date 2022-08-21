package com.miui.gallery.model.datalayer.repository;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumDBSnapSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumMemoryDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.ICommonAlbumDataSource;

/* loaded from: classes2.dex */
public class GalleryDataRepositoryConfig$AlbumRepositoryConfig$CommonAlbumModel {
    public static ICommonAlbumDataSource[] getDataSources(Context context) {
        return new ICommonAlbumDataSource[]{new CommonAlbumMemoryDataSourceImpl(context)};
    }

    public static ICommonAlbumDataSource[] getSnapDataSources(Context context) {
        return new ICommonAlbumDataSource[]{new CommonAlbumDBSnapSourceImpl()};
    }
}
