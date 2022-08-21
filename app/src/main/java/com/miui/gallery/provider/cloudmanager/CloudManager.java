package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.IMethod;

/* loaded from: classes2.dex */
public class CloudManager {
    public static boolean canHandle(String str) {
        return getMethod(str) != null;
    }

    public static Bundle call(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, String str2, Bundle bundle) throws Exception {
        IMethod method = getMethod(str);
        if (method == null) {
            return Bundle.EMPTY;
        }
        return method.execute(context, supportSQLiteDatabase, mediaManager, str2, bundle);
    }

    public static IMethod getMethod(String str) {
        return (IMethod) StrategyRegistry.getInstance().get(str);
    }
}
