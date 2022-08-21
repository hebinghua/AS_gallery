package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.EditDateTimeFactory;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class EditPhotoDateTimeMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_EditDateTime";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long j = -100;
        if (bundle == null) {
            DefaultLogger.e("galleryAction_EditDateTime", "extras is null");
            bundle2.putLong("id", -100L);
            return;
        }
        long[] jArr = new long[1];
        try {
            j = EditDateTimeFactory.create(context, supportSQLiteDatabase, arrayList, bundle, bundle2).run(supportSQLiteDatabase, mediaManager);
        } catch (StoragePermissionMissingException e) {
            throw new StoragePermissionMissingException(new LinkedList(e.getPermissionResultList()));
        } catch (Exception e2) {
            DefaultLogger.e("galleryAction_EditDateTime", e2);
        }
        jArr[0] = j;
        bundle2.putBoolean("should_request_sync", true);
        bundle2.putLongArray("ids", jArr);
    }
}
