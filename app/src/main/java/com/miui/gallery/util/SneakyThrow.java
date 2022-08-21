package com.miui.gallery.util;

/* loaded from: classes2.dex */
public class SneakyThrow {
    public static void reThrow(Throwable th) {
        sneakyThrow(th);
    }

    public static <E extends Throwable> void sneakyThrow(Throwable th) throws Throwable {
        throw th;
    }
}
