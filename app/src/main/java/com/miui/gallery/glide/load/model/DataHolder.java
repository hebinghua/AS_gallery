package com.miui.gallery.glide.load.model;

import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class DataHolder<Data> {
    public Data data;
    public int requestCode;

    public abstract void close() throws IOException;

    public DataHolder(Data data) {
        this.requestCode = -1;
        this.data = data;
    }

    public DataHolder(Data data, int i) {
        this.requestCode = -1;
        this.data = data;
        this.requestCode = i;
    }
}
