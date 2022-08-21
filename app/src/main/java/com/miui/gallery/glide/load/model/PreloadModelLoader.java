package com.miui.gallery.glide.load.model;

import android.graphics.Bitmap;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

/* loaded from: classes2.dex */
public class PreloadModelLoader implements ModelLoader<PreloadModel, Bitmap> {
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Bitmap> buildLoadData(PreloadModel preloadModel, int i, int i2, Options options) {
        return new ModelLoader.LoadData<>(new ObjectKey(preloadModel), new BitmapFetcher(preloadModel));
    }

    @Override // com.bumptech.glide.load.model.ModelLoader
    public boolean handles(PreloadModel preloadModel) {
        return preloadModel.getBitmap() != null;
    }

    /* loaded from: classes2.dex */
    public static final class BitmapFetcher implements DataFetcher<Bitmap> {
        public final PreloadModel mModel;

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cancel() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cleanup() {
        }

        public BitmapFetcher(PreloadModel preloadModel) {
            this.mModel = preloadModel;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Bitmap> dataCallback) {
            Bitmap bitmap = this.mModel.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                dataCallback.onDataReady(bitmap.copy(Bitmap.Config.ARGB_8888, true));
            } else {
                dataCallback.onLoadFailed(new IllegalArgumentException("Bitmap is null"));
            }
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public Class<Bitmap> getDataClass() {
            return Bitmap.class;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }
    }

    /* loaded from: classes2.dex */
    public static final class BitmapFactory implements ModelLoaderFactory<PreloadModel, Bitmap> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public void teardown() {
        }

        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<PreloadModel, Bitmap> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new PreloadModelLoader();
        }
    }
}
