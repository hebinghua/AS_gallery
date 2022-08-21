package com.miui.gallery.model.dto;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class AlbumList extends ArrayList<Album> implements Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public AlbumList(int i) {
        super(i);
    }

    public AlbumList() {
    }
}
