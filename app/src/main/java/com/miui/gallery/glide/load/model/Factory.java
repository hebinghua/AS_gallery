package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.File;

/* loaded from: classes2.dex */
public abstract class Factory<Data> implements ModelLoaderFactory<GalleryModel, Data> {
    public final Context context;
    public final Class<Data> dataClass;
    public final GalleryModelOpener<Data> opener;

    @Override // com.bumptech.glide.load.model.ModelLoaderFactory
    public final void teardown() {
    }

    public Factory(Context context, GalleryModelOpener<Data> galleryModelOpener, Class<Data> cls) {
        this.context = context;
        this.opener = galleryModelOpener;
        this.dataClass = cls;
    }

    @Override // com.bumptech.glide.load.model.ModelLoaderFactory
    public ModelLoader<GalleryModel, Data> build(MultiModelLoaderFactory multiModelLoaderFactory) {
        return new GalleryModelLoader(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass, this.opener);
    }
}
