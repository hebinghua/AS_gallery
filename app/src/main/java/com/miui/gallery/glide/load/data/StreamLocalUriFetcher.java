package com.miui.gallery.glide.load.data;

import android.content.Context;
import android.net.Uri;
import com.miui.gallery.util.Scheme;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class StreamLocalUriFetcher extends LocalUriFetcher<InputStream> {
    public StreamLocalUriFetcher(Context context, Uri uri) {
        super(context, uri);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.glide.load.data.LocalUriFetcher
    /* renamed from: loadResource */
    public InputStream mo994loadResource(Uri uri, Context context) throws FileNotFoundException {
        InputStream loadResourceFromUri = loadResourceFromUri(uri, context);
        if (loadResourceFromUri != null) {
            return loadResourceFromUri;
        }
        throw new FileNotFoundException("InputStream is null for " + uri);
    }

    public final InputStream loadResourceFromUri(Uri uri, Context context) throws FileNotFoundException {
        try {
            Scheme ofUri = Scheme.ofUri(uri.toString());
            Scheme scheme = Scheme.ASSETS;
            if (ofUri == scheme) {
                return context.getAssets().open(scheme.crop(uri.toString()));
            }
            Scheme ofUri2 = Scheme.ofUri(uri.toString());
            Scheme scheme2 = Scheme.DRAWABLE;
            if (ofUri2 != scheme2) {
                return null;
            }
            return context.getResources().openRawResource(Integer.parseInt(scheme2.crop(uri.toString())));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.miui.gallery.glide.load.data.LocalUriFetcher
    public void close(InputStream inputStream) throws IOException {
        inputStream.close();
    }

    @Override // com.bumptech.glide.load.data.DataFetcher
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }
}
