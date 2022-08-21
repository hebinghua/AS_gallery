package com.miui.gallery.glide.load.model;

import com.bumptech.glide.load.Options;
import java.io.IOException;

/* loaded from: classes2.dex */
public interface GalleryModelOpener<Data> {
    DataHolder<Data> open(GalleryModel galleryModel, int i, int i2, Options options) throws IOException;

    default void close(DataHolder<Data> dataHolder) throws IOException {
        dataHolder.close();
    }
}
