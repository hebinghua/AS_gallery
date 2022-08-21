package com.miui.gallery.scanner.core.task.convertor.scanpaths.walker;

import android.content.Context;
import android.os.Build;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public class TreeWalkerProvider {
    public static AbsTreeWalker acquire(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        if (Build.VERSION.SDK_INT > 29) {
            return new MediaTreeWalker(context, path, scanTaskConfig);
        }
        return new FileTreeWalker(context, path, scanTaskConfig);
    }
}
