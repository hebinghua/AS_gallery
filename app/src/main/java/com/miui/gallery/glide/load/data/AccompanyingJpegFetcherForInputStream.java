package com.miui.gallery.glide.load.data;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class AccompanyingJpegFetcherForInputStream implements IThumbFetcher<String, InputStream> {
    @Override // com.miui.gallery.glide.load.data.IThumbFetcher
    public InputStream load(String str) throws IOException {
        DocumentFile load = new AccompanyingJpegFetcherForFile().load(str);
        if (load != null) {
            return new BufferedInputStream(StorageSolutionProvider.get().openInputStream(load), 32768);
        }
        return null;
    }
}
