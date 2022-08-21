package com.miui.gallery.scanner.utils;

import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class AbsVideoValueCalculator extends AbsValueCalculator {
    public abstract long calcDuration() throws IOException;

    public AbsVideoValueCalculator(String str) {
        super(str);
    }
}
