package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.RemoveSecretById2;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class RemoveSecretMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_RemoveSecretMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long longValue = Numbers.parse(str, -1L).longValue();
        if (longValue == 2147483645) {
            longValue = AlbumCacheManager.getInstance().getScreenshotsAlbumId();
        }
        long j = longValue;
        if (bundle.containsKey("extra_src_media_ids")) {
            removeSecretByIds(context, supportSQLiteDatabase, mediaManager, str, bundle, j, bundle2, arrayList);
        }
    }

    public static void removeSecretByIds(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, long j, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long[] longArray = bundle.getLongArray("extra_src_media_ids");
        if (longArray == null) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < longArray.length; i++) {
            try {
            } catch (StoragePermissionMissingException e) {
                e = e;
            } catch (Exception e2) {
                e = e2;
            }
            try {
                longArray[i] = new RemoveSecretById2(context, arrayList, supportSQLiteDatabase, longArray[i], j).run(supportSQLiteDatabase, mediaManager);
            } catch (StoragePermissionMissingException e3) {
                e = e3;
                linkedList.addAll(e.getPermissionResultList());
            } catch (Exception e4) {
                e = e4;
                DefaultLogger.e("galleryAction_Method_RemoveSecretMethod", "remove from secret error [%d], %s", Long.valueOf(longArray[i]), e);
                longArray[i] = -100;
            }
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        bundle2.putLongArray("ids", longArray);
    }
}
