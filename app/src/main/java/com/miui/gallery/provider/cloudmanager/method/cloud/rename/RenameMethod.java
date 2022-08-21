package com.miui.gallery.provider.cloudmanager.method.cloud.rename;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class RenameMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_RenameMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long j = -100;
        if (TextUtils.isEmpty(str) || bundle == null) {
            DefaultLogger.e("galleryAction_Method_RenameMethod", "newName || extras is null");
            bundle2.putLong("id", -100L);
            return;
        }
        LinkedList linkedList = new LinkedList();
        try {
            j = RenameFactory.create(context, arrayList, bundle, str, supportSQLiteDatabase, mediaManager).run(supportSQLiteDatabase, mediaManager);
        } catch (StoragePermissionMissingException e) {
            linkedList.addAll(e.getPermissionResultList());
            j = -101;
        } catch (Exception e2) {
            DefaultLogger.e("galleryAction_Method_RenameMethod", e2);
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        DefaultLogger.d("galleryAction_Method_RenameMethod", "rename [%s] => resultCount [%d]", str, Long.valueOf(j));
        bundle2.putLong("id", j);
    }
}
