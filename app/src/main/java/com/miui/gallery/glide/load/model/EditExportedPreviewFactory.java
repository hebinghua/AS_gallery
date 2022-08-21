package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import java.io.File;

/* loaded from: classes2.dex */
public class EditExportedPreviewFactory extends Factory<Bitmap> {
    public EditExportedPreviewFactory(Context context) {
        super(context, new GalleryModelOpener<Bitmap>() { // from class: com.miui.gallery.glide.load.model.EditExportedPreviewFactory.1
            @Override // com.miui.gallery.glide.load.model.GalleryModelOpener
            public DataHolder<Bitmap> open(GalleryModel galleryModel, int i, int i2, Options options) {
                DefaultLogger.v("EditExportedPreviewFactory", "try trigger edited exported preview mode.");
                Bitmap imageCache = MediaEditorApiHelper.getImageCache(Uri.parse(galleryModel.getPath()));
                if (imageCache == null || imageCache.isRecycled()) {
                    return null;
                }
                DefaultLogger.v("EditExportedPreviewFactory", "edited exported preview works, bitmap %s.", imageCache);
                return new BitmapHolder(imageCache);
            }
        }, Bitmap.class);
    }

    @Override // com.miui.gallery.glide.load.model.Factory, com.bumptech.glide.load.model.ModelLoaderFactory
    public ModelLoader<GalleryModel, Bitmap> build(MultiModelLoaderFactory multiModelLoaderFactory) {
        return new GalleryModelLoader<Bitmap>(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass, this.opener) { // from class: com.miui.gallery.glide.load.model.EditExportedPreviewFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.glide.load.model.GalleryModelLoader, com.bumptech.glide.load.model.ModelLoader
            public boolean handles(GalleryModel galleryModel) {
                return galleryModel.isIsJustEditExported() && MediaEditorApiHelper.isJustEditExportedPath(galleryModel.getPath());
            }
        };
    }
}
