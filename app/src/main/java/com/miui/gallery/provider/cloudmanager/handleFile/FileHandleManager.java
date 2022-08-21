package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class FileHandleManager {
    public static void handle(Context context, boolean z, List<Long> list, boolean z2, String str) {
        if (context == null || !BaseMiscUtil.isValid(list)) {
            return;
        }
        if (z) {
            executeSync(context, list, z2, str);
        } else {
            executeAsync(context, list, z2, str);
        }
    }

    public static void executeSync(Context context, List<Long> list, boolean z, String str) {
        DefaultLogger.d("galleryAction_FileHandle_Manager", "executeSync => count = [%d]", Integer.valueOf(list.size()));
        long[] run = new FileHandleTask(context, Numbers.toArray(list), str).run(null, null);
        if (!BaseMiscUtil.isValid(run)) {
            DefaultLogger.e("galleryAction_FileHandle_Manager", "executeSync => error ! results inValid");
            return;
        }
        if (checkAndNotifyUri(context, run)) {
            requestSync(context);
        }
        long[] jArr = new long[run.length];
        int i = 0;
        for (int i2 = 0; i2 < run.length; i2++) {
            if (run[i2] == 1) {
                jArr[i] = list.get(i2).longValue();
                i++;
            }
        }
        if (!BaseMiscUtil.isValid(jArr) || !z) {
            return;
        }
        RemarkManager.doneRemarkMediaIds(jArr);
    }

    public static void executeAsync(Context context, List<Long> list, boolean z, String str) {
        DefaultLogger.d("galleryAction_FileHandle_Manager", "executeAsync => count = [%d]", Integer.valueOf(list.size()));
        Intent intent = new Intent("dispatch_media_ids");
        intent.setClass(context, FileHandleService.class);
        intent.putExtra("ids", Numbers.toArray(list));
        intent.putExtra("remark", z);
        intent.putExtra("extra_invoker_tag", str);
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent);
    }

    public static boolean checkAndNotifyUri(Context context, long[] jArr) {
        if (context == null || !BaseMiscUtil.isValid(jArr)) {
            return false;
        }
        int length = jArr.length;
        int i = 0;
        boolean z = false;
        while (true) {
            boolean z2 = true;
            if (i >= length) {
                break;
            }
            if (jArr[i] != 1) {
                z2 = false;
            }
            z |= z2;
            i++;
        }
        if (!z) {
            return false;
        }
        DefaultLogger.d("galleryAction_FileHandle_Manager", "notifyUri => ");
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.notifyChange(GalleryContract.Cloud.CLOUD_URI, (ContentObserver) null, false);
        contentResolver.notifyChange(GalleryContract.Media.URI, (ContentObserver) null, false);
        contentResolver.notifyChange(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, (ContentObserver) null, false);
        contentResolver.notifyChange(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, (ContentObserver) null, false);
        return true;
    }

    public static void requestSync(Context context) {
        if (context == null) {
            return;
        }
        DefaultLogger.d("galleryAction_FileHandle_Manager", "requestSync => ");
        SyncUtil.requestSync(context, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(561L).build());
    }

    public static void checkUnhandledMedias(Context context) {
        if (context == null) {
            return;
        }
        long[] loadUnHandleMediaIds = RemarkManager.loadUnHandleMediaIds();
        if (!BaseMiscUtil.isValid(loadUnHandleMediaIds)) {
            return;
        }
        DefaultLogger.d("galleryAction_FileHandle_Manager", "handlePendingMedias => count = [%d]", Integer.valueOf(loadUnHandleMediaIds.length));
        Intent intent = new Intent("check_unhandled_media_ids");
        intent.setClass(context, FileHandleService.class);
        intent.putExtra("ids", loadUnHandleMediaIds);
        intent.putExtra("remark", true);
        intent.putExtra("extra_invoker_tag", "checkUnhandledMedias");
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent);
    }

    public static void deleteFileToTrash(Context context, Cursor cursor, long j, int i, boolean z, String str) {
        MediaFileHandleJob.doDelete(context, cursor, j, i, z, str);
    }

    public static boolean deleteFile(String str, int i, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return MediaFileHandleJob.doDelete(str, i, str2);
    }
}
