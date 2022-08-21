package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class PixelsBlobFactory extends Factory<Bitmap> {
    public PixelsBlobFactory(Context context) {
        super(context, PixelsBlobFactory$$ExternalSyntheticLambda0.INSTANCE, Bitmap.class);
    }

    public static /* synthetic */ DataHolder lambda$new$0(GalleryModel galleryModel, int i, int i2, Options options) throws IOException {
        int length = galleryModel.getBlob().length;
        Bitmap.Config matchConfig = BaseBitmapUtils.matchConfig(i, i2, length);
        if (matchConfig == null) {
            throw new IOException(String.format("Invalid blob size [%d] for requested dimensions [%dx%d], %s", Integer.valueOf(length), Integer.valueOf(i), Integer.valueOf(i2), galleryModel.getPath()));
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, matchConfig);
            createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(galleryModel.getBlob()));
            DefaultLogger.v("PixelsBlobFactory", "Load bitmap from pixels for %s", galleryModel.getPath());
            return new BitmapHolder(createBitmap);
        } catch (Exception e) {
            throw new IOException("Failed to create bitmap from pixels", e);
        }
    }

    @Override // com.miui.gallery.glide.load.model.Factory, com.bumptech.glide.load.model.ModelLoaderFactory
    public ModelLoader<GalleryModel, Bitmap> build(MultiModelLoaderFactory multiModelLoaderFactory) {
        return new GalleryModelLoader<Bitmap>(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass, this.opener) { // from class: com.miui.gallery.glide.load.model.PixelsBlobFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.glide.load.model.GalleryModelLoader, com.bumptech.glide.load.model.ModelLoader
            public boolean handles(GalleryModel galleryModel) {
                return galleryModel.getBlob() != null;
            }
        };
    }
}
