package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

/* loaded from: classes.dex */
public interface DiskCache {

    /* loaded from: classes.dex */
    public interface Factory {
        DiskCache build();
    }

    /* loaded from: classes.dex */
    public interface Writer {
        boolean write(File file);
    }

    File get(Key key);

    void put(Key key, Writer writer);
}
