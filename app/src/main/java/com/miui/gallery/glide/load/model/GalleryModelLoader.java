package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.glide.Utils;
import java.io.File;

/* loaded from: classes2.dex */
public class GalleryModelLoader<Data> implements ModelLoader<GalleryModel, Data> {
    public final Context mContext;
    public final Class<Data> mDataClass;
    public final ModelLoader<File, Data> mFileDelegate;
    public final GalleryModelOpener<Data> mOpener;
    public final ModelLoader<Uri, Data> mUriDelegate;

    public GalleryModelLoader(Context context, ModelLoader<File, Data> modelLoader, ModelLoader<Uri, Data> modelLoader2, Class<Data> cls, GalleryModelOpener<Data> galleryModelOpener) {
        this.mContext = context.getApplicationContext();
        this.mFileDelegate = modelLoader;
        this.mUriDelegate = modelLoader2;
        this.mDataClass = cls;
        this.mOpener = galleryModelOpener;
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Data> buildLoadData(GalleryModel galleryModel, int i, int i2, Options options) {
        Glide glide = Glide.get(this.mContext);
        return new ModelLoader.LoadData<>(new ObjectKey(galleryModel), new GalleryDataFetcher(galleryModel, this.mOpener, this.mFileDelegate, this.mUriDelegate, i, i2, options, this.mDataClass, glide.getRegistry().getImageHeaderParsers(), glide.getArrayPool(), glide.getBitmapPool(), glide.getRegistry()));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public boolean handles(GalleryModel galleryModel) {
        return Utils.parseUri(galleryModel.getPath()) != null;
    }
}
