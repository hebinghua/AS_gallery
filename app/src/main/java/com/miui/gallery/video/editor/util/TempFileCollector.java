package com.miui.gallery.video.editor.util;

import android.content.Context;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import java.util.Arrays;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class TempFileCollector {
    public static LinkedList<String> tempFilePaths = new LinkedList<>();

    public static /* synthetic */ void $r8$lambda$3Zwf6EFcWiiXbscUuhBC2cNIsiM(Context context, String[] strArr) {
        lambda$deleteAllTempFile$0(context, strArr);
    }

    public static void add(String str) {
        DefaultLogger.d("TempFileCollector", "add %s", str);
        tempFilePaths.add(str);
    }

    public static void deleteAllTempFile(final Context context) {
        if (tempFilePaths.size() > 0) {
            final String[] strArr = new String[tempFilePaths.size()];
            tempFilePaths.toArray(strArr);
            tempFilePaths.clear();
            ThreadManager.execute(47, new Runnable() { // from class: com.miui.gallery.video.editor.util.TempFileCollector$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TempFileCollector.$r8$lambda$3Zwf6EFcWiiXbscUuhBC2cNIsiM(context, strArr);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$deleteAllTempFile$0(Context context, String[] strArr) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            CloudUtils.deleteByPath(context, 56, strArr);
        } catch (StoragePermissionMissingException e) {
            e.printStackTrace();
        }
        DefaultLogger.d("TempFileCollector", "deleteAllTempFile %s, cost %s", Arrays.toString(strArr), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }
}
