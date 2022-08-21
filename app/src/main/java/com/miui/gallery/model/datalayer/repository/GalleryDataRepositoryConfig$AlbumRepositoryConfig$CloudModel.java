package com.miui.gallery.model.datalayer.repository;

import android.content.Context;
import com.miui.gallery.model.datalayer.repository.photo.datasource.CloudMemorySourceImpl;
import com.miui.gallery.model.datalayer.repository.photo.datasource.ICloudDataSource;

/* loaded from: classes2.dex */
public class GalleryDataRepositoryConfig$AlbumRepositoryConfig$CloudModel {
    public static ICloudDataSource[] getDataSources(Context context) {
        return new ICloudDataSource[]{new CloudMemorySourceImpl(context)};
    }
}
