package com.miui.gallery.storage.utils;

import android.content.Context;
import java.util.List;

/* loaded from: classes2.dex */
public interface IFilePathResolver extends IStorageFunction {
    List<String> getPaths(Object obj, int i);

    @Override // com.miui.gallery.storage.utils.IStorageFunction
    default boolean handles(Context context, int i, int i2) {
        return true;
    }
}
