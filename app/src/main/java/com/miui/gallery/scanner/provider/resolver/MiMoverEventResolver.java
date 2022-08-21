package com.miui.gallery.scanner.provider.resolver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.backup.GalleryBackupHelper;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class MiMoverEventResolver extends IScanMethodResolver {
    public static final Uri URL_QUERY_TRANSFER_STATUS = Uri.parse("content://com.miui.huanji.transfer.TransferStatusProvider/transfer_status");

    /* renamed from: $r8$lambda$B-BjLYKdwzi-o3L2rjAdhIX2Me4 */
    public static /* synthetic */ void m1310$r8$lambda$BBjLYKdwzio3L2rjAdhIX2Me4(MiMoverEventResolver miMoverEventResolver) {
        miMoverEventResolver.lambda$pollingMiMoverState$1();
    }

    public static /* synthetic */ Object $r8$lambda$B33Q9w9pWcjtEwPTfXD9NNUhgI4(Set set, Set set2, Cursor cursor) {
        return lambda$readCloudToCache$2(set, set2, cursor);
    }

    /* renamed from: $r8$lambda$P7T7-W5cBxBuuHXTjNxno_2y3yM */
    public static /* synthetic */ Object m1311$r8$lambda$P7T7W5cBxBuuHXTjNxno_2y3yM(MiMoverEventResolver miMoverEventResolver, ThreadPool.JobContext jobContext) {
        return miMoverEventResolver.lambda$onHandleMiMoverEvent$0(jobContext);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public boolean handles(String str) {
        return TextUtils.equals("mi_mover_event", str);
    }

    @Override // com.miui.gallery.scanner.provider.resolver.IScanMethodResolver
    public Bundle resolve(Context context, Bundle bundle) {
        String string = bundle.getString("param_mi_mover_event");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        onHandleMiMoverEvent(string);
        return null;
    }

    public final void onHandleMiMoverEvent(String str) {
        DefaultLogger.d("MiMoverEventResolver", "onHandleMiMoverEvent [%s].", str);
        str.hashCode();
        if (str.equals("mi_mover_event_end")) {
            ScanCache.getInstance().put("key_mi_mover_event_stop", Boolean.TRUE);
            ScannerEngine.getInstance().triggerScan();
        } else if (!str.equals("mi_mover_event_start")) {
        } else {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.scanner.provider.resolver.MiMoverEventResolver$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return MiMoverEventResolver.m1311$r8$lambda$P7T7W5cBxBuuHXTjNxno_2y3yM(MiMoverEventResolver.this, jobContext);
                }
            });
        }
    }

    public /* synthetic */ Object lambda$onHandleMiMoverEvent$0(ThreadPool.JobContext jobContext) {
        Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
        if (bool == null || !bool.booleanValue()) {
            ScanCache.getInstance().put("key_mi_mover_event_start", Boolean.TRUE);
            pollingMiMoverState();
            SyncUtil.stopSync(GalleryApp.sGetAndroidContext());
            GalleryBackupHelper.restoreCloudProfiles();
            readCloudToCache();
            return null;
        }
        return null;
    }

    public final void pollingMiMoverState() {
        ThreadManager.getWorkHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.scanner.provider.resolver.MiMoverEventResolver$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MiMoverEventResolver.m1310$r8$lambda$BBjLYKdwzio3L2rjAdhIX2Me4(MiMoverEventResolver.this);
            }
        }, 60000L);
    }

    public /* synthetic */ void lambda$pollingMiMoverState$1() {
        Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
        if (bool == null || !bool.booleanValue()) {
            return;
        }
        if (isTransferWorking2()) {
            DefaultLogger.d("MiMoverEventResolver", "MiMover is working, keep polling...");
            pollingMiMoverState();
            return;
        }
        DefaultLogger.d("MiMoverEventResolver", "MiMover is not working, create a end event.");
        onHandleMiMoverEvent("mi_mover_event_end");
    }

    public final boolean isTransferWorking2() {
        Cursor query = GalleryApp.sGetAndroidContext().getContentResolver().query(URL_QUERY_TRANSFER_STATUS, null, null, null, null);
        boolean z = false;
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    if (query.getInt(query.getColumnIndex("transfer_status")) == 1) {
                        z = true;
                    }
                }
            } catch (Throwable th) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return z;
    }

    public final void readCloudToCache() {
        if (!BaseMiscUtil.isValid((Set) ScanCache.getInstance().get("key_mi_mover_cloud_sha1_cache")) || !BaseMiscUtil.isValid((Set) ScanCache.getInstance().get("key_mi_mover_cloud_path_cache"))) {
            final Set synchronizedSet = Collections.synchronizedSet(new HashSet());
            final Set synchronizedSet2 = Collections.synchronizedSet(new HashSet());
            SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile", "sha1"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.scanner.provider.resolver.MiMoverEventResolver$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public final Object mo1808handle(Cursor cursor) {
                    return MiMoverEventResolver.$r8$lambda$B33Q9w9pWcjtEwPTfXD9NNUhgI4(synchronizedSet, synchronizedSet2, cursor);
                }
            });
            ScanCache.getInstance().put("key_mi_mover_cloud_sha1_cache", synchronizedSet);
            ScanCache.getInstance().put("key_mi_mover_cloud_path_cache", synchronizedSet2);
            DefaultLogger.d("MiMoverEventResolver", "read cloud info to cache, size [%d].", Integer.valueOf(synchronizedSet.size()));
        }
    }

    public static /* synthetic */ Object lambda$readCloudToCache$2(Set set, Set set2, Cursor cursor) {
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String string = cursor.getString(0);
                String string2 = cursor.getString(1);
                if (TextUtils.isEmpty(string)) {
                    string = string2;
                }
                set.add(cursor.getString(2));
                if (!TextUtils.isEmpty(string)) {
                    set2.add(string.toLowerCase());
                }
                cursor.moveToNext();
            }
        }
        return null;
    }
}
