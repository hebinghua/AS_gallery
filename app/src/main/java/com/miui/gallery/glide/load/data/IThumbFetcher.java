package com.miui.gallery.glide.load.data;

import java.io.IOException;

/* loaded from: classes2.dex */
public interface IThumbFetcher<PARAMS, RESULT> {
    RESULT load(PARAMS params) throws IOException;

    default void release(RESULT result) {
    }
}
