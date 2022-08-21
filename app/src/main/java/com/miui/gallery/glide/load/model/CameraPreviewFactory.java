package com.miui.gallery.glide.load.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.CameraPreviewManager;
import com.miui.gallery.util.CameraPreviewParams;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class CameraPreviewFactory extends Factory<Bitmap> {
    public CameraPreviewFactory(Context context) {
        super(context, new GalleryModelOpener<Bitmap>() { // from class: com.miui.gallery.glide.load.model.CameraPreviewFactory.1
            @Override // com.miui.gallery.glide.load.model.GalleryModelOpener
            public DataHolder<Bitmap> open(GalleryModel galleryModel, int i, int i2, Options options) {
                DefaultLogger.v("CameraPreviewFactory", "try trigger camera preview mode.");
                CountDownLatch countDownLatch = new CountDownLatch(1);
                CameraPreviewParams requestCameraPreviewParams = CameraPreviewManager.getInstance().requestCameraPreviewParams(countDownLatch, galleryModel.getPath());
                if (requestCameraPreviewParams == null) {
                    try {
                        countDownLatch.await(2L, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    requestCameraPreviewParams = CameraPreviewManager.getInstance().getCameraPreviewParams(galleryModel.getPath());
                }
                if (requestCameraPreviewParams == null || !requestCameraPreviewParams.isValid()) {
                    return null;
                }
                Bitmap createBitmapFromPixels = BaseBitmapUtils.createBitmapFromPixels(requestCameraPreviewParams.getPFD(), requestCameraPreviewParams.getWidth(), requestCameraPreviewParams.getHeight(), requestCameraPreviewParams.getByteCount());
                DefaultLogger.v("CameraPreviewFactory", "camera preview works, bitmap %s.", createBitmapFromPixels);
                return new BitmapHolder(createBitmapFromPixels);
            }
        }, Bitmap.class);
    }

    @Override // com.miui.gallery.glide.load.model.Factory, com.bumptech.glide.load.model.ModelLoaderFactory
    public ModelLoader<GalleryModel, Bitmap> build(MultiModelLoaderFactory multiModelLoaderFactory) {
        return new GalleryModelLoader<Bitmap>(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass, this.opener) { // from class: com.miui.gallery.glide.load.model.CameraPreviewFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.glide.load.model.GalleryModelLoader, com.bumptech.glide.load.model.ModelLoader
            public boolean handles(GalleryModel galleryModel) {
                return galleryModel.isCameraPreview().booleanValue();
            }
        };
    }
}
