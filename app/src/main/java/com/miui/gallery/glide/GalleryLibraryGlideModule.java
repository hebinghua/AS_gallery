package com.miui.gallery.glide;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.module.LibraryGlideModule;
import com.miui.gallery.glide.load.data.InputStreamRewinder;
import com.miui.gallery.glide.load.model.CustomUriLoader;
import com.miui.gallery.glide.load.model.PreloadModel;
import com.miui.gallery.glide.load.model.PreloadModelLoader;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class GalleryLibraryGlideModule extends LibraryGlideModule {
    @Override // com.bumptech.glide.module.LibraryGlideModule, com.bumptech.glide.module.RegistersComponents
    public void registerComponents(Context context, Glide glide, Registry registry) {
        Context applicationContext = context.getApplicationContext();
        registry.append(Uri.class, InputStream.class, new CustomUriLoader.StreamFactory(applicationContext));
        registry.append(Uri.class, AssetFileDescriptor.class, new CustomUriLoader.AssetFileDescriptorFactory(applicationContext));
        registry.append(PreloadModel.class, Bitmap.class, new PreloadModelLoader.BitmapFactory());
        registry.replace(GlideUrl.class, InputStream.class, new ModelLoaderFactory<GlideUrl, InputStream>() { // from class: com.miui.gallery.glide.load.model.stream.HttpGlideUrlLoader$Factory
            public final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(500);

            @Override // com.bumptech.glide.load.model.ModelLoaderFactory
            public void teardown() {
            }

            @Override // com.bumptech.glide.load.model.ModelLoaderFactory
            public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
                return new HttpGlideUrlLoader(this.modelCache);
            }
        });
        registry.register(new InputStreamRewinder.Factory(glide.getArrayPool()));
    }
}
