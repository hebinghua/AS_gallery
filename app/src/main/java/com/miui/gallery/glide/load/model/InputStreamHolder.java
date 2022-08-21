package com.miui.gallery.glide.load.model;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class InputStreamHolder extends DataHolder<InputStream> {
    public InputStreamHolder(InputStream inputStream) {
        super(inputStream);
    }

    public InputStreamHolder(InputStream inputStream, int i) {
        super(inputStream, i);
    }

    @Override // com.miui.gallery.glide.load.model.DataHolder
    public void close() throws IOException {
        ((InputStream) this.data).close();
    }
}
