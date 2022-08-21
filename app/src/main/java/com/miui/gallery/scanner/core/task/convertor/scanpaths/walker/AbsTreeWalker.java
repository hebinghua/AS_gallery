package com.miui.gallery.scanner.core.task.convertor.scanpaths.walker;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.storage.constants.StorageConstants;
import java.io.IOException;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public abstract class AbsTreeWalker {
    public ScanTaskConfig mConfig;
    public Context mContext;
    public Path mRoot;

    public abstract void walk(TreeWalkListener treeWalkListener) throws IOException;

    public AbsTreeWalker(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        this.mContext = context;
        this.mRoot = path;
        this.mConfig = scanTaskConfig;
    }

    public static boolean isInBlackList(String str) {
        return str.contains(StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM) || str.contains("MIUI/Gallery/cloud/secretAlbum");
    }
}
