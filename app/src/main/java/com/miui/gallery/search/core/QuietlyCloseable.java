package com.miui.gallery.search.core;

import java.io.Closeable;

/* loaded from: classes2.dex */
public interface QuietlyCloseable extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}
