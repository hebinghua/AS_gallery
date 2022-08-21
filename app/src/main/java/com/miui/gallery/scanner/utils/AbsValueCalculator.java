package com.miui.gallery.scanner.utils;

import java.io.IOException;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class AbsValueCalculator {
    public final String path;

    public abstract long calcDateTaken(long j, long j2, boolean z) throws IOException;

    public abstract Map<String, String> calcExifGpsLocation() throws IOException;

    public abstract int calcExifImageHeight() throws IOException;

    public abstract int calcExifImageWidth() throws IOException;

    public abstract int calcExifOrientation() throws IOException;

    public abstract String calcSha1();

    public abstract long calcSpecialTypeFlags();

    public AbsValueCalculator(String str) {
        this.path = str;
    }
}
