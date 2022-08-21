package com.miui.gallery.provider.cloudmanager.method;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public interface IMethod {
    void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws Exception;

    String getInvokerTag();

    boolean isNeedDoneRemark();

    boolean isNeedFileHandle();

    default Bundle execute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle) throws Exception {
        Bundle bundle2 = new Bundle();
        ArrayList<Long> arrayList = new ArrayList<>();
        doExecute(context, supportSQLiteDatabase, mediaManager, str, bundle, bundle2, arrayList);
        if (!arrayList.isEmpty()) {
            boolean z = false;
            if (bundle != null && bundle.getBoolean("should_operate_sync", false)) {
                z = true;
            }
            if (isNeedFileHandle()) {
                FileHandleManager.handle(context, z, arrayList, isNeedDoneRemark(), getInvokerTag());
            }
        }
        return bundle2;
    }
}
