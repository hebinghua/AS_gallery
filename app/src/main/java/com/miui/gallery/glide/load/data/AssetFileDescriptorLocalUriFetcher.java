package com.miui.gallery.glide.load.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import com.miui.gallery.util.Scheme;
import java.io.FileNotFoundException;
import java.io.IOException;

/* loaded from: classes2.dex */
public class AssetFileDescriptorLocalUriFetcher extends LocalUriFetcher<AssetFileDescriptor> {
    public AssetFileDescriptorLocalUriFetcher(Context context, Uri uri) {
        super(context, uri);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.glide.load.data.LocalUriFetcher
    /* renamed from: loadResource */
    public AssetFileDescriptor mo994loadResource(Uri uri, Context context) throws FileNotFoundException {
        AssetFileDescriptor loadResourceFromUri = loadResourceFromUri(uri, context);
        if (loadResourceFromUri != null) {
            return loadResourceFromUri;
        }
        throw new FileNotFoundException("AssetFileDescriptor is null for " + uri);
    }

    public final AssetFileDescriptor loadResourceFromUri(Uri uri, Context context) throws FileNotFoundException {
        try {
            Scheme ofUri = Scheme.ofUri(uri.toString());
            Scheme scheme = Scheme.ASSETS;
            if (ofUri == scheme) {
                return context.getAssets().openFd(scheme.crop(uri.toString()));
            }
            Scheme ofUri2 = Scheme.ofUri(uri.toString());
            Scheme scheme2 = Scheme.DRAWABLE;
            if (ofUri2 != scheme2) {
                return null;
            }
            return context.getResources().openRawResourceFd(Integer.parseInt(scheme2.crop(uri.toString())));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.miui.gallery.glide.load.data.LocalUriFetcher
    public void close(AssetFileDescriptor assetFileDescriptor) throws IOException {
        assetFileDescriptor.close();
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public Class<AssetFileDescriptor> getDataClass() {
        return AssetFileDescriptor.class;
    }
}
